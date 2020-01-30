package com.sukaiyi.visualgit.charts.weekstatchart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.charts.dotchart.DotChart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class WeekStatChartHandler extends AbstractFreemakerHandler {

    private Chart dotChart = new WeekStatChart();
    private static final Gson gson = new Gson();

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "weekStatChart.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());
        Map<String, Object> map = new HashMap<>();
        map.put("data", gson.toJson(dotChart.data(commitInfos)));
        return map;
    }
}
