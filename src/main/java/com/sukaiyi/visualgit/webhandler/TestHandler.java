package com.sukaiyi.visualgit.webhandler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class TestHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write("response.getBytes()".getBytes());

            responseBody.close();
        }
    }
}
