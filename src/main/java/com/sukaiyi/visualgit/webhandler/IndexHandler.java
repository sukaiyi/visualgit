package com.sukaiyi.visualgit.webhandler;

import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexHandler extends AbstractFreemakerHandler {
    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "index.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        return null;
    }
}
