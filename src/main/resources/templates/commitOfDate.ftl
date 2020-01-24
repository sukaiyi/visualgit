<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${date?number_to_datetime?string("yyyy-MM-dd")}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/static/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body>
<div>
    <div id="main">
        <div class="row" style="text-align: center; margin-top: 32px">
            <div class="panel panel-default" style="width:1000px;display: inline-block;text-align: left;">
                <div class="panel-heading">${date?number_to_datetime?string("yyyy年MM月dd日")}的提交</div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-2" style="padding-right: 0"><b>Hash</b></div>
                        <div class="col-md-1" style="padding-right: 0"><b>Committer</b></div>
                        <div class="col-md-1" style="padding-right: 0"><b>Time</b></div>
                        <div class="col-md-1" style="padding-right: 0"><b>Insertions</b></div>
                        <div class="col-md-1" style="padding-right: 0"><b>Deletions</b></div>
                        <div class="col-md-6" style="padding-right: 0"><b>Title</b></div>
                    </div>
                    <#list commitInfos as info>
                        <div class="row">
                            <div class="col-md-2" style="padding-right: 0">
                                <a href="javascript:void(0);"
                                   onclick="$('#modal').modal({remote: 'commitDetail?hash=${info.hash}'});">
                                    ${info.hash?substring(0, 12)}
                                </a>
                            </div>
                            <div class="col-md-1" style="padding-right: 0">${info.committer.name}</div>
                            <div class="col-md-1"
                                 style="padding-right: 0">${info.committer.timestamp?number_to_datetime?string("HH:mm:ss")}</div>
                            <div class="col-md-1" style="padding-right: 0">${info.insertions}</div>
                            <div class="col-md-1" style="padding-right: 0">${info.deletions}</div>
                            <div class="col-md-6" style="padding-right: 0">${info.subject}</div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </div>
    <div id="modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">


</script>
</body>
</html>