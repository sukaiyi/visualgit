package com.sukaiyi.visualgit.webhandler;

import com.sukaiyi.visualgit.utils.IoUtils;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class StaticHandler implements WebRequestHandler {

    @Override
    public void handle(HttpServerExchange exchange) {
        String path = exchange.getRequestPath();
        Sender sender = exchange.getResponseSender();
        String resourcePath = path.substring(1);
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                exchange.setStatusCode(404);
                return;
            }
            reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            sender.send(sb.toString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtils.close(is);
            IoUtils.close(reader);
        }
    }

}
