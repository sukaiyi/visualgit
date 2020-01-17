package com.sukaiyi.visualgit.webhandler;

import io.undertow.server.HttpServerExchange;

public interface WebRequestHandler {

    void handle(HttpServerExchange exchange);
}
