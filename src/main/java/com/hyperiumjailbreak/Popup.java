package com.hyperiumjailbreak;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.gui.HyperiumGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;

public class Popup {
    private final String text;
    private final String subtext;
    private long framesLeft;
    private float percentComplete;
    private long systemTime;

    @InvokeEvent
    public void onRenderTick(RenderHUDEvent e) {
        if (this.render()) {
            EventBus.INSTANCE.unregister(this);
        }
    }

    public Popup(String text, String subtext) {
        this.framesLeft = 300;
        this.text = text;
        this.subtext = subtext;

        this.percentComplete = 0.0f;
        this.systemTime = Minecraft.getSystemTime();
    }
    
    public void startShowing() {
        this.systemTime = Minecraft.getSystemTime();

        EventBus.INSTANCE.register(this);
    }

    private boolean render() {
        if (framesLeft <= 0) {
            return true;
        }

        while (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
            this.framesLeft--;
            this.systemTime += (1000 / 60);
        }

        final long upperThreshold = 240;
        final long lowerThreshold = 60;

        this.percentComplete = HyperiumGui.clamp(HyperiumGui.easeOut(this.percentComplete, this.framesLeft < lowerThreshold ? 0.0f : this.framesLeft > upperThreshold ? 1.0f : framesLeft, 0.01f, 15f), 0.0f, 1.0f);

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int middle = sr.getScaledWidth() / 2;
        int halfWidth = 105;
        int currWidth = (int) (halfWidth * percentComplete);

        // Background
        Gui.drawRect(middle - currWidth, 50, middle + currWidth, 95, new Color(30, 30, 30).getRGB());

        if (this.percentComplete == 1.0F) {
            long length = upperThreshold - lowerThreshold;
            long current = framesLeft - lowerThreshold;
            float progress = 1.0F - HyperiumGui.clamp((float) current / (float) length, 0.0F, 1.0F);

            // Progress
            Gui.drawRect(middle - currWidth, 93, (int) (middle - currWidth + (210 * progress)), 95, new Color(149, 201, 144).getRGB());

            fr.drawString(text, sr.getScaledWidth() / 2 - fr.getStringWidth(text) / 2, 58, 0xFFFFFF);

            String s = subtext;
            fr.drawString(s, sr.getScaledWidth() / 2 - fr.getStringWidth(s) / 2, 70, new Color(170, 170, 170).getRGB());
        }

        return false;
    }
}