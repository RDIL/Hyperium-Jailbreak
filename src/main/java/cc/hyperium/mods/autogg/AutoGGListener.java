package cc.hyperium.mods.autogg;

import cc.hyperium.Hyperium;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.world.WorldChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

public class AutoGGListener {
    private final AutoGG mod;
    boolean invoked = false;

    public AutoGGListener(AutoGG mod) {
        this.mod = mod;
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        invoked = false;
    }

    @InvokeEvent
    public void onChat(final ChatEvent event) {
        if (this.mod.getConfig().ANTI_GG && invoked && event.getChat().getUnformattedText().toLowerCase().endsWith("gg") || event.getChat().getUnformattedText().endsWith("Good Game")) {
            event.setCancelled(true);
        }
        if (!this.mod.getConfig().isToggled() || this.mod.isRunning() || this.mod.getTriggers().isEmpty()) return;

        // Double parse to remove hypixel formatting codes
        String unformattedMessage = ChatColor.stripColor(event.getChat().getUnformattedText());

        if (this.mod.getTriggers().stream().anyMatch(unformattedMessage::contains) && unformattedMessage.startsWith(" ")) {
            this.mod.setRunning(true);
            invoked = true;
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Multithreading.POOL.submit(() -> {
                try {
                    Thread.sleep(Hyperium.INSTANCE.getModIntegration().getAutoGG().getConfig().getDelay() * 1000);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/achat " + (mod.getConfig().sayGoodGameInsteadOfGG ? (mod.getConfig().lowercase ? "good game" : "Good Game") : (mod.getConfig().lowercase ? "gg" : "GG")));
                    Thread.sleep(2000L);

                    Hyperium.INSTANCE.getModIntegration().getAutoGG().setRunning(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
