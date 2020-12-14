package cc.hyperium.mixins;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(WorldGenBigTree.class)
public class MixinWorldGenBigTree {
    @Shadow private World world;

    @Inject(method = "generate", at = @At(value = "RETURN", ordinal = 0))
    private void dontStoreWorldAfterFail(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir) {
        world = null;
    }

    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/WorldGenBigTree;generateLeafNodeBases()V", shift = At.Shift.AFTER))
    private void dontStoreWorldAfterGen(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir) {
        world = null;
    }
}
