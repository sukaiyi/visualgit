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
    var data = ${data};
    var xAxis = data.xAxis;
    var yAxis = data.yAxis;
    var series = [];
    var max = 0;
    var min = 99999999;
    for (var key in data.data) {
        var value = data.data[key];
        for (var i = 0; i < value.length; i++) {
            if (max < value[i][2]) {
                max = value[i][2];
            }
            if (min > value[i][2] && value[i][2] !== 0) {
                min = value[i][2];
            }
        }
    }
    for (var key in data.data) {
        series.push({
            name: key,
            data: data.data[key],
            type: 'scatter',
            symbolSize: function (item) {
                return item[2] === 0 ? 0 : (40 * item[2]) / max + 5;
            }
        });
    }
    option = {
        legend: {},
        xAxis: {
            type: 'category',
            data: xAxis,
        },
        yAxis: {
            type: 'category',
            data: yAxis,
            axisLabel: {
                color: "#000000"
            },
            axisLine: {
                lineStyle: {
                    color: "#00000000"
                }
            },
            splitLine: {
                show: true
            },
        },
        series: series
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