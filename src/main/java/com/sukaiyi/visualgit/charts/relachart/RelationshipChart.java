package com.sukaiyi.visualgit.charts.relachart;

import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sukaiyi
 * @date 2020/01/22
 */
public class RelationshipChart implements Chart {

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        List<Dot> dots = new ArrayList<>(commitInfos.size());
        List<Edge> edges = new ArrayList<>(commitInfos.size() - 1);
        for (int i = 0; i < commitInfos.size(); i++) {
            GitCommitInfo thisInfo = commitInfos.get(i);
            // 添加点
            Dot dot = new Dot();
            dot.setId(thisInfo.getRevision());
            dot.setX(-1); // -1表示节点的X坐标还未调整
            dot.setY(i * 20);
            dot.setFixed(i == 0 || i == commitInfos.size() - 1);
            dots.add(dot);
            // 添加边
            thisInfo.getParents().forEach(parent -> edges.add(new Edge(parent, thisInfo.getRevision())));
        }
        Map<String, Dot> dotIdMap = dots.stream().collect(Collectors.toMap(Dot::getId, e -> e));
        Map<String, List<String>> inDu = new HashMap<>(dots.size()); // 入度
        Map<String, List<String>> outDu = new HashMap<>(dots.size()); // 出度
        for (Edge edge : edges) {
            String source = edge.getSource();
            String target = edge.getTarget();
            inDu.computeIfAbsent(target, key -> new ArrayList<>()).add(source);
            outDu.computeIfAbsent(source, key -> new ArrayList<>()).add(target);
        }
        // 调整点的 x 坐标
        for (int i = commitInfos.size() - 1; i >= 0; i--) {
            GitCommitInfo thisInfo = commitInfos.get(i);
            if (i == commitInfos.size() - 1) { // 最后一个点的X坐标直接初始化为0
                Optional.of(thisInfo).map(GitCommitInfo::getRevision).map(dotIdMap::get).ifPresent(dot -> dot.setX(0));
                continue;
            }
            List<String> parents = thisInfo.getParents();
            // 如果节点的入度是1
            if (parents.size() == 1) {
                String parent = parents.get(0);
                Dot parentDot = dotIdMap.get(parent);
                // 父节点的所有子节点最大 X 坐标
                int maxX = outDu.get(parent).stream().map(dotIdMap::get).mapToInt(Dot::getX).max().orElse(-1);
                int thisX = maxX == -1 ? parentDot.getX() : maxX + 20;
                Optional.of(thisInfo).map(GitCommitInfo::getRevision).map(dotIdMap::get).ifPresent(dot -> dot.setX(thisX));
            }
        }
        return new Data(dots, edges);
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dot {
        private String id;
        private Integer x;
        private Integer y;
        private Boolean fixed;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edge {
        private String source;
        private String target;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private List<Dot> dots;
        private List<Edge> edges;
    }
}
