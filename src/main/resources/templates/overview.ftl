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

<body style="margin: 0">
<div class="jumbotron">
    <div class="container" style="text-align: center">
        <h1>${project}</h1>
        <span class="label label-default">${language}</span>
        <div style="padding: 10px"></div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="text-right">项目类型:</div>
        </div>
        <div class="col-md-6">${language}</div>
        <div class="col-md-6">
            <div class="text-right">累计代码量:</div>
        </div>
        <div class="col-md-6">${totalLines} 行</div>
        <div class="col-md-6">
            <div class="text-right">源文件数:</div>
        </div>
        <div class="col-md-6">${totalFiles} 个</div>
        <div class="col-md-6">
            <div class="text-right">历时:</div>
        </div>
        <div class="col-md-6">${across} 天 ${start} ~ ${end}</div>
        <div class="col-md-6">
            <div class="text-right">涉及开发者:</div>
        </div>
        <div class="col-md-6">${totalDevelopers} 位</div>
    </div>
</div>
</body>
</html>
