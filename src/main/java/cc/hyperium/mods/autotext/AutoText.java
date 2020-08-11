package cc.hyperium.mods.autotext;

import cc.hyperium.Hyperium;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.autotext.commands.AutoTextCommand;
import cc.hyperium.mods.autotext.config.AutoTextConfig;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class AutoText extends AbstractMod {
    @Override
    public AbstractMod init() {
        Hyperium.CONFIG.register(AutoTextConfig.INSTANCE);
        for (Map.Entry<String, String> entry : AutoTextConfig.INSTANCE.getKeybinds().entrySet()) {
            Hyperium.INSTANCE.getHandlers().getKeybindHandler().registerKeyBinding(new HyperiumBind(entry.getValue(), Integer.parseInt(entry.getKey())) {
                @Override
                public void onPress() {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(entry.getValue());
                }
            });
        }
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new AutoTextCommand());
        return this;
    }
}
