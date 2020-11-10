package com.hyperiumjailbreak;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import cc.hyperium.Hyperium;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BackendHandler {
    public static HttpClient httpclient = HttpClients.createDefault();

    public BackendHandler() {}

    public void apiRequest(String url) {
        try {
            httpclient.execute(generate("http://backend.rdil.rocks/" + url));
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
                        generate("http://backend.rdil.rocks/checkUpdate")
                    ).getEntity(), "UTF-8"
            ), Hyperium.version);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getRawText(String url) {
        String s = "";
        try {
            s = IOUtils.toString(httpclient.execute(new HttpGet(url)).getEntity().getContent(), StandardCharsets.UTF_8);
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
}
