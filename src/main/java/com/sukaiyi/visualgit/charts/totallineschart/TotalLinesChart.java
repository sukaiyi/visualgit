package com.sukaiyi.visualgit.charts.totallineschart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sukaiyi
 * @date 2020/01/21
 */
public class TotalLinesChart implements Chart {

    private static Gson gson = new Gson();

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        List<List<Object>> data = commitInfos.stream()
                .sorted(Comparator.comparingLong(info -> info.getCommitter().getTimestamp()))
                .map(info -> {
                    String title = Optional.ofNullable(info.getSubject()).orElse("");
                    return Arrays.<Object>asList(
                            info.getCommitter().getTimestamp(),
                            info.getInsertions() - info.getDeletions(),
                            Optional.ofNullable(info.getStats()).map(List::size).orElse(0),
                            Optional.ofNullable(info.getCommitter()).map(GitCommitInfo.GitCommitDeveloperInfo::getName).orElse(""),
                            Optional.ofNullable(info.getCommitter()).map(GitCommitInfo.GitCommitDeveloperInfo::getEmail).orElse(""),
                            Optional.ofNullable(info.getHash()).orElse(""),
                            title.length() > 30 ? title.substring(0, 30) + "..." : title
                    );
                })
                .collect(Collectors.toList());
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                continue;
            }
            List<Object> datum = data.get(i);
            List<Object> pre = data.get(i - 1);
            datum.set(1, (Long) datum.get(1) + (Long) pre.get(1));
        }
        Map<String, Object> model = new HashMap<>();
        model.put("data", gson.toJson(data));
        return model;
    }
}
