package com.sukaiyi.visualgit;

import com.sukaiyi.visualgit.charts.dotchart.CommitDetailHandler;
import com.sukaiyi.visualgit.charts.dotchart.DotChartHandler;
import com.sukaiyi.visualgit.charts.dotchart.FileChangeDetailHandler;
import com.sukaiyi.visualgit.webhandler.StaticHandler;
import com.sukaiyi.visualgit.webhandler.IndexHandler;
import io.undertow.Undertow;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;

@Data
@Slf4j
public class VisualGitApplication {

    private String workRepo = null;
    private static final VisualGitApplication INSTANCE = new VisualGitApplication();

    public static void main(String[] args) {
        String workRepo = Optional.of(args)
                .filter(e -> e.length > 0).map(e -> e[0])
                .map(File::new)
                .filter(File::exists)
                .map(File::getAbsolutePath)
                .orElse(null);
        if (workRepo == null) {
            System.out.println("Usage: java -jar visualgit.jar <repo-path>");
            return;
        }
        INSTANCE.setWorkRepo(workRepo);

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(
                        new PathBasedWebRequestDispatcher()
                                .match("/index", new IndexHandler())
                                .match("/dotchart", new DotChartHandler())
                                .match("/commitDetail", new CommitDetailHandler())
                                .match("/fileChangeDetail", new FileChangeDetailHandler())
                                .match("/static/.*", new StaticHandler())
                ).build();
        server.start();
    }

    public static VisualGitApplication getInstance() {
        return INSTANCE;
    }
}