package com.sukaiyi.visualgit.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sukaiyi.common.utils.MD5Utils;

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
            // 先转义 subject 和 body 中的特殊字符
            json = handleQuota(json);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
            GitCommitInfo commitInfo = mapToGitCommitInfo(jsonObject);
            String detail = entry.getValue();
            String[] detailParts = detail.split("\n");
            for (String detailPart : detailParts) {
                List<GitCommitInfo.GitCommitFileInfo> fileInfos = Optional.of(commitInfo).map(GitCommitInfo::getStats).orElseGet(ArrayList::new);
                commitInfo.setStats(fileInfos);
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
            commitInfos.add(commitInfo);
        }
        CACHE.put(cacheKey, commitInfos);
        return commitInfos;
    }

    private static GitCommitInfo mapToGitCommitInfo(JsonObject jsonObject) {
        List<String> refs = Arrays.stream(jsonObject.get("refs").getAsString().split(" ")).filter(e -> !e.isEmpty()).collect(Collectors.toList());
        String hash = jsonObject.get("hash").getAsString();
        String tree = jsonObject.get("tree").getAsString();
        List<String> parents = Arrays.stream(jsonObject.get("parents").getAsString().split(" ")).filter(e -> !e.isEmpty()).collect(Collectors.toList());
        JsonObject authorObj = jsonObject.getAsJsonObject("author");
        JsonObject committerObj = jsonObject.getAsJsonObject("committer");
        String subject = jsonObject.get("subject").getAsString();
        String body = jsonObject.get("body").getAsString();
        String notes = jsonObject.get("notes").getAsString();

        GitCommitInfo info = new GitCommitInfo();
        info.setRefs(refs);
        info.setHash(hash);
        info.setHashAbbrev(hash.substring(0, 8));
        info.setTree(tree);
        info.setTreeAbbrev(tree.substring(0, 8));
        info.setParents(parents);
        info.setParentsAbbrev(parents.stream().map(e -> e.substring(0, 8)).collect(Collectors.toList()));
        info.setInsertions(0L);
        info.setDeletions(0L);
        info.setAuthor(new GitCommitInfo.GitCommitDeveloperInfo(
                authorObj.get("name").getAsString(),
                authorObj.get("email").getAsString(),
                authorObj.get("timestamp").getAsLong() * 1000
        ));
        info.setCommitter(new GitCommitInfo.GitCommitDeveloperInfo(
                committerObj.get("name").getAsString(),
                committerObj.get("email").getAsString(),
                committerObj.get("timestamp").getAsLong() * 1000
        ));
        info.setSubject(subject);
        info.setBody(body);
        info.setNotes(notes);
        return info;
    }

    private static String handleQuota(String json) {
        String subjectSp = "\"subject\":\"";
        String bodySp = "\",\"body\":\"";
        String noteSp = "\",\"notes\":\"";
        json = json.trim();
        int indexOfSubject = json.indexOf(subjectSp);
        String remainBodyNotes = json.substring(indexOfSubject + subjectSp.length());
        int indexOfBody = remainBodyNotes.indexOf(bodySp);
        int indexOfNote = remainBodyNotes.indexOf(noteSp);
        String subject = remainBodyNotes.substring(0, indexOfBody)
                .replace('"', '\'')
                .replace("\\", "\\\\");
        String body = remainBodyNotes.substring(indexOfBody + bodySp.length(), indexOfNote)
                .replace('"', '\'')
                .replace("\\", "\\\\");
        String notes = remainBodyNotes.substring(indexOfNote + noteSp.length(), remainBodyNotes.length() - 2)
                .replace('"', '\'')
                .replace("\\", "\\\\");
        return json.substring(0, indexOfSubject) + subjectSp +
                subject + bodySp +
                body + noteSp + notes + "\"}";
    }

}
