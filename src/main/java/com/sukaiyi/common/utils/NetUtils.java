package com.sukaiyi.common.utils;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.IntPredicate;

/**
 * @author sukaiyi
 * @date 2020/02/17
 */
public class NetUtils {


    private NetUtils() {
    }

    public static int choosePort(int... prefers) {
        IntPredicate alreadyInUse = NetUtils::isPortAlreadyInUse;
        int port = Arrays.stream(prefers).filter(alreadyInUse.negate()).findFirst().orElse(-1);
        if (port == -1) {
            int testPort = 9000;
            while (isPortAlreadyInUse(testPort)) {
                testPort++;
            }
            return testPort;
        }
        return port;
    }

    public static boolean isPortAlreadyInUse(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
