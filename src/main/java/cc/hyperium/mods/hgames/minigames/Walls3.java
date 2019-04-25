package cc.hyperium.mods.hgames.minigames;

import cc.hyperium.mods.chromahud.displayitems.hyperium.MinigameDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

public class Walls3 extends Minigame {
    private int timesCalled = 0;
    private int duplicateCounter = 0;

    @Override
    public void draw(MinigameDisplay display, int starX, double startY, boolean config) {
        display.setHeight(10);
        display.setWidth(10);
        timesCalled = 0;
    }

    @Override
    public void onTick() {}

    @Override
    public void onChat(IChatComponent message) {
        String line = message.getUnformattedText();
        if (line.startsWith("You are currently playing on ")) {
            duplicateCounter = 0;
        } else if (line.equalsIgnoreCase("This command is not available on this server!")) {
            duplicateCounter = 0;
        }
    }

    @Override
    public void onWorldChange() {
        if (duplicateCounter == 0) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/wtfmap");
        }
        duplicateCounter++;
    }
}
