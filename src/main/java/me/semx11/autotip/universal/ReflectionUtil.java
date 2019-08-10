package me.semx11.autotip.universal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {
    private static Map<String, Class<?>> loadedClasses = new HashMap<>();
    private static Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<>();
    private static Map<Class<?>, Map<String, Field>> loadedFields = new HashMap<>();
    public static Class<?> findClazz(String... classNames) {
        for (String className : classNames) {
            if (loadedClasses.containsKey(className)) {
                return loadedClasses.get(className);
            }
        }

        Exception err = null;
        for (String className : classNames) {
            try {
                Class clazz = Class.forName(className);
                loadedClasses.put(className, clazz);
                return clazz;
            } catch (ClassNotFoundException e) {
                err = e;
            }
        }
        throw new UnableToFindClassException(classNames, err);
    }

    public static Method findMethod(Class<?> clazz, String[] methodNames, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<>());
        }

        Map<String, Method> clazzMethods = loadedMethods.get(clazz);

        Exception err = null;
        for (String methodName : methodNames) {
            if (clazzMethods.containsKey(methodName)) {
                return clazzMethods.get(methodName);
            }

            try {
                Method method = clazz.getMethod(methodName, params);
                method.setAccessible(true);

                clazzMethods.put(methodName, method);
                loadedMethods.put(clazz, clazzMethods);
                return method;
            } catch (NoSuchMethodException e) {
                err = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, err);
    }

    public static Field findField(Class<?> clazz, String... fieldNames) {
        if (!loadedFields.containsKey(clazz)) {
            loadedFields.put(clazz, new HashMap<>());
        }

        Map<String, Field> clazzFields = loadedFields.get(clazz);

        Exception err = null;
        for (String fieldName : fieldNames) {
            if (clazzFields.containsKey(fieldName)) {
                return clazzFields.get(fieldName);
            }

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                clazzFields.put(fieldName, field);
                loadedFields.put(clazz, clazzFields);
                return field;
            } catch (NoSuchFieldException e) {
                err = e;
            } catch (SecurityException e) {
                throw new UnableToAccessFieldException(fieldNames, e);
            }
        }
        throw new UnableToFindFieldException(fieldNames, err);
    }

    public static class UnableToFindMethodException extends RuntimeException {
        private static final long serialVersionUID = 2646222778476346499L;

        UnableToFindMethodException(String[] methodNames, Exception e) {
            super("Could not find methods: " + String.join(", ", methodNames), e);
        }
    }

    public static class UnableToFindClassException extends RuntimeException {
        private static final long serialVersionUID = 3898634214210207487L;
        UnableToFindClassException(String[] classNames, Exception e) {
            super("Could not find classes: " + String.join(", ", classNames), e);
        }
    }

    public static class UnableToAccessFieldException extends RuntimeException {
        private static final long serialVersionUID = 3624431716334716913L;
        UnableToAccessFieldException(String[] fieldNames, Exception e) {
            super("Could not access fields: " + String.join(", ", fieldNames), e);
        }
    }

    public static class UnableToFindFieldException extends RuntimeException {
        private static final long serialVersionUID = 6746871885265039462L;
        UnableToFindFieldException(String[] fieldNames, Exception e) {
            super("Could not find fields: " + String.join(", ", fieldNames), e);
        }
    }
}
