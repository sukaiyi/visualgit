<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="/static/iconfont.css">
    <script src="/static/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <title>Visual Git</title>
</head>

<body style="margin: 0">
<div class="jumbotron">
    <div class="container" style="text-align: center">
        <h1>${project}</h1>
        <span class="label label-default">${language}</span>
        <div style="padding: 10px"></div>
    </div>

    <div class="row" style="text-align: center; margin-top: 32px">
        <div class="panel panel-default card" style="background:#87CEEB; width:350px">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-rankinglist-fill"></span>
                    <span style="font-size: 24px;">贡献榜</span>
                </div>
                <#list rank as ra>
                    <div class="row">
                        <div class="col-md-6" style="padding-right: 0">${ra.name}</div>
                        <div class="col-md-3" style="padding-right: 0">+${ra.insertions}</div>
                        <div class="col-md-3" style="padding-right: 0">-${ra.deletions}</div>
                    </div>
                </#list>
            </div>
        </div>
    </div>

    <div class="row" style="text-align: center;">
        <div class="panel panel-default card" style="background:#FFA500">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-apparel"></span>
                    <span style="font-size: 24px;">${language}</span>
                </div>
                <div style="color: grey; text-align: right">开发语言</div>
            </div>
        </div>
        <div class="panel panel-default card" style="background:#FF8C00">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-code"></span>
                    <span style="font-size: 24px;">${totalLines} 行</span>
                </div>
                <div style="color: grey; text-align: right">累计代码量</div>
            </div>
        </div>
        <div class="panel panel-default card" style="background:#40E0D0">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-writing"></span>
                    <span style="font-size: 24px;">${totalFiles}</span>
                </div>
                <div style="color: grey; text-align: right">源文件</div>
            </div>
        </div>
        <div class="panel panel-default card" style="background:#EEE8AA">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-ontimeshipment"></span>
                    <span style="font-size: 24px;">${across} 天</span>
                </div>
                <div style="color: grey; text-align: right">${start} ~ ${end}</div>
            </div>
        </div>
    </div>
    <div class="row" style="text-align: center;">
        <div class="panel panel-default card" style="background:#87CEEB">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-certified-supplier"></span>
                    <span style="font-size: 24px;">${maintainer.name}</span>
                </div>
                <div style="color: grey; text-align: right">主要维护者</div>
            </div>
        </div>
        <div class="panel panel-default card" style="background:#DCDCDC">
            <div class="panel-body">
                <div style="display: flex; justify-content: space-between; align-content: center;">
                    <span class="iconfont icon-Customermanagement"></span>
                    <span style="font-size: 24px;">${totalDevelopers} 位</span>
                </div>
                <div style="color: grey; text-align: right">涉及开发者</div>
            </div>
        </div>
    </div>

</div>

<style type="text/css">
    .card {
        display: inline-block;
        width: 200px;
        text-align: left;
    }

    .panel-body {
        padding: 7px
    }

    .iconfont {
        font-size: 32px;
    }

    .icon-container {
        padding: 7px;
    }
</style>
</body>
</html>
