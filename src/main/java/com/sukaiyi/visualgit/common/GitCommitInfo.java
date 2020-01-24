package com.sukaiyi.visualgit.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitCommitInfo {
    private List<String> refs;
    private String hash;
    private String hashAbbrev;
    private String tree;
    private String treeAbbrev;
    private List<String> parents;
    private List<String> parentsAbbrev;
    private GitCommitDeveloperInfo author;
    private GitCommitDeveloperInfo committer;
    private String subject;
    private String body;
    private String notes;
    private Long insertions;
    private Long deletions;
    /**
     * 该提交的文件信息
     */
    private List<GitCommitFileInfo> stats;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GitCommitFileInfo {
        private String file;
        private Long insertions;
        private Long deletions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GitCommitDeveloperInfo {
        private String name;
        private String email;
        private Long timestamp;
    }

}
