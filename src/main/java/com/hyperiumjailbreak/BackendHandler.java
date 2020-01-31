package com.hyperiumjailbreak;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import cc.hyperium.Hyperium;
import cc.hyperium.installer.utils.http.NameValuePair;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.entity.UrlEncodedFormEntity;
import cc.hyperium.installer.utils.http.client.methods.HttpGet;
import cc.hyperium.installer.utils.http.client.methods.HttpPost;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import cc.hyperium.installer.utils.http.util.EntityUtils;
import cc.hyperium.mods.sk1ercommon.Multithreading;

public class BackendHandler {
    public static HttpClient httpclient = HttpClients.createDefault();

    public BackendHandler() {}

    public void apiRequest(String url) {
        try {
            httpclient.execute(generate("https://backend.rdil.rocks/" + url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean apiUpdateCheck() {
        try {
            // Execute and get the response.
            return !Objects.equals(
                EntityUtils.toString(
                    httpclient.execute(
                        generate("https://backend.rdil.rocks/checkUpdate")
                    ).getEntity(), "UTF-8"
            ), Hyperium.version);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getRawText(String url) {
        String s = "";
        try {
            s = IOUtils.toString(httpclient.execute(new HttpGet(url)).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static HttpPost generate(String url) {
        HttpPost tmp = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(0);
        try {
            tmp.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static void scheduleTips() {
        List<Popup> tips = new ArrayList<Popup>();
        tips.add(new Popup("Visit our website for", "helpful info - hyperiumjailbreak.com"));
        tips.add(new Popup("Customize HJB", "Press ~ for settings."));

        for (Popup tip : tips) {
            Multithreading.runAsync(() -> {
                try {
                    Thread.sleep(new Random().nextInt(200) * 6000);
                    tip.startShowing();
                } catch (InterruptedException ignored) {}
            });
        }
    }
}
