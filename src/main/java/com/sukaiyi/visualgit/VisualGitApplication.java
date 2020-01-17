package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.webhandler.TestHandler;
import io.undertow.Undertow;

public class VisualGitApplication {

    public static void main(String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(
                        new PathBasedWebRequestDispatcher()
                                .match("/test", new TestHandler())
                                .match("/test1", new TestHandler())
                                .match("/test2", new TestHandler())
                ).build();
        server.start();
    }
}