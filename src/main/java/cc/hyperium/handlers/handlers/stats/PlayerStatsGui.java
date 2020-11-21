package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.stats.fields.ArcadeStats;
import cc.hyperium.handlers.handlers.stats.fields.GeneralStats;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStatsGui extends HyperiumGui {
    private List<AbstractHypixelStats> fields = new ArrayList<>();
    private Map<AbstractHypixelStats, BufferedImage> texturesImage = new ConcurrentHashMap<>();
    private static Map<AbstractHypixelStats, DynamicTexture> logos = new HashMap<>();

    public PlayerStatsGui() {
        fields.add(new GeneralStats());
        fields.add(new ArcadeStats());
        for (AbstractHypixelStats field : fields) {
            Multithreading.runAsync(() -> {
                if (!logos.containsKey(field))
                    try {
                        URL url = new URL("https://static.sk1er.club/hypixel_games/" + field.getImage() + ".png");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setUseCaches(true);
                        connection.addRequestProperty("User-Agent", "Mozilla/4.76");
                        connection.setReadTimeout(15000);
                        connection.setConnectTimeout(15000);
                        connection.setDoOutput(true);
                        InputStream is = connection.getInputStream();
                        BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(is));
                        texturesImage.put(field, img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!texturesImage.isEmpty()) {
            for (AbstractHypixelStats s : texturesImage.keySet()) {
                if (!logos.containsKey(s))
                    logos.put(s, new DynamicTexture(texturesImage.get(s)));
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        GeneralChatHandler.instance().sendMessage("This command is deprecated!");
        mc.displayGuiScreen(null);
    }

    @Override protected void pack(){}
}
