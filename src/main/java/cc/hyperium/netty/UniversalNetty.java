package cc.hyperium.netty;

import cc.hyperium.netty.packet.ClientPacketManager;

public class UniversalNetty {
    public static final int PORT = 9001;
    private static UniversalNetty instance;
    public static final String HOST = "server.hyperium.cc";
    private ClientPacketManager packetManager;

    public UniversalNetty() {
        super();
        UniversalNetty.instance = this;
        this.packetManager = new ClientPacketManager();
    }

    public static UniversalNetty getInstance() {
        return UniversalNetty.instance;
    }

    public ClientPacketManager getPacketManager() {
        return this.packetManager;
    }
}
