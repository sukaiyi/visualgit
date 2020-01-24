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
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
    </button>
    <h4 class="modal-title">${hash}</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-2"><p class="text-right">Hash:</p></div>
        <div class="col-md-10" style="padding-left: 0">${hash}</div>
    </div>
    <div class="row">
        <div class="col-md-2"><p class="text-right">提交人:</p></div>
        <div class="col-md-10" style="padding-left: 0"><span class="label label-default">${committer.name}</span> ${committer.email}</div>
    </div>
    <div class="row">
        <div class="col-md-2"><p class="text-right">提交时间:</p></div>
        <div class="col-md-10"
             style="padding-left: 0">${committer.timestamp?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</div>
    </div>
    <div class="row">
        <div class="col-md-2"><p class="text-right">影响文件:</p></div>
        <div class="col-md-10" style="padding-left: 0">${stats?size} 新增${insertions}行, 删除${deletions}行</div>
    </div>
    <div class="row">
        <div class="col-md-2"><p class="text-right">提交内容:</p></div>
        <div class="col-md-10" style="padding-left: 0">${subject}${body}</div>
    </div>

    <#assign fileList = ""/>
    <#list stats as info>
        <#assign thisFile = info.file + ", +" + info.insertions + ", -" + info.deletions/>
        <#assign fileList = fileList + "\n" + "<a href='fileChangeDetail?hash=${hash}&file=${info.file}' target='_blank'>${thisFile}</a>"/>
    </#list>
    <pre style="max-height: 400px">${fileList}</pre>
</div>
</body>
</html>
