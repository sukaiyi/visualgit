package com.sukaiyi.visualgit.common;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.utils.MD5Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GitLogParser {

    private static Gson GSON = new Gson();

    private static final Map<String, List<GitCommitInfo>> CACHE = new ConcurrentHashMap<>();

    public static List<GitCommitInfo> parse(String log) {
        String cacheKey = MD5Utils.calc(log);
        List<GitCommitInfo> cachedValue = CACHE.get(cacheKey);
        if (cachedValue != null) {
            return cachedValue;
        }

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
            commitInfo.setFileCount(0L);
            commitInfo.setInsertions(0L);
            commitInfo.setDeletions(0L);
            String detail = entry.getValue();
            String[] detailParts = detail.split("\n");
            for (String detailPart : detailParts) {
                List<GitCommitInfo.GitCommitFileInfo> fileInfos = Optional.of(commitInfo).map(GitCommitInfo::getFileInfos).orElseGet(ArrayList::new);
                commitInfo.setFileInfos(fileInfos);
                Optional.of(detailPart)
                        .map(e -> Arrays.stream(e.split("\t")).filter(t -> !t.isEmpty()).collect(Collectors.toList()))
                        .filter(e -> e.size() == 3)
                        .map(fileInfoPart -> {
                            GitCommitInfo.GitCommitFileInfo fileInfo = new GitCommitInfo.GitCommitFileInfo();
                            fileInfo.setFile(fileInfoPart.get(2).trim());
                            Long insertions = Long.parseLong(Optional.of(fileInfoPart.get(0)).filter(e -> e.matches("\\d+")).orElse("0"));
                            Long deletions = Long.parseLong(Optional.of(fileInfoPart.get(1)).filter(e -> e.matches("\\d+")).orElse("0"));
                            fileInfo.setInsertions(insertions);
                            fileInfo.setDeletions(deletions);
                            commitInfo.setInsertions(commitInfo.getInsertions() + insertions);
                            commitInfo.setDeletions(commitInfo.getDeletions() + deletions);
                            return fileInfo;
                        })
                        .ifPresent(fileInfos::add);
            }
            commitInfo.setFileCount((long) commitInfo.getFileInfos().size());
            commitInfo.setTimestamp(Optional.ofNullable(commitInfo.getTimestamp()).map(e -> 1000 * e).orElse(0L));
            commitInfos.add(commitInfo);
        }
        CACHE.put(cacheKey, commitInfos);
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
