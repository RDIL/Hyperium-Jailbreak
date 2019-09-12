package cc.hyperium.network;

import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.packet.PacketType;
import cc.hyperium.netty.packet.packets.clientbound.LoginReplyPacket;
import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import net.minecraft.client.Minecraft;

public class LoginReplyHandler implements PacketHandler<LoginReplyPacket> {
    @Override
    public void handle(LoginReplyPacket loginReplyPacket) {
        if (Minecraft.getMinecraft().getCurrentServerData() != null) {
            NettyClient client = NettyClient.getClient();
            if (client != null) {
                client.write(UpdateLocationPacket.build("Other"));
            }
        }
    }

    @Override
    public PacketType accepting() {
        return PacketType.LOGIN_REPLY;
    }
}
