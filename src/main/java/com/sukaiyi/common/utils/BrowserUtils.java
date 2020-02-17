package com.sukaiyi.common.utils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author sukaiyi
 * @date 2020/02/16
 */
public class BrowserUtils {

    private BrowserUtils() {

    }

    public static void openQuiet(String url) {
        try {
            open(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void open(String url) throws Exception {
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            macHandler(url);
        } else if (osName.startsWith("Windows")) {
            windowsHandler(url);
        } else {
            linuxHandler(url);
        }
    }

    @SuppressWarnings("all")
    private static void macHandler(String url) throws ReflectiveOperationException {
        Class fileMgr = Class.forName("com.apple.eio.FileManager");
        Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
        openURL.invoke(null, new Object[]{url});
    }

    private static void windowsHandler(String url) throws IOException {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }

    @SuppressWarnings("all")
    private static void linuxHandler(String url) throws Exception {
        String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
        String browser = null;
        for (int count = 0; count < browsers.length && browser == null; count++) {
            if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                browser = browsers[count];
            }
        }
        if (browser == null) {
            throw new Exception("Could not find web browser");
        } else {
            Runtime.getRuntime().exec(new String[]{browser, url});
        }
    }
}
