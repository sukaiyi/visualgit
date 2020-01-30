package com.sukaiyi.visualgit.charts.totallineschart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;

import java.util.*;

/**
 * @author sukaiyi
 * @date 2020/01/21
 */
public class TotalLinesChart implements Chart {

    private static Gson gson = new Gson();

    /**
     * 采样点数
     */
    private static final int SAMPLE_DOTS = 500;

    @Override
    public Object data(List<GitCommitInfo> commitInfos) {
        LongSummaryStatistics statistics = commitInfos.stream()
                .mapToLong(this::timestamp)
                .summaryStatistics();

        // 根据项目存在的时间计算采样的间隔, 总体维持 SAMPLE_DOTS 个采样点
        long start = statistics.getMin();
        long end = statistics.getMax();
        long offset = end - start;
        long sampleInterval = offset / SAMPLE_DOTS;
        List<List<Object>> insertionData = new ArrayList<>(SAMPLE_DOTS);
        List<List<Object>> deletionData = new ArrayList<>(SAMPLE_DOTS);
        for (long i = start; i < end; i += sampleInterval) {
            insertionData.add(Arrays.asList(i, 0L));
            deletionData.add(Arrays.asList(i, 0L));
        }
        for (GitCommitInfo commitInfo : commitInfos) {
            long timestamp = timestamp(commitInfo);
            // 根据时间戳计算该提交应该计算到哪一个采样点
            int pos = Math.min(Math.toIntExact((timestamp - start) / sampleInterval), SAMPLE_DOTS - 1);
            long insertions = commitInfo.getInsertions();
            long deletions = commitInfo.getDeletions();
            List<Object> insertionDataAtPos = insertionData.get(pos);
            List<Object> deletionDataAtPos = deletionData.get(pos);
            insertionDataAtPos.set(1, (Long) insertionDataAtPos.get(1) + insertions);
            deletionDataAtPos.set(1, (Long) deletionDataAtPos.get(1) + deletions);
        }
        for (int i = 1; i < SAMPLE_DOTS; i++) {
            List<Object> insertionDataAtPos = insertionData.get(i);
            List<Object> deletionDataAtPos = deletionData.get(i);
            insertionDataAtPos.set(1, (Long) insertionData.get(i - 1).get(1) + (Long) insertionDataAtPos.get(1));
            deletionDataAtPos.set(1, (Long) deletionData.get(i - 1).get(1) + (Long) deletionDataAtPos.get(1));
        }

        Map<String, Object> model = new HashMap<>();
        model.put("insertionData", gson.toJson(insertionData));
        model.put("deletionData", gson.toJson(deletionData));
        return model;
    }

    private long timestamp(GitCommitInfo info) {
        return Optional.ofNullable(info)
                .map(GitCommitInfo::getCommitter)
                .map(GitCommitInfo.GitCommitDeveloperInfo::getTimestamp)
                .orElse(0L);
    }
}
