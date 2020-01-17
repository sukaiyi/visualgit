package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.webhandler.WebRequestHandler;
import com.sukaiyi.visualgit.webhandler._404Handler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.HashMap;
import java.util.Map;

public class PathBasedWebRequestDispatcher implements HttpHandler {

    private Map<String, WebRequestHandler> route = new HashMap<>();

    private static final WebRequestHandler _404_HANDLER = new _404Handler();

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        String path = exchange.getRequestPath();
        WebRequestHandler handler = route.keySet().stream()
                .filter(path::matches)
                .findFirst()
                .map(route::get)
                .orElse(_404_HANDLER);
        handler.handle(exchange);
    }

    public PathBasedWebRequestDispatcher match(String path, WebRequestHandler handler) {
        route.put(path, handler);
        return this;
    }
}
