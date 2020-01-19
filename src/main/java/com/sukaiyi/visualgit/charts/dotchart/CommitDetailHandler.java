package com.sukaiyi.visualgit.charts.dotchart;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CommitDetailHandler extends AbstractFreemakerHandler {

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "commitDetail.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        String revision = Optional.of(exchange)
                .map(HttpServerExchange::getQueryParameters)
                .map(map -> map.get("revision"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse(null);

        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        List<GitCommitInfo> commitInfos = Optional.of(repoPath)
                .map(GitLogFetcher::fetch)
                .map(GitLogParser::parse)
                .orElse(Collections.emptyList());

        return commitInfos.stream().filter(e -> Objects.equals(e.getRevision(), revision)).findFirst().orElseGet(() -> {
            GitCommitInfo defaultValue = new GitCommitInfo();
            defaultValue.setRevision(revision);
            return defaultValue;
        });
    }
}
