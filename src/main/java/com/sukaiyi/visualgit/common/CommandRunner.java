package com.sukaiyi.visualgit.common;

import com.sukaiyi.visualgit.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author sukaiyi
 * @date 2020/01/21
 */
@Slf4j
public class CommandRunner {
    private CommandRunner(){

    }

    public static String exec(String command, File workSpace){
        InputStream is = null;
        BufferedReader reader = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(command, null, workSpace);
            is = process.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
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
