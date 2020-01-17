package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.webhandler.WebRequestHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PathBasedWebRequestDispatcher implements HttpHandler {

    private Map<String, WebRequestHandler> route = new HashMap<>();

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        String path = exchange.getRequestPath();
        Optional.of(path).map(route::get).ifPresent(handler -> handler.handle(exchange));
    }

    public PathBasedWebRequestDispatcher match(String path, WebRequestHandler handler) {
        route.put(path, handler);
        return this;
    }
}
