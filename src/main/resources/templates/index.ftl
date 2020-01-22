<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/static/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <title>Visual Git</title>
</head>
<body style="margin: 0;overflow-y: hidden;">
<nav id="navbar" class="navbar navbar-default" style="margin: 0;border-radius: 0">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" target="_blank" href="https://github.com/sukaiyi/visualgit">Visual Git</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li src="overview" tinge="default" class="nav-item active"><a href="#">总览</a></li>
                <li src="dotchart" tinge="inverse" class="nav-item"><a href="#">提交散点图</a></li>
                <li src="totalLinesChart" tinge="default" class="nav-item"><a href="#">代码量变化图</a></li>
                <li src="calendarChart" tinge="inverse" class="nav-item"><a href="#">提交日历</a></li>
                <li src="filelist" tinge="default" class="nav-item"><a href="#">源文件</a></li>
                <li src="rela" tinge="default" class="nav-item"><a href="#">提交关系图</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">Dropdown <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a target="_blank" href="https://github.com/sukaiyi/visualgit">Github</a></li>
                        <li><a href="#">Another action</a></li>
                        <li><a href="#">Something else here</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#">Separated link</a></li>
                    </ul>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<iframe id="iframe" src="overview" style="border: none" onload="resetIframeHeight()"
        width="100%"></iframe>
<script type="text/javascript">
    $(".nav-item").click(function () {
        $(".nav-item").removeClass("active");
        $(this).addClass("active");
        $("#iframe").attr("src", this.getAttribute("src"));
        var navbar = $("#navbar");
        navbar.removeClass("navbar-default");
        navbar.removeClass("navbar-inverse");
        navbar.addClass("navbar-" + this.getAttribute("tinge"))
    });
    window.addEventListener("resize", resetIframeHeight);

    function resetIframeHeight() {
        var navHeight = $("#navbar").outerHeight();
        $("#iframe").height(document.documentElement.clientHeight - navHeight);
    }

</script>
</body>
</html>