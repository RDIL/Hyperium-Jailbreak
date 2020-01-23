package cc.hyperium.event.model;

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;

public final class PreCopyPlayerModelAnglesEvent extends CopyPlayerModelAnglesEvent {
    public PreCopyPlayerModelAnglesEvent(@NotNull AbstractClientPlayer entity, @NotNull IMixinModelBiped model) {
        super(entity, model);
    }
}
