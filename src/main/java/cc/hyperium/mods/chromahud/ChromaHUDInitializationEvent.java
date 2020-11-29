package cc.hyperium.mods.chromahud;

import cc.hyperium.event.Event;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;

import java.util.ArrayList;
import java.util.List;

/**
 * An event called when ChromaHUD is initialized. This allows you to register custom {@link ChromaHUDParser ChromaHUDParsers},
 * which can in turn register new display elements.
 *
 * @see ChromaHUDParser
 */
public class ChromaHUDInitializationEvent extends Event {
    final List<ChromaHUDParser> additionalItems;

    /**
     * Creates a new instance of the event.
     */
    public ChromaHUDInitializationEvent() {
        additionalItems = new ArrayList<>();
    }

    /**
     * Registers a new parser for ChromaHUD.
     *
     * @param parser The parser.
     */
    public void registerParser(ChromaHUDParser parser) {
        additionalItems.add(parser);
    }
}
