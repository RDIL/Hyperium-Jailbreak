package cc.hyperium.netty.utils;

import cc.hyperium.netty.UniversalNetty;
import cc.hyperium.utils.JsonHolder;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Utils {
    public static String toString(Throwable e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        e.printStackTrace(stream);
        String ret = baos.toString();

        try {
            baos.flush();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return ret;
   }

   public static JsonHolder get(String url) {
       try {
           URL u = new URL(url);
           HttpURLConnection connection = (HttpURLConnection)u.openConnection();
           connection.setRequestMethod("GET");
           connection.setUseCaches(true);
           connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Sk1er.club Website)");
           connection.setReadTimeout(5000);
           connection.setConnectTimeout(5000);
           connection.setDoOutput(true);
           InputStream is = connection.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           String line = reader.readLine();
           StringBuilder builder = new StringBuilder(line != null ? line : "");
           if (line != null) {
               while(line != null) {
                   line = reader.readLine();
                   if (line != null) {
                       builder.append(line);
                   }
               }
               return new JsonHolder(builder.toString());
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return (new JsonHolder()).put("success", false).put("cause", "API_DOWN1");
    }

    public static UUID dashMeUp(String uuidWithoutDashes) {
       return UUID.fromString(uuidWithoutDashes.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
