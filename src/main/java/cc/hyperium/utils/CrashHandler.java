package cc.hyperium.utils;

import java.io.OutputStream;
import java.io.PrintStream;

@SuppressWarnings("unused")
public class CrashHandler {
    // DON'T TOUCH THIS
    public static void handle(Exception ex) {
        StringBuilder err = new StringBuilder();
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                err.append((char) b);
            }
        });
        ex.printStackTrace(ps);
    }
}
