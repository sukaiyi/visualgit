<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="utf-8">
    <title>dotchart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="/static/echarts.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body style="margin: 0px;height: 100%">
<div style="height: 100%">
    <div id="main" style="width: 100%;height: 100%"></div>
    <!-- Modal -->
    <div id="modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    option = {
        tooltip: {
            padding: 10,
            backgroundColor: '#222',
            borderColor: '#777',
            borderWidth: 1,
            formatter: function (obj) {
                var value = obj.value;
                var date = new Date(value[0]);

                return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); padding-bottom: 7px;margin-bottom: 7px">'
                    + '<span style="font-size: 18px;display: block">' + value[3] + '</span>'
                    + '<span>' + value[4] + '</span>'
                    + '</div>'
                    + 'Hash：' + value[5] + '<br>'
                    + '   Notes：' + value[6] + '<br>'
                    + '   代码行：' + value[1] + '<br>'
                    + ' 涉及文件：' + value[2] + '<br>'
                    + ' 提交时间：' + date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + '<br>';

            }
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        dataZoom: [
            {
                type: 'slider',
                xAxisIndex: [0],
                start: 0,
                end: 100,
                bottom: "-10",
                showDetail: false,
                borderColor: 'rgba(167, 183, 204, 0.0)'
            },
            {type: 'inside', xAxisIndex: [0], start: 0, end: 100}
        ],
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            },
        },
        yAxis: {
            type: 'value',
            scale: true,
            splitLine: {
                show: false
            },
        },
        series: [
            {
                name: '代码总量',
                type: 'line',
                areaStyle: {},
                data: ${data}
            },
        ]
    };


    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
    myChart.on('click', function (param) {
        var data = param.data;
        $('#modal').modal({remote: "commitDetail?hash=" + data[5]});
    });
    window.addEventListener("resize", function () {
        myChart.resize();
    });
</script>
</body>
</html>