package com.sukaiyi.visualgit.common;

import com.sukaiyi.visualgit.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GitLogFetcher {

    private static String GIT_LOG_COMMAND = null;

    static {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = GitLogFetcher.class.getClassLoader().getResourceAsStream("app.properties");
            assert is != null;
            prop.load(is);
            GIT_LOG_COMMAND = prop.getProperty("git.log.command");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtils.close(is);
        }
    }

    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    public static String fetch(String repoPath) {
        String cachedValue = CACHE.get(repoPath);
        if (cachedValue != null) {
            return cachedValue;
        }

        InputStream is = null;
        BufferedReader reader = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(GIT_LOG_COMMAND, null, new File(repoPath));
            is = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            CACHE.put(repoPath, sb.toString());
            return sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtils.close(is);
            IoUtils.close(reader);
        }
        return null;
    }
}
