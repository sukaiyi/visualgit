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
        legend: {},
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            },
            formatter: function (params, ticket, callback) {
                var timestamp = params[0].data[0];
                var date = new Date(timestamp);
                var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
                return '日期:' + dateStr + '</br>' +
                    '净增:' + (params[0].data[1] - params[1].data[1]) + '</br>' +
                    '新增:' + params[0].data[1] + '</br>' +
                    '删除:' + params[1].data[1] + '</br>';
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
                name: '新增',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                showSymbol: false,
                areaStyle: {},
                data: ${insertionData}
            },
            {
                name: '删除',
                type: 'line',
                smooth: true,
                symbol: 'circle',
                showSymbol: false,
                areaStyle: {},
                data: ${deletionData}
            },
        ]
    };


    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
    myChart.on('click', function (param) {
    });
    window.addEventListener("resize", function () {
        myChart.resize();
    });
</script>
</body>
</html>