package com.sukaiyi.visualgit.charts;

import com.sukaiyi.visualgit.common.GitCommitInfo;

import java.util.List;

public interface Chart {
    Object data(List<GitCommitInfo> commitInfos);
}
