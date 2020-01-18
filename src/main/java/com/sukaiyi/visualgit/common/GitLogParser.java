package com.sukaiyi.visualgit.common;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class GitLogParser {

    private static Gson GSON = new Gson();

    public static List<GitCommitInfo> parse(String log) {
        List<GitCommitInfo> commitInfos = new ArrayList<>();
        Map<String, String> parts = Arrays.stream(log.split("\\{\\{start}}"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .map(e -> e.split("\\{\\{end}}"))
                .filter(e -> e.length >= 1)
                .collect(Collectors.toMap(t -> t[0].trim(), t -> t.length >= 2 ? t[1] : "", (n, o) -> o, LinkedHashMap::new));

        for (Map.Entry<String, String> entry : parts.entrySet()) {
            String json = entry.getKey();
            // 先转义 title 和 content 中的特殊字符
            json = handleQuota(json);
            GitCommitInfo commitInfo = GSON.fromJson(json, GitCommitInfo.class);
            String detail = entry.getValue();
            String[] detailParts = detail.split("\n");
            for (String detailPart : detailParts) {
                String detailPartTrim = detailPart.trim();
                if (detailPartTrim.matches("\\d+ files changed, \\d+ insertions\\(\\+\\), \\d+ deletions\\(-\\)") ||
                        detailPartTrim.matches("\\d+ files changed, \\d+ insertions\\(\\+\\)")) {
                    String[] data = detailPartTrim.split(" ");
                    commitInfo.setFileCount(Integer.parseInt(data[0]));
                    commitInfo.setInsertions(Integer.parseInt(data[3]));
                    commitInfo.setDeletions(data.length > 6 ? Integer.parseInt(data[5]) : 0);
                }
            }
            commitInfo.setTimestamp(Optional.ofNullable(commitInfo.getTimestamp()).map(e -> 1000 * e).orElse(0L));
            commitInfos.add(commitInfo);
        }
        return commitInfos;
    }

    private static String handleQuota(String json) {
        json = json.trim();
        int indexOfTitle = json.indexOf("\"title\":\"");
        String remain = json.substring(indexOfTitle + 9);
        int indexOfContent = remain.indexOf("\",\"content\":\"");
        String title = remain.substring(0, indexOfContent)
                .replace('"', '\'')
                .replace("\\", "\\\\");
        String content = remain.substring(indexOfContent + 13, remain.length() - 2)
                .replace('"', '\'')
                .replace("\\", "\\\\");
        return json.substring(0, indexOfTitle) + "\"title\":\"" +
                title + "\",\"content\":\"" +
                content + "\"}";
    }

}
