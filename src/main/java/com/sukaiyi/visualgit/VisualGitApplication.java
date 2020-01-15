package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.webhandler.TestHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;

public class VisualGitApplication {

    public static void main(String[] args) throws IOException {
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(62435), 100);
        httpserver.createContext("/RestSample", new TestHandler());
        httpserver.setExecutor(null);
        httpserver.start();
    }
}
