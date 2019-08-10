package rocks.rdil.jailbreak.chat;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.handlers.handlers.HypixelDetector;
import net.minecraft.client.Minecraft;

public class CommonChatResponder {
    private String listenFor;
    private String say;
    private boolean onlyOnHypixel;
    public CommonChatResponder(String listenFor, String say, boolean onlyOnHypixel) {
        this.listenFor = listenFor;
        this.say = say;
        this.onlyOnHypixel = onlyOnHypixel;
    }

    @InvokeEvent
    public void onChat(ChatEvent e) {
        if (e.getChat().getUnformattedText().contains(this.listenFor) && !this.onlyOnHypixel || HypixelDetector.getInstance().isHypixel()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/achat" + this.say);
        }
    }
}
