package com.hyperiumjailbreak;

import cc.hyperium.installer.InstallerMain;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Runtime {
    public static void main(String[] args) {
        InstallerMain.main("local");
    }

    public void scheduleTips() throws InterruptedException {
        List<Popup> tips = new ArrayList<Popup>();
        tips.add(new Popup("Visit our website for", "helpful info - hyperiumjailbreak.com"));

        for (Popup tip : tips) {
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(new Random().nextLong());
                    tip.startShowing();
                } catch (InterruptedException ignored) {}
            });
        }
    }
}
