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

/**
 * Parses an addon manifest.
 */
public class AddonManifestParser {
    private JsonObject json = null;
    private final Gson gson = new Gson();

    /**
     * Parses an addon manifest from a Jar file.
     *
     * @param jar The Jar file to analyze.
     * @throws IOException If something goes wrong.
     */
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

    /**
     * Parses an addon manifest from a string.
     *
     * @param contents The manifest to parse.
     */
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

    private void copyInputStream(InputStream input, OutputStream output) {
        copyLarge(input, output, new byte[1024 * 4]);
    }

    private void copyLarge(InputStream input, OutputStream outputStream, byte[] buffer) {
        int n;

        try {
            while ((n = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
