package com.sukaiyi.visualgit.webhandler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class TestHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            httpExchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = httpExchange.getResponseBody();
            InputStream is = getClass().getClassLoader().getResourceAsStream("index.html");
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bis.read(buffer)) != -1) {
                responseBody.write(buffer, 0, count);
            }
            responseBody.close();
        }
    }
}
