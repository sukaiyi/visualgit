package com.sukaiyi.visualgit;

import com.sukaiyi.common.utils.BrowserUtils;
import com.sukaiyi.common.utils.NetUtils;
import com.sukaiyi.visualgit.charts.branchgraph.BranchGraphHandler;
import com.sukaiyi.visualgit.charts.calendarchart.CalendarChartHandler;
import com.sukaiyi.visualgit.charts.calendarchart.CommitOfDateHandler;
import com.sukaiyi.visualgit.charts.divisionrelachart.DivisionRelaChartHandler;
import com.sukaiyi.visualgit.charts.dotchart.CommitDetailHandler;
import com.sukaiyi.visualgit.charts.dotchart.DotChartHandler;
import com.sukaiyi.visualgit.charts.dotchart.FileChangeDetailHandler;
import com.sukaiyi.visualgit.charts.filelist.FileListHandler;
import com.sukaiyi.visualgit.charts.overview.OverviewHandler;
import com.sukaiyi.visualgit.charts.totallineschart.TotalLinesChartHandler;
import com.sukaiyi.visualgit.charts.weekstatchart.WeekStatChartHandler;
import com.sukaiyi.visualgit.webhandler.IndexHandler;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Optional;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;

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

        HttpHandler pathHadnler = path()
                .addExactPath("/index", new IndexHandler())
                .addExactPath("/overview", new OverviewHandler())
                .addExactPath("/dotchart", new DotChartHandler())
                .addExactPath("/totalLinesChart", new TotalLinesChartHandler())
                .addExactPath("/calendarChart", new CalendarChartHandler())
                .addExactPath("/branchgraph", new BranchGraphHandler())
                .addExactPath("/divisionrela", new DivisionRelaChartHandler())
                .addExactPath("/weekstatchart", new WeekStatChartHandler())
                .addExactPath("/filelist", new FileListHandler())
                .addExactPath("/commitDetail", new CommitDetailHandler())
                .addExactPath("/commitOfDate", new CommitOfDateHandler())
                .addExactPath("/fileChangeDetail", new FileChangeDetailHandler())
                .addPrefixPath("/static", resource(new ClassPathResourceManager(VisualGitApplication.class.getClassLoader(), "static")));
        String host = "localhost";
        int port = NetUtils.choosePort(9000, 9001, 9002);
        Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(pathHadnler).build();
        server.start();
        BrowserUtils.openQuiet(String.format("http://%s:%s/index", host, port));
    }

    public static VisualGitApplication getInstance() {
        return INSTANCE;
    }
}