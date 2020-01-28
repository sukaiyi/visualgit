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
<body style="margin: 0;height: 100%;background: #404a59">
<div style="height: 100%">
    <div id="main" style="width: ${(segments?size*250 + 50)?c}px;height: 800px;background: #404a59"></div>
</div>
<script type="text/javascript">

    var data = ${data};

    option = {
        backgroundColor: '#404a59',

        title: {
            top: 30,
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: function (obj) {
                var value = obj.value;
                return '日期：' + value[0] + '<br>'
                    + '提交数：' + value[1] + '<br>'
            }
        },
        calendar: [
            <#list segments as segment>
            {
                left: ${(100 + segment_index * 240)?c},
                cellSize: [25, 25],
                range: [<#list segment as sg>'${sg}', </#list>],
                orient: 'vertical',
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#000',
                        width: 2,
                        type: 'solid'
                    }
                },
                yearLabel: {
                    formatter: '{start}',
                    textStyle: {
                        color: '#fff'
                    }
                },
                monthLabel: {
                    nameMap: 'cn',
                    margin: 10,
                },
                dayLabel: {
                    firstDay: 1,
                    nameMap: 'cn'
                },
                itemStyle: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            },
            </#list>
        ],
        series: [
            <#list segments as segment>
            {
                name: '提交数',
                type: 'scatter',
                coordinateSystem: 'calendar',
                data: data,
                calendarIndex: ${segment_index},
                symbolSize: function (val) {
                    return val[1] / ${max} * 10 + 5;
                },
                itemStyle: {
                    color: '#ddb926'
                }
            },
            </#list>
        ]
    };


    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
    myChart.on('click', function (param) {
        var data = param.data;
        window.open("commitOfDate?date=" + data[0], '_blank');
    });
    window.addEventListener("resize", function () {
        myChart.resize();
    });
</script>
</body>
</html>