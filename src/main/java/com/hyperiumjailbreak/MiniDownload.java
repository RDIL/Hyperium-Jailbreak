package com.hyperiumjailbreak;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiniDownload {
    private static final Pattern FILENAME = Pattern.compile("filename=(?<name>\\S+)");
    private HttpURLConnection httpConn;
    private InputStream inputStream;
    private String fileName;

    public void download(final String fileURL) throws IOException {
        final URL url = new URL(fileURL);
        this.httpConn = (HttpURLConnection) url.openConnection();
        this.httpConn.setRequestMethod("GET");
        this.httpConn.setUseCaches(true);
        this.httpConn.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium");
        this.httpConn.setReadTimeout(15000);
        this.httpConn.setConnectTimeout(15000);
        this.httpConn.setDoOutput(true);
        int responseCode = this.httpConn.getResponseCode();
        if (responseCode == 200) {
            String disposition = this.httpConn.getHeaderField("Content-Disposition");
            String contentType = this.httpConn.getContentType();
            int contentLength = this.httpConn.getContentLength();
            if (disposition != null) {
                Matcher m = FILENAME.matcher(disposition);
                if (m.find()) {
                    this.fileName = m.group("name");
                }
            }

            if (this.fileName == null) {
                this.fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
            }

            this.fileName = URLDecoder.decode(this.fileName, "UTF-8");
            this.inputStream = this.httpConn.getInputStream();
        } else {
            throw new IOException("No file to download. Server replied HTTP code: " + responseCode);
        }
    }

    public void disconnect() throws IOException {
        this.inputStream.close();
        this.httpConn.disconnect();
    }

    public String getFileName() {
        return this.fileName;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }
}
