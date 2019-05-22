package cc.hyperium.mixinsimp.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.mixins.entity.IMixinNetworkPlayerInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class HyperiumNetworkPlayerInfo {
    private NetworkPlayerInfo parent;

    public HyperiumNetworkPlayerInfo(NetworkPlayerInfo parent) {
        this.parent = parent;
    }

    public ResourceLocation getLocationCape(GameProfile gameProfile, ResourceLocation locationCape) {
        ResourceLocation cape = locationCape;

        if (cape == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        ((IMixinNetworkPlayerInfo) parent).setLocationCape(cape);
        return cape;
    }

    public ResourceLocation getLocationSkin(GameProfile gameProfile, ResourceLocation locationSkin) {
        ResourceLocation skin = locationSkin;

        if (skin == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        ResourceLocation normalizedSkin = normalizeSkin(skin, gameProfile);
        ((IMixinNetworkPlayerInfo) parent).setLocationSkin(normalizedSkin);
        return normalizedSkin;
    }

    private ResourceLocation normalizeSkin(ResourceLocation skin, GameProfile gameProfile) {
        return (skin != null ? skin : DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
    }
}
