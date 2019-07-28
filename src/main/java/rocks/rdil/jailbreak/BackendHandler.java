package rocks.rdil.jailbreak;

import cc.hyperium.installer.utils.http.NameValuePair;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.entity.UrlEncodedFormEntity;
import cc.hyperium.installer.utils.http.client.methods.HttpPost;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import cc.hyperium.installer.utils.http.util.EntityUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BackendHandler {
    private HttpClient httpclient = HttpClients.createDefault();

    public BackendHandler() {}

    public void apiRequest(String url) {
        try {
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/" + url);
            httppost.setHeader("User-Agent", "HyperiumJailbreak");
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

    public boolean apiUpdateCheck() {
        try {
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/checkUpdate");
            httppost.setHeader("User-Agent", "HyperiumJailbreak");
            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // Execute and get the response.
            String response = EntityUtils.toString(httpclient.execute(httppost).getEntity(), "UTF-8");
            return !response.equals(jb.Metadata.getVersion());
        } catch (Exception e) {
            return false;
        }
    }
}
