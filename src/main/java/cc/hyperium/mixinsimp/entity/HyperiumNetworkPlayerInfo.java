package cc.hyperium.mixinsimp.entity;

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

    public ResourceLocation getLocationCape(ResourceLocation locationCape) {
        if (locationCape == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        ((IMixinNetworkPlayerInfo) parent).setLocationCape(locationCape);
        return locationCape;
    }

    public ResourceLocation getLocationSkin(GameProfile gameProfile, ResourceLocation locationSkin) {
        if (locationSkin == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        ResourceLocation normalizedSkin = normalizeSkin(locationSkin, gameProfile);
        ((IMixinNetworkPlayerInfo) parent).setLocationSkin(normalizedSkin);
        return normalizedSkin;
    }

    private ResourceLocation normalizeSkin(ResourceLocation skin, GameProfile gameProfile) {
        return (skin != null ? skin : DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
    }
}
