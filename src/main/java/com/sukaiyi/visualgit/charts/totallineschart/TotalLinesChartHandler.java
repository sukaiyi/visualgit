package com.sukaiyi.visualgit.charts.totallineschart;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TotalLinesChartHandler extends AbstractFreemakerHandler {

    private Chart dotChart = new TotalLinesChart();

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "totalLinesChart.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());
        return dotChart.data(commitInfos);
    }
}
