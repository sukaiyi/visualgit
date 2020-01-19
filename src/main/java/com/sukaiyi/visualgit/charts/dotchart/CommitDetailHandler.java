package com.sukaiyi.visualgit.charts.dotchart;

import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
public class CommitDetailHandler extends AbstractFreemakerHandler {

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "commitDetail.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String revision = Optional.of(exchange)
                .map(HttpServerExchange::getQueryParameters)
                .map(map -> map.get("revision"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse(null);

        return new HashMap<String, String>() {{
            put("revision", revision);
        }};
    }
}
