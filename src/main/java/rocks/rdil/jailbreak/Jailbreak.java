package rocks.rdil.jailbreak;

import cc.hyperium.Hyperium;

public class Jailbreak {
    public Jailbreak() {}

    public void debug() {
        Hyperium.LOGGER.info("----------------");
        Hyperium.LOGGER.warn("This project is NOT RUN BY THE HYPERIUM TEAM");
        Hyperium.LOGGER.warn("Please report bugs by DMing rdil#0001 on Discord");
        Hyperium.LOGGER.warn("or by emailing me@rdil.rocks");
        Hyperium.LOGGER.info("----------------");
    }
}
