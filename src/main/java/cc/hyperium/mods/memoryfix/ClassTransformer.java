package cc.hyperium.mods.memoryfix;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.ClassReader;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.commons.ClassRemapper;
import org.spongepowered.asm.lib.commons.Remapper;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Iterator;
import java.util.function.BiConsumer;

public class ClassTransformer implements IClassTransformer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        switch (name) {
            case "CapeUtils":
                // Use our CapeImageBuffer instead of OptiFine's
                return transformCapeUtils(bytes);
            case "cc.hyperium.mods.memoryfix.CapeImageBuffer":
                // Redirect our stub calls to optifine
                return transformMethods(bytes, this::transformCapeImageBuffer);
            default:
                return bytes;
        }
    }

    private byte[] transformMethods(byte[] bytes, BiConsumer<ClassNode, MethodNode> transformer) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            transformer.accept(classNode, methodNode);
        }

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] transformCapeUtils(byte[] bytes) {
        LOGGER.info("Transforming OptiFine CapeUtils...");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassRemapper adapter = new ClassRemapper(classWriter, new Remapper() {
            @Override
            public String map(String typeName) {
                if ("CapeUtils$1".equals(typeName)) {
                    return "cc/hyperium/mods/memoryfix/CapeImageBuffer";
                }
                return typeName;
            }
        });

        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(adapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    private void transformCapeImageBuffer(ClassNode clazz, MethodNode method) {
        LOGGER.info("Transforming MemoryFix CapeImageBuffer...");
        Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals("parseCape")) {
                    methodInsn.owner = "CapeUtils";
                } else if (methodInsn.name.equals("setLocationOfCape")) {
                    methodInsn.setOpcode(Opcodes.INVOKEVIRTUAL);
                    methodInsn.owner = "net/minecraft/client/entity/AbstractClientPlayer";
                    methodInsn.desc = "(Lnet/minecraft/util/ResourceLocation;)V";
                }
            }
        }
    }
}
