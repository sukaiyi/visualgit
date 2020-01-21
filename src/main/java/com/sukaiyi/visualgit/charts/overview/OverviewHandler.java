package com.sukaiyi.visualgit.charts.overview;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.common.GitCommitInfo;
import com.sukaiyi.visualgit.common.GitLogFetcher;
import com.sukaiyi.visualgit.common.GitLogParser;
import com.sukaiyi.visualgit.utils.DateUtils;
import com.sukaiyi.visualgit.webhandler.AbstractFreemakerHandler;
import io.undertow.server.HttpServerExchange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OverviewHandler extends AbstractFreemakerHandler {

    private static final Map<String, String> FILE_TYPE_LANGUAGE_MAP = new HashMap<>();

    static {
        FILE_TYPE_LANGUAGE_MAP.put("java", "Java");
        FILE_TYPE_LANGUAGE_MAP.put("cs", "C#");
        FILE_TYPE_LANGUAGE_MAP.put("c", "C");
        FILE_TYPE_LANGUAGE_MAP.put("cpp", "C++");
        FILE_TYPE_LANGUAGE_MAP.put("py", "Python");
        FILE_TYPE_LANGUAGE_MAP.put("php", "PHP");
        FILE_TYPE_LANGUAGE_MAP.put("js", "JavaScript");
        FILE_TYPE_LANGUAGE_MAP.put("ts", "TypeScript");
        FILE_TYPE_LANGUAGE_MAP.put("html", "HTML");
        FILE_TYPE_LANGUAGE_MAP.put("sh", "Shell");
        FILE_TYPE_LANGUAGE_MAP.put("bat", "Bat");
    }

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
        Map<String, DeveloperStatInfo> developerStatInfoMap = new HashMap<>();
        long timestampStart = Long.MAX_VALUE;
        long timestampEnd = 0L;
        long totalLines = 0L;
        for (GitCommitInfo commitInfo : commitInfos) {
            List<GitCommitInfo.GitCommitFileInfo> fileInfos = Optional.of(commitInfo)
                    .map(GitCommitInfo::getFileInfos)
                    .orElse(Collections.emptyList());
            DeveloperStatInfo developerStatInfo = Optional.of(commitInfo).map(GitCommitInfo::getAuthor).map(developerStatInfoMap::get).orElseGet(() -> {
                DeveloperStatInfo info = new DeveloperStatInfo();
                info.setName(commitInfo.getAuthor());
                info.setEmail(commitInfo.getEmail());
                info.setCommitNum(0);
                info.setInsertions(0L);
                info.setDeletions(0L);
                return info;
            });
            developerStatInfoMap.put(commitInfo.getAuthor(), developerStatInfo);
            developerStatInfo.setCommitNum(developerStatInfo.getCommitNum() + 1);
            developerStatInfo.setInsertions(developerStatInfo.getInsertions() + commitInfo.getInsertions());
            developerStatInfo.setDeletions(developerStatInfo.getDeletions() + commitInfo.getDeletions());
            developerStatInfo.setPureLines(developerStatInfo.getInsertions() - developerStatInfo.getDeletions());
            for (GitCommitInfo.GitCommitFileInfo fileInfo : fileInfos) {
                String path = fileInfo.getFile();
                int lastIndexOfDot = path.lastIndexOf('.');
                String fileType = path.substring(lastIndexOfDot + 1).toLowerCase();
                fileTypeStatsMap.put(fileType, fileTypeStatsMap.getOrDefault(fileType, 0) + 1);
                fileSet.add(path);
            }
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
        data.put("language", Optional.of(languageIterator).filter(Iterator::hasNext).map(Iterator::next).map(e -> FILE_TYPE_LANGUAGE_MAP.getOrDefault(e, e)).orElse(""));
        data.put("totalLines", totalLines);
        data.put("commits", commitInfos.size());
        data.put("totalFiles", fileSet.size());
        data.put("totalDevelopers", developerStatInfoMap.size());
        data.put("developers", developerStatInfoMap.keySet());
        data.put("across", (timestampEnd - timestampStart) / 1000 / 60 / 60 / 24);
        data.put("start", DateUtils.format(new Date(timestampStart), "yyyy-MM-dd"));
        data.put("end", DateUtils.format(new Date(timestampEnd), "yyyy-MM-dd"));
        data.put("maintainer", Optional.of(developerStatInfoMap).map(this::maintainer).orElse(null));
        data.put(
                "rank",
                developerStatInfoMap.values().stream()
                        .sorted(
                                Comparator.comparing(DeveloperStatInfo::getCommitNum)
                                        .thenComparing(DeveloperStatInfo::getPureLines)
                                        .reversed()
                        )
                        .limit(10)
                        .collect(Collectors.toList())
        );
        return data;
    }

    private DeveloperStatInfo maintainer(Map<String, DeveloperStatInfo> developerStatInfoMap) {
        return developerStatInfoMap.values().stream()
                .max((info1, info2) -> {
                    long v1 = info1.getCommitNum() * 50 + info1.getInsertions() * 30 + info1.getDeletions() * 20;
                    long v2 = info2.getCommitNum() * 50 + info2.getInsertions() * 30 + info2.getDeletions() * 20;
                    return (int) Math.signum(v1 - v2);
                })
                .orElse(null);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeveloperStatInfo {
        private String name;
        private String email;
        private Integer commitNum;
        private Long pureLines;
        private Long insertions;
        private Long deletions;
    }
}
