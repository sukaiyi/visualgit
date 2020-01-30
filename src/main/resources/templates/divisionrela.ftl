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
<body style="margin: 0;height: 100%">
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

    var dots = ${dots};
    var edges = ${edges};

    var option = {
        tooltip: {
            padding: 10,
            backgroundColor: '#222',
            borderColor: '#777',
            borderWidth: 1,
            formatter: function (obj) {
                var dataType = obj.dataType;
                var name = dataType === 'edge' ? obj.name : obj.data.name;
                var value = dataType === 'edge' ? obj.value : obj.data.value;
                return '<div style="padding-bottom: 7px;margin-bottom: 7px">'
                    + '<span style="font-size: 18px;display: block">' + name + ' ' + value + '</span>'
                    + '</div>';
            }
        },
        series: [{
            type: 'graph',
            layout: 'force',
            animation: false,
            data: dots,
            force: {
                // initLayout: 'circular'
                repulsion: 100,
                edgeLength: [100, 200]
            },
            edges: edges
        }]
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