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
    var authors = [
        <#list authors as author>'${author}', </#list>
    ];
    var itemStyle = {
        opacity: 0.8,
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowOffsetY: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
    };

    option = {
        title: {
            text: '共 ${totalCommit} 个提交',
            bottom: 0,
            textStyle: {
                color: 'rgba(255, 255, 255, 0.3)'
            }
        },
        backgroundColor: '#404a59',
        color: [
            '#FFD700', '#FF69B4', '#FFF0F5', '#FF4500', '#F5DEB3',
            '#A52A2A', '#008080', '#90EE90', '#00BFFF', '#87CECB'
        ],
        legend: {
            top: 10,
            data: authors,
            textStyle: {
                color: '#fff',
                fontSize: 14
            }
        },
        grid: {
            left: '10%',
            right: 150,
            top: '18%',
            bottom: '10%'
        },
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
                    + 'Revision：' + value[5] + '<br>'
                    + '   Notes：' + value[6] + '<br>'
                    + '   代码行：' + value[1] + '<br>'
                    + ' 涉及文件：' + value[2] + '<br>'
                    + ' 提交时间：' + date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + '<br>';

            }
        },
        xAxis: {
            type: 'time',
            name: '时间',
            scale: true,
            nameTextStyle: {
                color: 'rgba(255, 255, 255, 0.3)',
                fontSize: 14
            },
            splitLine: {
                show: false
            },
            axisLine: {
                lineStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                }
            }
        },
        yAxis: {
            type: 'log',
            name: '代码行数',
            logBase: 2,
            min: 1,
            scale: true,
            nameTextStyle: {
                color: 'rgba(255, 255, 255, 0.3)',
                fontSize: 14
            },
            axisLine: {
                lineStyle: {
                    color: 'rgba(255, 255, 255, 0.3)'
                }
            },
            splitLine: {
                show: false
            }
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
        visualMap: [
            {
                left: 'right',
                top: '10%',
                min: 0,
                max: 30,
                dimension: 2,
                itemWidth: 30,
                itemHeight: 120,
                calculable: true,
                precision: 0.1,
                text: ['气泡大小：涉及文件'],
                textGap: 30,
                textStyle: {
                    color: '#fff'
                },
                inRange: {
                    symbolSize: [10, 70]
                },
                outOfRange: {
                    symbolSize: [10, 70],
                    color: ['rgba(255,255,255,.1)']
                },
                controller: {
                    inRange: {
                        color: ['#c23531']
                    },
                    outOfRange: {
                        color: ['#444']
                    }
                }
            }
        ],
        series: [
            <#list data?keys as key>
            {
                name: '${key}',
                type: 'scatter',
                itemStyle: itemStyle,
                data: ${data[key]}
            },
            </#list>
        ]
    };

    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
    myChart.on('click', function (param) {
        var data = param.data;
        $('#modal').modal({remote: "commitDetail?revision=" + data[5]});
    });
    window.addEventListener("resize", function () {
        myChart.resize();
    });
</script>
</body>
</html>