package com.sukaiyi.visualgit.webhandler;

import io.undertow.server.HttpServerExchange;

/**
 * @author sukaiyi
 * @date 2020/01/17
 */
public class _404Handler implements WebRequestHandler {
    @Override
    public void handle(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
    }
}
