package com.hyperiumjailbreak;

import cc.hyperium.Hyperium;
import com.hyperiumjailbreak.http.NameValuePair;
import com.hyperiumjailbreak.http.client.HttpClient;
import com.hyperiumjailbreak.http.client.entity.UrlEncodedFormEntity;
import com.hyperiumjailbreak.http.client.methods.HttpPost;
import com.hyperiumjailbreak.http.client.methods.HttpGet;
import com.hyperiumjailbreak.http.impl.client.HttpClients;
import com.hyperiumjailbreak.http.util.EntityUtils;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackendHandler {
    public static HttpClient httpclient = HttpClients.createDefault();

    public BackendHandler() {}

    public void apiRequest(String url) {
        try {
            httpclient.execute(generate("https://backend.rdil.rocks/" + url));
        } catch (IOException e) {
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
        } catch (IOException e) {
            return false;
        }
    }

    public static String getRawText(String url) {
        try {
            return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpPost generate(String url) {
        HttpPost tmp = new HttpPost(url);
        tmp.setHeader("User-agent", "HyperiumJailbreak");
        List<NameValuePair> params = new ArrayList<NameValuePair>(0);
        try {
            tmp.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tmp;
    }
}
