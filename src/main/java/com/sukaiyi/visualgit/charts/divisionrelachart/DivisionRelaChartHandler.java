package com.sukaiyi.visualgit.charts.divisionrelachart;

import com.google.gson.Gson;
import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.charts.Chart;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class DivisionRelaChartHandler extends AbstractFreemakerHandler {

    private static Gson gson = new Gson();
    private Chart chart = new DivisionRelaChart();

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "divisionrela.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());
        Map<String, Object> map = new HashMap<>();
        DivisionRelaChart.Graph graph = (DivisionRelaChart.Graph) chart.data(commitInfos);
        map.put("dots", gson.toJson(graph.getDots()));
        map.put("edges", gson.toJson(graph.getEdges()));
        return map;
    }
}
