package com.sukaiyi.visualgit.webhandler;

import io.undertow.server.HttpServerExchange;

import java.util.HashMap;

public class TestHandler extends AbstractFreemakerHandler {
    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "index.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        return new HashMap<String, String>() {{
            put("var", exchange.getRequestURI());
        }};
    }
}
