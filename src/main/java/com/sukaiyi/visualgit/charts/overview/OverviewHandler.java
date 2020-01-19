package com.sukaiyi.visualgit.charts.overview;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.utils.DateUtils;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OverviewHandler extends AbstractFreemakerHandler {

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "overview.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        File repoFile = new File(repoPath);
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());

        Map<String, Integer> fileTypeStatsMap = new HashMap<>();
        Set<String> fileSet = new HashSet<>();
        Set<String> peopleSet = new HashSet<>();
        long timestampStart = Long.MAX_VALUE;
        long timestampEnd = 0L;
        long totalLines = 0L;
        for (GitCommitInfo commitInfo : commitInfos) {
            List<GitCommitInfo.GitCommitFileInfo> fileInfos = Optional.of(commitInfo)
                    .map(GitCommitInfo::getFileInfos)
                    .orElse(Collections.emptyList());
            for (GitCommitInfo.GitCommitFileInfo fileInfo : fileInfos) {
                String path = fileInfo.getFile();
                int lastIndexOfDot = path.lastIndexOf('.');
                String fileType = path.substring(lastIndexOfDot + 1).toLowerCase();
                fileTypeStatsMap.put(fileType, fileTypeStatsMap.getOrDefault(fileType, 0) + 1);
                fileSet.add(path);
            }
            peopleSet.add(commitInfo.getAuthor());
            timestampStart = timestampStart > commitInfo.getTimestamp() ? commitInfo.getTimestamp() : timestampStart;
            timestampEnd = timestampEnd < commitInfo.getTimestamp() ? commitInfo.getTimestamp() : timestampEnd;
            totalLines += commitInfo.getInsertions() - commitInfo.getDeletions();
        }
        fileTypeStatsMap = fileTypeStatsMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> n, LinkedHashMap::new));

        Map<String, Object> data = new HashMap<>();
        data.put("project", repoFile.getName());
        Iterator<String> languageIterator = fileTypeStatsMap.keySet().iterator();
        data.put("language", languageIterator.hasNext() ? languageIterator.next() : "");
        data.put("totalLines", totalLines);
        data.put("totalFiles", fileSet.size());
        data.put("totalDevelopers", peopleSet.size());
        data.put("developers", peopleSet);
        data.put("across", (timestampEnd - timestampStart) / 1000 / 60 / 60 / 24);
        data.put("start", DateUtils.format(new Date(timestampStart), "yyyy-MM-dd"));
        data.put("end", DateUtils.format(new Date(timestampEnd), "yyyy-MM-dd"));
        return data;
    }
}
