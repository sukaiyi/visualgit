package com.sukaiyi.visualgit.charts.filelist;

import com.sukaiyi.visualgit.VisualGitApplication;
import com.sukaiyi.visualgit.common.CommandRunner;
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
public class FileListHandler extends AbstractFreemakerHandler {

    @Override
    protected String getTemplate(HttpServerExchange exchange) {
        return "filelist.ftl";
    }

    @Override
    protected Object getDataModel(HttpServerExchange exchange) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Deque<String>> params = Optional.of(exchange)
                .map(HttpServerExchange::getQueryParameters)
                .orElse(Collections.emptyMap());
        String path = Optional.of(params)
                .map(m -> m.get("path"))
                .filter(deque -> !deque.isEmpty())
                .map(Deque::poll)
                .orElse("");
        String rawPath = path;

        String repoPath = VisualGitApplication.getInstance().getWorkRepo();
        File repoFile = new File(repoPath);
        String output = CommandRunner.exec("git ls-files", repoFile);
        assert output != null;
        String[] lineArr = output.split("\n");
        boolean isFile = Arrays.asList(lineArr).contains(path);
        if (isFile) {
            path = path.substring(0, Math.max(path.lastIndexOf('/'), 0));
        }
        String finalPath = path;
        List<String> lines = Arrays.stream(lineArr).filter(e -> e.startsWith(finalPath)).collect(Collectors.toList());
        List<FileInfo> fileInfos = new ArrayList<>();
        Set<String> addedFolder = new HashSet<>();
        for (String line : lines) {
            String removePrefix = line.substring(path.length());
            if (removePrefix.startsWith("/")) {
                removePrefix = removePrefix.substring(1);
            }
            if (removePrefix.indexOf('/') < 0) {
                fileInfos.add(new FileInfo(removePrefix, line, true));
            } else {
                String folder = removePrefix.substring(0, removePrefix.indexOf('/'));
                if (!addedFolder.contains(folder)) {
                    addedFolder.add(folder);
                    fileInfos.add(new FileInfo(folder, path.length() == 0 ? folder : path + "/" + folder, false));
                }
            }
        }
        if (path.length() > 0) {
            int lastIndexOf = path.lastIndexOf('/');
            lastIndexOf = Math.max(lastIndexOf, 0);
            fileInfos.add(new FileInfo("..", path.substring(0, lastIndexOf), false));
        }

        map.put("path", path);
        map.put(
                "fileList",
                fileInfos.stream().sorted(Comparator.comparing(FileInfo::getIsFile).thenComparing(FileInfo::getName)).collect(Collectors.toList())
        );
        map.put("content", isFile ? CommandRunner.exec("git blame " + rawPath, repoFile).replace("<", "&lt;").replace(">", "&gt;") : "");
        return map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String name;
        private String path;
        private Boolean isFile;
    }
}
