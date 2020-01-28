package com.sukaiyi.visualgit.charts.divisionrelachart;

import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class DivisionRelaChart implements Chart {

    @Override
    public Graph data(List<GitCommitInfo> commitInfos) {
        Map<String, Set<String>> fileDeveloperMap = new HashMap<>();
        Map<String, Set<String>> developerFileMap = new HashMap<>();

        for (GitCommitInfo commitInfo : commitInfos) {
            String committer = Optional.of(commitInfo)
                    .map(GitCommitInfo::getCommitter)
                    .map(GitCommitInfo.GitCommitDeveloperInfo::getName)
                    .orElse(null);
            List<String> files = Optional.of(commitInfo)
                    .map(GitCommitInfo::getStats)
                    .map(stats -> stats.stream().map(GitCommitInfo.GitCommitFileInfo::getFile).collect(Collectors.toList()))
                    .orElse(null);
            if (committer == null || files == null) {
                continue;
            }
            Set<String> thisFileSet = developerFileMap.computeIfAbsent(committer, key -> new HashSet<>());
            for (String file : files) {
                fileDeveloperMap.computeIfAbsent(file, key -> new HashSet<>()).add(committer);
                thisFileSet.add(file);
            }
        }
        List<Dot> dots = developerFileMap.entrySet()
                .stream()
                .map(entry -> new Dot(entry.getKey(), entry.getKey(), 1.0, entry.getValue().size(), entry.getKey()))
                .collect(Collectors.toList());
        int max = dots.stream().mapToInt(Dot::getValue).max().orElse(1);
        dots.forEach(dot -> {
            Integer old = dot.getValue();
            dot.setSymbolSize(old * 40.0 / max + 10);
        });
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < dots.size(); i++) {
            for (int j = i + 1; j < dots.size(); j++) {
                Dot source = dots.get(i);
                Dot target = dots.get(j);
                int value = 0;
                for (Map.Entry<String, Set<String>> entry : fileDeveloperMap.entrySet()) {
                    Set<String> developers = entry.getValue();
                    if (developers.contains(source.getId()) && developers.contains(target.getId())) {
                        value++;
                    }
                }
                if (value > 0) {
                    edges.add(new Edge(source.getId(), target.getId(), value));
                }
            }
        }
        return new Graph(dots, edges);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dot {
        private String id;
        private String name;
        private Double symbolSize;
        private Integer value;
        private String category;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Edge {
        private String source;
        private String target;
        private Integer value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Graph {
        private List<Dot> dots;
        private List<Edge> edges;
    }

}
