package cc.hyperium.handlers.handlers;

import cc.hyperium.config.Settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

// TODO: do we really need this?
public class SettingsHandler {
    private HashMap<Field, Supplier<String[]>> customStates = new HashMap<>();
    private HashMap<Field, List<Consumer<Object>>> callbacks = new HashMap<>();
    private List<Object> settingsObjects = new ArrayList<>();

    public SettingsHandler() {
        try {
            Field max_particle_string = Settings.class.getField("MAX_PARTICLE_STRING");
            customStates.put(max_particle_string, () -> new String[] {
                    "25",
                    "50",
                    "100",
                    "150",
                    "200",
                    "250",
                    "300"
            });

            registerCallback(Settings.class.getField("MAX_WORLD_PARTICLES_STRING"), o -> {
                try {
                    Settings.MAX_WORLD_PARTICLES_INT = Integer.parseInt(o.toString());
                } catch (Exception ignored) {

                }
            });
            registerCallback(max_particle_string, o -> {
                try {
                    Settings.MAX_PARTICLES = Integer.parseInt(o.toString());
                } catch (Exception ignored) {
                }
            });
            registerCallback(Settings.class.getField("HEAD_SCALE_FACTOR_STRING"), o -> {
                try {
                    Settings.HEAD_SCALE_FACTOR = Double.parseDouble(o.toString());
                } catch (Exception ignored) {
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void registerCallback(Field field, Consumer<Object> callback) {
        this.callbacks.computeIfAbsent(field, tmp -> new ArrayList<>()).add(callback);
    }

    public HashMap<Field, Supplier<String[]>> getCustomStates() {
        return customStates;
    }

    public HashMap<Field, List<Consumer<Object>>> getcallbacks() {
        return callbacks;
    }

    public List<Object> getSettingsObjects() {
        return settingsObjects;
    }
}
