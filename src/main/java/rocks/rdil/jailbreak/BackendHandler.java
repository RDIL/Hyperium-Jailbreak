package rocks.rdil.jailbreak;

import cc.hyperium.installer.utils.http.NameValuePair;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.entity.UrlEncodedFormEntity;
import cc.hyperium.installer.utils.http.client.methods.HttpPost;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BackendHandler {
    public BackendHandler() {}

    public void apiRequest(String url) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/" + url);

            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // Execute and get the response.
            httpclient.execute(httppost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
