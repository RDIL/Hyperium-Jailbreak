/*
 *     Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.gui;

import cc.hyperium.config.provider.CosmeticOptionsProvider;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.client.TickEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleOverlay {
    private static ParticleOverlay overlay;
    private final List<Particle> particles = new ArrayList<>();
    private float h = 0.1F;
    private long last;

    private ParticleOverlay() {
        for (int i = 0; i < CosmeticOptionsProvider.MAX_PARTICLES; i++) {
            particles.add(new Particle());
        }
    }

    public static ParticleOverlay getOverlay() {
        if (overlay == null) {
            overlay = new ParticleOverlay();
            EventBus.INSTANCE.register(overlay);
        }
        return overlay;
    }

    public void render(int mouseX, int mouseY, int guiLeft, int guiTop, int guiRight, int guiBottom) {
        if (CosmeticOptionsProvider.PARTICLE_MODE.equals("OFF"))
            return;
        try {
            float step = (float) (0.01 * (CosmeticOptionsProvider.MAX_PARTICLES / 100));
            Mode m = getMode();
            if (m == Mode.OFF) return;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory)
                return;

            last = System.currentTimeMillis();
            for (Particle particle : particles) {
                float v1 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle.x;
                float v2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle.y;
                double mouseDis = Math.pow(v1 - mouseX, 2) + Math.pow(v2 - mouseY, 2);
                int i = ResolutionUtil.current().getScaledWidth() / 12;
                if (mouseDis < Math.pow(i, 2)) {
                    float xVec = Math.min(500F, (float) i / (mouseX - v1));
                    float yVec = Math.min(500F, (float) i / (mouseY - v2));
                    v1 -= xVec;
                    v2 -= yVec;
                    particle.regenerateVector();
                }

                particle.x = v1 / (float) ResolutionUtil.current().getScaledWidth_double();
                particle.y = v2 / (float) ResolutionUtil.current().getScaledHeight_double();
                for (Particle particle1 : particles) {
                    double v = particle.distSqTo(particle1);
                    if (v < 0.02) {
                        double lineStrength = Math.min(10000.0D, 1.0D / v) / 100D;
                        float x2 = ((float) ResolutionUtil.current().getScaledWidth_double()) * particle1.x;
                        float y2 = ((float) ResolutionUtil.current().getScaledHeight_double()) * particle1.y;
                        double alpha = 100 + ((0.02 / 155) * v);
                        if (((v1 >= guiLeft && v1 <= guiRight) || (x2 >= guiLeft && x2 <= guiRight))
                            && (v2 >= guiTop && v2 <= guiBottom) || (y2 >= guiTop && y2 <= guiBottom)) {
                            continue;
                        }

                        int color = Color.HSBtoRGB(h, 0.8F, 0.8F);
                        Color eee = new Color(color);
                        eee = new Color(eee.getRed(), eee.getBlue(), eee.getGreen(), 255);
                        switch (m) {
                            case PLAIN_1:
                                RenderUtils.drawLine(v1, v2, x2, y2, (float) lineStrength, new Color(255, 255, 255, (int) alpha).getRGB());
                                break;
                            case PLAIN_2:
                                RenderUtils.drawLine(v1, v2, x2, y2, 1F, new Color(255, 255, 255, (int) alpha).getRGB());
                                break;
                            case CHROMA_1:
                                RenderUtils.drawLine(v1, v2, x2, y2, (float) lineStrength, eee.getRGB());
                                break;
                            case CHROMA_2:
                                RenderUtils.drawLine(v1, v2, x2, y2, 1F, eee.getRGB());
                                break;
                        }
                    }
                }

                if (h >= 1.0F)
                    h = 0.0F;
                h += step;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void tick(TickEvent e) {
        if (CosmeticOptionsProvider.PARTICLE_MODE.equals("OFF"))
            return;
        if (System.currentTimeMillis() - last < 1000)
            for (Particle particle : particles) {
                particle.update();
            }
    }

    public Mode getMode() {
        try {
            return Mode.valueOf(CosmeticOptionsProvider.PARTICLE_MODE.replace(" ", "_"));
        } catch (Exception e) {
            return Mode.OFF;
        }
    }

    public enum Mode {
        OFF,
        PLAIN_1,
        PLAIN_2,
        CHROMA_1,
        CHROMA_2
    }

    static class Particle {
        private float x;
        private float y;
        private float xVec;
        private float yVec;

        private Particle() {
            x = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            y = (float) ThreadLocalRandom.current().nextDouble(0, 1);
            regenerateVector();
        }

        private void regenerateVector() {
            xVec = (float) ThreadLocalRandom.current().nextDouble(-.003, .003);
            yVec = (float) ThreadLocalRandom.current().nextDouble(-.003, .003);
        }

        void update() {
            x += xVec;
            if (x > 1.0)
                x = x - 1.0F;
            if (x < 0)
                x = 1.0F - x;
            y += yVec;
            if (y > 1.0)
                y = y - 1.0F;
            if (y < 0)
                y = 1.0F - y;
        }

        private double distSqTo(Particle other) {
            return Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
