package com.hyperiumjailbreak;

import cc.hyperium.installer.utils.JsonHolder;
import cc.hyperium.installer.utils.http.client.HttpClient;
import cc.hyperium.installer.utils.http.client.methods.HttpGet;
import cc.hyperium.installer.utils.http.impl.client.HttpClients;
import org.apache.commons.io.IOUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class InstallerUtils {
    public static int find(byte[] buf, byte[] pattern) {
        return find(buf, 0, pattern);
    }

    public static int find(byte[] buf, int index, byte[] pattern) {
        for(int i = index; i < buf.length - pattern.length; ++i) {
            boolean found = true;

            for(int pos = 0; pos < pattern.length; ++pos) {
                if (pattern[pos] != buf[i + pos]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return i;
            }
        }

        return -1;
    }

    public static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        while(true) {
            int len = is.read(buf);
            if (len < 0) {
                is.close();
                return baos.toByteArray();
            }

            baos.write(buf, 0, len);
        }
    }

    public static String[] tokenize(String str, String delim) {
        List<String> list = new ArrayList<>();
        final StringTokenizer tok = new StringTokenizer(str, delim);

        while(tok.hasMoreTokens()) {
            String token = tok.nextToken();
            list.add(token);
        }

        return list.toArray(new String[0]);
    }

    public static String getExceptionStackTrace(Throwable e) {
        StringWriter swr = new StringWriter();
        PrintWriter pwr = new PrintWriter(swr);
        e.printStackTrace(pwr);
        pwr.close();

        try {
            swr.close();
        } catch (IOException ignored) {
        }

        return swr.getBuffer().toString();
    }

    public static void copyFile(File fileSrc, File fileDest) throws IOException {
        if (!fileSrc.getCanonicalPath().equals(fileDest.getCanonicalPath())) {
            FileInputStream fin = new FileInputStream(fileSrc);
            FileOutputStream fout = new FileOutputStream(fileDest);
            copyAll(fin, fout);
            fout.flush();
            fin.close();
            fout.close();
        }
    }

    public static void copyAll(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];

        while(true) {
            int len = is.read(buf);
            if (len < 0) {
                return;
            }

            os.write(buf, 0, len);
        }
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "OptiFine", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static String readFile(File file) throws IOException {
        return readFile(file, "ASCII");
    }

    public static String readFile(File file, String encoding) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        return readText(fin, encoding);
    }

    public static String readText(InputStream in, String encoding) throws IOException {
        InputStreamReader inr = new InputStreamReader(in, encoding);
        BufferedReader br = new BufferedReader(inr);
        StringBuilder sb = new StringBuilder();

        while(true) {
            String line = br.readLine();
            if (line == null) {
                br.close();
                inr.close();
                in.close();
                return sb.toString();
            }

            sb.append(line);
            sb.append("\n");
        }
    }

    public static String[] readLines(InputStream in, String encoding) throws IOException {
        String str = readText(in, encoding);
        return tokenize(str, "\n\r");
    }

    public static void centerWindow(Component c, Component par) {
        if (c != null) {
            Rectangle rect = c.getBounds();
            Rectangle parRect;
            if (par != null && par.isVisible()) {
                parRect = par.getBounds();
            } else {
                Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
                parRect = new Rectangle(0, 0, scrDim.width, scrDim.height);
            }

            int newX = parRect.x + (parRect.width - rect.width) / 2;
            int newY = parRect.y + (parRect.height - rect.height) / 2;
            if (newX < 0) {
                newX = 0;
            }

            if (newY < 0) {
                newY = 0;
            }

            c.setBounds(newX, newY, rect.width, rect.height);
        }
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else {
            return o1 != null && o1.equals(o2);
        }
    }

    private static final HttpClient client = HttpClients.custom().setUserAgent("Mozilla/5.0 (Hyperium Installer)").build();

    public static File getMinecraftDir() {
        switch(getOS()) {
            case Windows:
                return new File(System.getenv("APPDATA"), ".minecraft");
            case MacOS:
                return new File(System.getProperty("user.home") + "/Library/Application Support", "minecraft");
            default:
                return new File(System.getProperty("user.home"), ".minecraft");
        }
    }

    public static com.hyperiumjailbreak.InstallerUtils.OSType getOS() {
        final String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (!OS.contains("mac") && !OS.contains("darwin")) {
            if (OS.contains("win")) {
                return com.hyperiumjailbreak.InstallerUtils.OSType.Windows;
            } else if (OS.contains("nux")) {
                return com.hyperiumjailbreak.InstallerUtils.OSType.Linux;
            } else {
                return com.hyperiumjailbreak.InstallerUtils.OSType.Other;
            }
        }
        return com.hyperiumjailbreak.InstallerUtils.OSType.MacOS;
    }

    public static JsonHolder get(String url) {
        try {
            return new JsonHolder(getRaw(url));
        } catch (Exception var2) {
            var2.printStackTrace();
            return new JsonHolder();
        }
    }

    public static String getRaw(String url) throws IOException {
        return IOUtils.toString(client.execute(new HttpGet(url)).getEntity().getContent(), Charset.defaultCharset());
    }

    public static Class<?> loadClass(URL url, String c) throws ClassNotFoundException {
        return (new URLClassLoader(new URL[]{url})).loadClass(c);
    }

    public static void download(final String downloadURL, final File destinationFolder) throws IOException {
        final MiniDownload miniDownload = new MiniDownload();
        miniDownload.download(downloadURL);
        final File downloadedFileReal = new File(destinationFolder, miniDownload.getFileName());
        InputStream inputStream = miniDownload.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(downloadedFileReal);
        final byte[] buffer = new byte[4096];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        miniDownload.disconnect();
    }

    public enum OSType {
        Windows,
        MacOS,
        Linux,
        Other
    }
}
