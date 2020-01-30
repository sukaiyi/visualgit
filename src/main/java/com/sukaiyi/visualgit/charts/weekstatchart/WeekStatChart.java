package com.sukaiyi.visualgit.charts.weekstatchart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeekStatChart implements Chart {

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        WeekStatChartModel model = new WeekStatChartModel();
        List<String> xAxis = IntStream.range(0, 24).boxed().map(e -> String.format("%02d", e)).collect(Collectors.toList());
        List<String> yAxis = Arrays.asList("日", "一", "二", "三", "四", "五", "六");
        model.setYAxis(yAxis);
        model.setXAxis(xAxis);

        Map<String, List<GitCommitInfo>> infoGroupByAuthor = commitInfos.stream()
                .collect(Collectors.groupingBy(e -> e.getCommitter().getName()))
                .entrySet().stream()
                .sorted((en1, en2) -> en2.getValue().size() - en1.getValue().size())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));
        Map<String, List<GitCommitInfo>> infoGroupByAuthorTop = infoGroupByAuthor;
        if (infoGroupByAuthor.size() > 6) {
            infoGroupByAuthorTop = new LinkedHashMap<>(6);
            List<GitCommitInfo> otherInfos = new ArrayList<>();
            int i = 0;// 取前 5 个
            for (Map.Entry<String, List<GitCommitInfo>> entry : infoGroupByAuthor.entrySet()) {
                if (i < 5) {
                    infoGroupByAuthorTop.put(entry.getKey(), entry.getValue());
                } else {
                    otherInfos.addAll(entry.getValue());
                }
                i++;
            }
            infoGroupByAuthorTop.put("other", otherInfos);
        }

        Map<String, List<List<Object>>> data = new LinkedHashMap<>(infoGroupByAuthorTop.size());
        for (Map.Entry<String, List<GitCommitInfo>> entry : infoGroupByAuthorTop.entrySet()) {
            String committer = entry.getKey();
            List<GitCommitInfo> infos = entry.getValue();
            List<List<Object>> thisCommitterData = new ArrayList<>(7 * 24);
            for (String yAxi : yAxis) {
                for (String xAxi : xAxis) {
                    thisCommitterData.add(Arrays.asList(xAxi, yAxi, 0));
                }
            }
            for (GitCommitInfo info : infos) {
                long timestamp = info.getCommitter().getTimestamp();
                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.of("+8"));
                int dayOfWeek = dateTime.getDayOfWeek().getValue();
                int hour = dateTime.getHour();
                List<Object> thisHourData = thisCommitterData.get((dayOfWeek % 7) * 24 + hour);
                thisHourData.set(2, (Integer) thisHourData.get(2) + 1);
            }
            data.put(committer, thisCommitterData);
        }
        model.setData(data);
        return model;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeekStatChartModel {
        private List<String> xAxis;
        private List<String> yAxis;
        private Map<String, List<List<Object>>> data;
    }

}
