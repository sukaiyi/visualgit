package com.sukaiyi.visualgit.charts.dotchart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class DotChart implements Chart {

    private static Gson gson = new Gson();

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        // 按人分组
        // [
        //     [timestamp, insertions, fileCount, author, email, revision, title],
        //     [timestamp, insertions, fileCount, author, email, revision, title],
        //     [timestamp, insertions, fileCount, author, email, revision, title],...
        // ]
        Map<String, List<GitCommitInfo>> infoGroupByAuthor = commitInfos.stream()
                .collect(Collectors.groupingBy(GitCommitInfo::getAuthor))
                .entrySet().stream()
                .sorted((en1, en2) -> en2.getValue().size() - en1.getValue().size())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        Map<String, List<List<Object>>> data = new LinkedHashMap<>(infoGroupByAuthor.size());
        List<List<Object>> other = null;
        for (Map.Entry<String, List<GitCommitInfo>> entry : infoGroupByAuthor.entrySet()) {
            String author = entry.getKey();
            List<GitCommitInfo> infoThisAuthor = entry.getValue();
            List<List<Object>> dataThisAuthor = null;
            if (data.size() == 9) {
                other = dataThisAuthor = new ArrayList<>();
                data.put("other", dataThisAuthor);
            } else if (data.size() > 9) {
                dataThisAuthor = other;
            } else {
                dataThisAuthor = new ArrayList<>(infoThisAuthor.size());
                data.put(author, dataThisAuthor);
            }
            for (GitCommitInfo commit : infoThisAuthor) {
                String title = Optional.ofNullable(commit.getTitle()).orElse("");
                List<Object> dataThisCommit = Arrays.asList(
                        commit.getTimestamp(),
                        Optional.ofNullable(commit.getInsertions()).orElse(0L),
                        Optional.ofNullable(commit.getFileCount()).orElse(0L),
                        Optional.ofNullable(commit.getAuthor()).orElse(""),
                        Optional.ofNullable(commit.getEmail()).orElse(""),
                        Optional.ofNullable(commit.getRevision()).orElse(""),
                        title.length() > 30 ? title.substring(0, 30) + "..." : title
                );
                assert dataThisAuthor != null;
                dataThisAuthor.add(dataThisCommit);
            }
        }
        return new DotChartModel(
                "共 " + commitInfos.size() + " 个提交",
                new ArrayList<>(data.keySet()),
                data.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> gson.toJson(entry.getValue()), (o, n) -> n, LinkedHashMap::new))
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DotChartModel {
        private String title;
        private List<String> authors;
        private Map<String, String> data;
    }
}
