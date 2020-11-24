package cc.hyperium.mixins;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.interact.PlayerDestroyBlockEvent;
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

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "onPlayerRightClick", at = @At("HEAD"))
    private void placeClientLiquid(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 hitVec, CallbackInfoReturnable<Boolean> cir) {
        if (heldStack == null) return;
        final float f = (float)(hitVec.xCoord - hitPos.getX());
        final float f2 = (float)(hitVec.yCoord - hitPos.getY());
        final float f3 = (float)(hitVec.zCoord - hitPos.getZ());
        final C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement(hitPos, side.getIndex(), player.inventory.getCurrentItem(), f, f2, f3);
        if (heldStack.getUnlocalizedName().equals("item.bucketLava")) {
            worldIn.setBlockState(new BlockPos(packet.getPlacedBlockOffsetX(), packet.getPlacedBlockOffsetY(), packet.getPlacedBlockOffsetZ()), Blocks.flowing_lava.getDefaultState());
        } else if (heldStack.getUnlocalizedName().equals("item.bucketWater")) {
            worldIn.setBlockState(new BlockPos(packet.getPlacedBlockOffsetX(), packet.getPlacedBlockOffsetY(), packet.getPlacedBlockOffsetZ()), Blocks.flowing_water.getDefaultState());
        }
    }

    @Inject(method = "onPlayerDestroyBlock", at = @At("RETURN"), cancellable = true)
    public void onPlayerDestroyBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        final PlayerDestroyBlockEvent dbe = new PlayerDestroyBlockEvent(pos);
        EventBus.INSTANCE.post(dbe);
        if (dbe.isCancelled()) {
            cir.setReturnValue(false);
        }
    }
}
