package cc.hyperium.mixins.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBucket.class)
public class MixinItemBucket {
    @Shadow private Block isFull;

    @Overwrite
    public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos) {
        if (isFull == Blocks.air) {
            return false;
        }
        final Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
        final boolean flag = !material.isSolid();
        if (!worldIn.isAirBlock(pos) && !flag) return false;
        if (worldIn.provider.doesWaterVaporize() && isFull == Blocks.flowing_water) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            worldIn.playSoundEffect((double)(i + 0.5f), (double)(j + 0.5f), (double)(k + 0.5f), "random.fizz", 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
            for (int l = 0; l < 8; ++l) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            }
        } else {
            if (!worldIn.isRemote && flag && !material.isLiquid()) {
                worldIn.destroyBlock(pos, true);
            }
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, isFull.getDefaultState(), 3);
            }
        }
        return true;
    }
}
