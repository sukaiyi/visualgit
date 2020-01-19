package com.sukaiyi.visualgit.charts.dotchart;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.utils.IoUtils;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class FileChangeDetailHandler extends AbstractFreemakerHandler {

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "fileChangeDetail.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        Map<String, Deque<String>> params = Optional.of(exchange)
                .map(HttpServerExchange::getQueryParameters)
                .orElse(Collections.emptyMap());
        String revision = Optional.of(params)
                .map(map -> map.get("revision"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse(null);
        String file = Optional.of(params)
                .map(map -> map.get("file"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse(null);

        InputStream is = null;
        BufferedReader reader = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec("git show " + revision + " -- \"" + file + "\"", null, new File(VisualGitApplication.getInstance().getWorkRepo()));
            is = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            Map<String, String> data = new HashMap<>();
            data.put("file", file);
            data.put("revision", revision);
            data.put("data", sb.toString().replace("<", "&lt;").replace(">", "&gt;"));
            return data;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtils.close(is);
            IoUtils.close(reader);
        }
        return null;
    }
}
