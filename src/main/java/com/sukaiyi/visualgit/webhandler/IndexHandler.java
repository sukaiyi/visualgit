package com.sukaiyi.visualgit.webhandler;

import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class IndexHandler extends AbstractFreemakerHandler {
    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "index.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
//        InputStream is = null;
//        BufferedReader reader = null;
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process process = rt.exec("git log --pretty=format:\"{{start}} %h %at %an %ce\" --stat", null, new File("C:\\Users\\HT-Dev\\Documents\\Projects\\jiaoxunjing\\jxj-cms"));
//            is = process.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                log.info(line);
//            }
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            IoUtils.close(is);
//            IoUtils.close(reader);
//        }
        return new HashMap<String, String>() {{
            put("var", "123");
        }};
    }
}
