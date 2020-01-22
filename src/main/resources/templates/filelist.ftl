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
<body style="margin: 16px">
<div>
    <div class="row" style="margin-bottom: 16px">
        <div class="col-md-12">${path}</div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="list-group">
                <#list fileList as file>
                    <a href="filelist?path=${file.path}" class="list-group-item">
                        <span class="iconfont icon<#if file.isFile==false>-folder<#else>-feeds</#if>"></span>
                        ${file.name}
                    </a>
                </#list>
            </div>
        </div>
        <div class="col-md-8">
            <#if content?length gt 0>
                <pre style="overflow: auto;">${content}</pre>
            </#if>
        </div>
    </div>
</div>
<style type="text/css">
    .iconfont {
        font-size: 24px;
    }

    .list-group-item {
        padding: 5px 8px;
    }
</style>
</body>
</html>