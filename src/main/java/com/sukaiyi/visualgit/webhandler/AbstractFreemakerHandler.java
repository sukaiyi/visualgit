package com.sukaiyi.visualgit.webhandler;

import com.sukaiyi.visualgit.common.StringBuilderWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class AbstractFreemakerHandler implements WebRequestHandler {

    protected abstract String getTemplate(HttpServerExchange exchange);

    protected abstract Object getDataModel(HttpServerExchange exchange);

    @Override
    public void handle(HttpServerExchange exchange) {
        Sender sender = exchange.getResponseSender();
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            Template temp = cfg.getTemplate(getTemplate(exchange));
            StringBuilder sb = new StringBuilder();
            temp.process(getDataModel(exchange), new StringBuilderWriter(sb));
            sender.send(sb.toString());
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
        }
    }

}
