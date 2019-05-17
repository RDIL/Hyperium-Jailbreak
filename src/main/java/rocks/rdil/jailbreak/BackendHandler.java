package rocks.rdil.jailbreak;

import cc.hyperium.installer.utils.http.NameValuePair;
import cc.hyperium.installer.utils.http.util.EntityUtils;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.entity.UrlEncodedFormEntity;
import cc.hyperium.installer.utils.http.client.methods.HttpPost;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import jb.Metadata;

public class BackendHandler {
    private boolean update = false;

    public BackendHandler() {
        EventBus.INSTANCE.register(this);
    }

    public void apiRequest(String url) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/" + url);

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            httpclient.execute(httppost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiUpdateCheck() {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://backend.rdil.rocks/checkUpdate");

            List<NameValuePair> params = new ArrayList<NameValuePair>(0);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // Execute and get the response.
            String response = EntityUtils.toString(httpclient.execute(httppost).getEntity(), "UTF-8");
            if (!response.equals(Metadata.getVersion())) {
                update = true;
            } else {
                update = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getUpdate() {
        return update;
    }

    @InvokeEvent
    public void worldSwap(ServerJoinEvent event) {
        boolean update = this.bh.getUpdate();
        Runnable wait = new Runnable() {
            public void run(){
                while (Minecraft.getMinecraft().thePlayer == null) {}
                if (update) {
                    try {
                        Thread.sleep(250);
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "A new update for HyperiumJailbreak is available at " + EnumChatFormatting.BLUE + "https://www.rdil.rocks/update"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(wait);
    }
}
