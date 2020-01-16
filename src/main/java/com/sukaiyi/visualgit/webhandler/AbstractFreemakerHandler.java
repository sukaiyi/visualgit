package com.sukaiyi.visualgit.webhandler;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;

import java.io.IOException;
import java.io.Writer;

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
            temp.process(getDataModel(exchange), new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) {
                    sb.append(cbuf);
                }

                @Override
                public void flush() {

                }

                @Override
                public void close() {

                }
            });
            sender.send(sb.toString());
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

}
