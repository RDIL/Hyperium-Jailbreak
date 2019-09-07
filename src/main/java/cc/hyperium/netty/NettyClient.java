package cc.hyperium.netty;

import cc.hyperium.netty.packet.packets.serverbound.UpdateLocationPacket;
import cc.hyperium.netty.packet.packets.serverbound.ServerCommandPacket;
import cc.hyperium.netty.utils.Utils;
import cc.hyperium.netty.packet.packets.serverbound.InitPacket;
import io.netty.channel.ChannelHandler;
import cc.hyperium.netty.network.ClientInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import java.util.concurrent.TimeUnit;
import cc.hyperium.netty.packet.IPacket;
import cc.hyperium.netty.packet.packets.universal.HeartBeatPacket;
import cc.hyperium.netty.handlers.MessagePacketHandler;
import cc.hyperium.netty.handlers.PartyUsersHandler;
import cc.hyperium.netty.handlers.RegisterGamesHandler;
import cc.hyperium.netty.handlers.CrossClientDataHandler;
import cc.hyperium.netty.handlers.LoginReplyHandler;
import cc.hyperium.netty.packet.PacketHandler;
import cc.hyperium.netty.handlers.SendHashHandler;
import cc.hyperium.utils.JsonHolder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.Channel;
import java.util.UUID;

public class NettyClient {
    public static final int PROTOCOL_VERSION = 1;
    private static NettyClient client;
    private UUID uuid;
    private Channel ch;
    private EventLoopGroup group;
    private INetty netty;
    private boolean connected;
    private boolean admin;
    private boolean authenticated;
    private JsonHolder games;
    private Thread currentThread;
    
    public NettyClient(final INetty netty) {
        super();
        this.connected = false;
        this.admin = false;
        this.games = new JsonHolder();
        this.currentThread = null;
        final UniversalNetty universalNetty = new UniversalNetty();
        universalNetty.getPacketManager().register((PacketHandler)new SendHashHandler());
        universalNetty.getPacketManager().register((PacketHandler)new LoginReplyHandler());
        universalNetty.getPacketManager().register((PacketHandler)new CrossClientDataHandler());
        universalNetty.getPacketManager().register((PacketHandler)new RegisterGamesHandler());
        universalNetty.getPacketManager().register((PacketHandler)new PartyUsersHandler());
        universalNetty.getPacketManager().register((PacketHandler)new MessagePacketHandler());
        this.netty = netty;
        NettyClient.client = this;
        netty.addVerboseLog("Initiated");
        try {
            this.reset();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Multithreading.schedule(() -> {
            if (this.connected) {
                this.write((IPacket)new HeartBeatPacket());
            }
        }, 10L, 10L, TimeUnit.SECONDS);
    }
    
    public static NettyClient getClient() {
        return NettyClient.client;
    }
    
    public boolean isAdmin() {
        return this.admin;
    }
    
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }
    
    public boolean isAuthenticated() {
        return this.authenticated;
    }
    
    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    private void setup() throws Exception {
        try {
            this.currentThread = Thread.currentThread();
            final SslContext sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            this.group = (EventLoopGroup)new NioEventLoopGroup();
            final Bootstrap b = new Bootstrap();
            ((Bootstrap)((Bootstrap)b.group(this.group)).channel((Class)NioSocketChannel.class)).handler((ChannelHandler)new ClientInitializer(sslCtx));
            this.ch = b.connect("server.hyperium.cc", 9001).sync().channel();
            this.connected = true;
            this.uuid = this.netty.getPlayerUUID();
            this.write((IPacket)InitPacket.build(this.uuid, 1));
        } catch (Exception e) {}
    }
    
    public INetty getNetty() {
        return this.netty;
    }
    
    public void close() {
        this.group.shutdownGracefully();
    }
    
    public void write(final IPacket packet) {
        final String s = packet.toPacketJson();
        if (!this.ch.isActive() || !this.ch.isOpen()) {
            this.reset();
        }
        if (this.ch.isOpen()) {
            this.ch.writeAndFlush((Object)(s + "\r\n"));
        }
    }
    
    public void handleInput(final String msg) {
        try {
            final IPacket iPacket = IPacket.fromPacketJson(msg);
            UniversalNetty.getInstance().getPacketManager().handle(iPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void dispatchCommand(final String command) {
        if (this.admin) {
            this.write((IPacket)ServerCommandPacket.build(command));
        }
    }
    
    public void updateLocation(final String location) {
        this.write((IPacket)UpdateLocationPacket.build(location));
    }
    
    public JsonHolder getGames() {
        return this.games;
    }
    
    public void setGames(final JsonHolder games) {
        this.games = games;
    }
    
    public void reset() {
        this.connected = false;
        try {
            this.group.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Multithreading.runAsync(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            if (!getClient().connected) {
                this.currentThread.interrupt();
                this.reset();
            }
            return;
        });
        try {
            this.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void lambda$reset$1() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!getClient().connected) {
            this.currentThread.interrupt();
            this.reset();
        }
    }
    
    private void lambda$new$0() {
        if (this.connected) {
            this.write((IPacket)new HeartBeatPacket());
        }
    }
}
