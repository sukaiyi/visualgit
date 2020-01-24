package com.sukaiyi.visualgit.charts.calendarchart;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.utils.DateUtils;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sukaiyi
 * @date 2020/01/21
 */
public class CommitOfDateHandler extends AbstractFreemakerHandler {
    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "commitOfDate.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        Map<String, Deque<String>> params = Optional.of(exchange)
                .map(HttpServerExchange::getQueryParameters)
                .orElse(Collections.emptyMap());
        String date = Optional.of(params)
                .map(map -> map.get("date"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse(null);
        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());

        Date startOfDate = DateUtils.parse(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        assert startOfDate != null;
        long start = startOfDate.getTime();
        long end = start + 24 * 60 * 60 * 1000;
        List<GitCommitInfo> infos = commitInfos.stream()
                .filter(info -> info.getCommitter().getTimestamp() >= start && info.getCommitter().getTimestamp() < end)
                .sorted(Comparator.comparing(info -> info.getCommitter().getTimestamp()))
                .collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("date", start);
        data.put("commitInfos", infos);
        return data;
    }
}
