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
    <script src="https://cdn.jsdelivr.net/npm/@gitgraph/js"></script>
</head>
<body style="margin: 0px;height: 100%">
<div style="height: 100%">
    <div id="graph-container" style="width: 100%;height: 100%"></div>
    <!-- Modal -->
    <div id="modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    const graphContainer = document.getElementById("graph-container");

    // Instantiate the graph.
    const gitgraph = GitgraphJS.createGitgraph(graphContainer);

    const master = gitgraph.branch("master");
    master.commit("Initial commit");

    const develop = gitgraph.branch("develop");
    develop.commit("Add TypeScript");

    const aFeature = gitgraph.branch("a-feature");
    aFeature
        .commit("Make it work")
        .commit("Make it right")
        .commit("Make it fast");

    develop.merge(aFeature);
    develop.commit("Prepare v1");

    master.merge(develop).tag("v1.0.0");
</script>
</body>
</html>