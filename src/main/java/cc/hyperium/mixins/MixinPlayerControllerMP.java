package cc.hyperium.mixins;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "onPlayerRightClick", at = @At("HEAD"))
    private void placeClientLiquid(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec, CallbackInfoReturnable cir) {
        if (heldStack == null) return;
        final float f = (float)(hitVec.xCoord - hitPos.getX());
        final float f2 = (float)(hitVec.yCoord - hitPos.getY());
        final float f3 = (float)(hitVec.zCoord - hitPos.getZ());
        final C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f2, f3);
        if (heldStack.getUnlocalizedName().equals("item.bucketLava")) {
            worldIn.setBlockState(new BlockPos((double)packet.getPlacedBlockOffsetX(), (double)packet.getPlacedBlockOffsetY(), (double)packet.getPlacedBlockOffsetZ()), Blocks.flowing_lava.getDefaultState());
        } else if (heldStack.getUnlocalizedName().equals("item.bucketWater")) {
            worldIn.setBlockState(new BlockPos((double)packet.getPlacedBlockOffsetX(), (double)packet.getPlacedBlockOffsetY(), (double)packet.getPlacedBlockOffsetZ()), Blocks.flowing_water.getDefaultState());
        }
    }
}
