package cc.hyperium.network;

import cc.hyperium.config.Settings;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.packet.PacketType;
import cc.hyperium.netty.packet.packets.clientbound.LoginReplyPacket;
import cc.hyperium.netty.packet.packets.serverbound.ServerCrossDataPacket;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class LoginReplyHandler implements PacketHandler<LoginReplyPacket> {
    @Override
    public void handle(LoginReplyPacket loginReplyPacket) {
        ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
        if (currentServerData != null) {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(UpdateLocationPacket.build("Other"));
                if (Settings.SEND_SERVER)
                    client.write(ServerCrossDataPacket.build(new JsonHolder().put("internal", true).put("server_update", currentServerData.serverIP)));
            }
        }
    }

    @Override
    public PacketType accepting() {
        return PacketType.LOGIN_REPLY;
    }
}
