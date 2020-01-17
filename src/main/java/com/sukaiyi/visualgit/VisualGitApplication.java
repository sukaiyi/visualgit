package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.webhandler.StaticHandler;
import com.sukaiyi.visualgit.webhandler.IndexHandler;
import io.undertow.Undertow;

public class VisualGitApplication {

    public static void main(String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(
                        new PathBasedWebRequestDispatcher()
                                .match("/index", new IndexHandler())
                                .match("/static/.*", new StaticHandler())
                ).build();
        server.start();
    }
}