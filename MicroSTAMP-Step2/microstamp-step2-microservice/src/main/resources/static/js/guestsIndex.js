$(window).ready(function () {
    var cs_id = $("#control_structure_id").val();
    $.ajax({
        "type": 'get',
        "url": '/guests/components/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            var backup = data;
            $.each(data, function (idx, obj) {
                if(obj.father == null && obj.name != "Environment"){
                    $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td></tr>");
                    addChildren(obj.id,backup);
                }
            });
            $("#treeTable").treetable({
                expandable: true,
                initialState: "collapsed",
                clickableNodeNames: true,
                indent: 30
            });
        }
    });
    $.ajax({
        "type": 'get',
        "url": '/guests/connections/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                $("#connectionTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.sourceName + "</td><td>" + obj.targetName + "</td><td>" + obj.connectionType + "</td><td>" + obj.style + "</td></tr>");
                $.each(obj.labels, function (idx, label) {
                    $("#connectionTable").append("<tr data-tt-id=\"" + obj.id + "-l-" + label.id + "\" data-tt-parent-id=\"" + obj.id + "\"><td>" + label.label + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + "LABEL" + "</td></tr>");
                });
            });
            $("#connectionTable").treetable({
                expandable: true,
                initialState: "collapsed",
                clickableNodeNames: true,
                indent: 30
            });
        }
    });
    $.ajax({
        "type": 'get',
        "url": '/guests/components/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                if(obj.type == "Controller" || obj.type == "ControlledProcess"){
                    $.each(obj.variables, function (idx, variable) {
                        var id = variable.id;
                        $("#variableTable").append("<tr data-tt-id=\"" + variable.id + "\" data-tt-parent-id=\"" + " " + "\"><td>" + variable.name + "</td><td>" + obj.name + "</td><td>" + " " + "</td><td>" + " " + "</td></tr>");
                        $.each(variable.states, function (idx, state) {
                            $("#variableTable").append("<tr data-tt-id=\"" + variable.id + "-s-" + state.id + "\" data-tt-parent-id=\"" + variable.id + "\"><td>" + state.name + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + " " + "</td></tr>");
                        });
                    });
                }
            });
            $("#variableTable").treetable({
                expandable: true,
                initialState: "collapsed",
                clickableNodeNames: true,
                indent: 30
            });
        }
    });
});

function addChildren(id, backup){
   $.each(backup, function (idx, obj) {
        if(obj.father != null){
            if(obj.father.id == id){
                $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father.id + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td></tr>");
                addChildren(obj.id,backup);
            }
        }
   });
}

function collapseAllComponents(){
    $("#collapseAllComponentsButton").attr("onclick","expandAllComponents()");
    $("#collapseAllComponentsButton").text("Expand All");
    $("#treeTable").treetable("collapseAll");
}

function expandAllComponents(){
    $("#collapseAllComponentsButton").attr("onclick","collapseAllComponents()");
    $("#collapseAllComponentsButton").text("Collapse All");
    $("#treeTable").treetable("expandAll");
}

function collapseAllConnections(){
    $("#collapseAllConnectionsButton").attr("onclick","expandAllConnections()");
    $("#collapseAllConnectionsButton").text("Expand All");
    $("#connectionTable").treetable("collapseAll");
}

function expandAllConnections(){
    $("#collapseAllConnectionsButton").attr("onclick","collapseAllConnections()");
    $("#collapseAllConnectionsButton").text("Collapse All");
    $("#connectionTable").treetable("expandAll");
}

function collapseAllVariables(){
    $("#collapseAllVariablesButton").attr("onclick","expandAllVariables()");
    $("#collapseAllVariablesButton").text("Expand All");
    $("#variableTable").treetable("collapseAll");
}

function expandAllVariables(){
    $("#collapseAllVariablesButton").attr("onclick","collapseAllVariables()");
    $("#collapseAllVariablesButton").text("Collapse All");
    $("#variableTable").treetable("expandAll");
}