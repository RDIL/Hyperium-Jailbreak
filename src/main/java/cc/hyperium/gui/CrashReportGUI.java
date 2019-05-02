package cc.hyperium.gui;

import cc.hyperium.installer.InstallerMain;
import cc.hyperium.internal.addons.AddonBootstrap;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonParser;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Bootstrap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import jb.Metadata;

public class CrashReportGUI extends JDialog {
    private CrashReport report;
    private int handle = 0;

    CrashReportGUI(CrashReport report) {
        super();
        this.report = report;

        setModal(true);
        setTitle("Game Crash");
        setSize(200, 300);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);
        initComponents();
        setAlwaysOnTop(true);

        this.setVisible(true);
        this.setLayout(null);
    }

    public static int handle(CrashReport report) {
        try {
            CrashReportGUI crg = new CrashReportGUI(report);
            return crg.handle;
        } catch (Exception ex) {
            ex.printStackTrace();
            Bootstrap.printToSYSOUT("## FAILED TO HANDLE CRASH WITH HYPERIUM CRASH REPORT GUI ##");
        }
        return 0;
    }

    public static String haste(String txt) {
        try {
            HttpClient hc = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://hastebin.com/documents");
            post.setEntity(new StringEntity(txt));
            HttpResponse response = hc.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            return "https://hastebin.com/" + new JsonParser().parse(result).getAsJsonObject().get("key").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setBackground(new Color(30, 30, 30));
        c.setLayout(null);
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, InstallerMain.class.getResourceAsStream("/assets/hyperium/fonts/Montserrat-Regular.ttf")).deriveFont(12.0F);
        } catch (FontFormatException | IOException e) {
            f = Font.getFont("Arial"); // backup
            e.printStackTrace();
        }
        JLabel error = null;
        try {
            error = new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/assets/minecraft/textures/material/error.png")).getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            error.setHorizontalAlignment(SwingConstants.CENTER);
            error.setBounds(50, 50, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel crash = new JLabel("HyperiumJailbreak");
        crash.setHorizontalAlignment(SwingConstants.CENTER);
        crash.setBounds(0, 2, 200, 40);
        crash.setBackground(new Color(30, 30, 30));
        crash.setForeground(Color.WHITE);
        crash.setFont(f);
        String t = report == null ? "Crash unknown" : report.getDescription();
        JLabel desc = new JLabel(t);
        desc.setHorizontalAlignment(SwingConstants.CENTER);
        desc.setBounds(0, 175, 200, 20);
        desc.setBackground(new Color(30, 30, 30));
        desc.setForeground(Color.WHITE);
        desc.setFont(resize(t, 190, f, desc));

        JButton exit = new JButton("EXIT");
        exit.setUI(new BasicButtonUI());
        exit.setBackground(new Color(255, 254, 254));
        exit.setForeground(new Color(30, 30, 30));
        exit.setFont(f);
        exit.setBorderPainted(false);
        exit.setFocusPainted(false);
        exit.setBounds(2, 208, 196, 20);
        exit.addActionListener(a -> {
            handle = 1;
            dispose();
        });
        c.add(crash);
        c.add(desc);
        c.add(exit);
        if (error != null) {
            c.add(error);
        }
    }

    private boolean copyReport() {
        try {
            AtomicReference<String> addons = new AtomicReference<>("");
            try {
                AddonBootstrap.INSTANCE.getAddonManifests().forEach(m -> addons.getAndUpdate(a -> a + m.getName() + ", "));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (addons.get().isEmpty()) addons.set("none");
            String hurl = null;
            if (report != null) {
                hurl = haste(report.getCompleteReport());
            }

            if (report != null && hurl == null) {
                return false;
            }
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection((hurl == null ? "Report unavailable" : hurl) + "\n\nHyperium: " + Metadata.getVersion() + "\nAddons: " + addons.get()), null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Font resize(String s, int width, Font f, JLabel l) {
        if (l.getFontMetrics(f).stringWidth(s) > width) {
            return resize(s, width, f.deriveFont(f.getSize() - 1f), l);
        }
        return f;
    }
}
