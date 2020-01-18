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
    /**
     * hash
     */
    private String hash;
    /**
     * 提交时间戳
     */
    private Long timestamp;
    /**
     * 作者
     */
    private String author;
    /**
     * 作者邮箱
     */
    private String email;
    /**
     * 提交标题，（第一行）
     */
    private String title;
    /**
     * 提交内容，（除第一行）
     */
    private String content;

    /**
     * 影响的文件数量
     */
    private Integer fileCount;
    /**
     * 插入行
     */
    private Integer insertions;
    /**
     * 删除行
     */
    private Integer deletions;

    /**
     * 该提交的文件信息
     */
    private List<GitCommitFileInfo> fileInfos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GitCommitFileInfo {
        /**
         * 文件路径
         */
        private String file;
        /**
         * 插入行
         */
        private Integer insertions;
        /**
         * 删除行
         */
        private Integer deletions;
    }
}
