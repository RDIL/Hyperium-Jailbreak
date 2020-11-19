package cc.hyperium.internal.addons;

import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.commons.ClassRemapper;
import org.spongepowered.asm.lib.commons.Remapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link net.minecraft.launchwrapper.IClassTransformer} that ensures external addons built against
 * older versions of the client properly resolve some classes that have been moved.
 */
public class AddonDeprecationRemapper implements IClassTransformer {
    public static final Map<String, String> REPLACEMENTS = new ConcurrentHashMap<>();

    static {
        REPLACEMENTS.putIfAbsent("cc.hyperium.event.ChatEvent", "cc.hyperium.event.network.chat.ChatEvent");
        REPLACEMENTS.putIfAbsent("cc.hyperium.event.InitializationEvent", "cc.hyperium.event.client.InitializationEvent");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        // sanity check
        if (basicClass == null) {
            return null;
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassRemapper adapter = new ClassRemapper(classWriter, new Remapper() {
            @Override
            public String map(String typeName) {
                if (REPLACEMENTS.containsKey(typeName)) {
                    return REPLACEMENTS.get(typeName);
                }
                return typeName;
            }
        });

        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(adapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
