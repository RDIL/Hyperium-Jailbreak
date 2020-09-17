package cc.hyperium.internal.addons.misc;

import cc.hyperium.internal.addons.AddonManifest;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AddonManifestParser {
    private JsonObject json = null;
    private final Gson gson = new Gson();

    public AddonManifestParser(JarFile jar) throws IOException {
        InputStream jarInputStream = null;
        try {
            ZipEntry entry = jar.getEntry("addon.json");

            File jsonFile = File.createTempFile("json", "tmp");
            jsonFile.deleteOnExit();

            jarInputStream = jar.getInputStream(entry);
            copyInputStream(jarInputStream, new FileOutputStream(jsonFile));

            String contents = Files.toString(jsonFile, Charset.defaultCharset());

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(contents).getAsJsonObject();

            if (!json.has("version") && !json.has("name") && !json.has("mainClass")) {
                throw new IOException("Invalid addon manifest (Needs name, version and mainClass)");
            }

            this.json = json;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jarInputStream != null) {
                jarInputStream.close();
            }
            jar.close();
        }
    }

    public AddonManifestParser(String contents) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(contents).getAsJsonObject();
        if (!json.has("version") && !json.has("name") && !json.has("mainClass")) {
            try {
                throw new IOException("Invalid addon manifest (Needs name, version and mainClass)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.json = json;
    }

    public AddonManifest getAddonManifest() {
        return gson.fromJson(json, AddonManifest.class);
    }

    @Override
    public String toString() {
        return json.toString();
    }

    private int copyInputStream(InputStream input, OutputStream output) {
        long count = copyLarge(input, output, new byte[1024 * 4]);
        if (count > Integer.MAX_VALUE) {
            return -1;
        } else {
            return (int) count;
        }
    }

    private long copyLarge(InputStream input, OutputStream outputStream, byte[] buffer) {
        long count = 0;
        int n;

        try {
            while ((n = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
                count += n;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
}