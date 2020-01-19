package com.sukaiyi.visualgit.common;

import com.sukaiyi.visualgit.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class GitLogFetcher {

    private static String GIT_LOG_COMMAND = null;

    static {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = GitLogFetcher.class.getClassLoader().getResourceAsStream("app.properties");
            prop.load(is);
            GIT_LOG_COMMAND = prop.getProperty("git.log.command");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(is);
        }
    }

    public static String fetch(String repoPath) {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(GIT_LOG_COMMAND, null, new File(repoPath));
            is = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
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