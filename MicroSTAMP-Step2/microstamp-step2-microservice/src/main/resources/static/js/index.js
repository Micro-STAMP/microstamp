var componentSelected;
var componentSelectedOriginalType;
var componentToBeDeleted;
var connectionSelected;
var connectionToBeDeleted;
var connectionToBeLabeled;
var labelToBeEdited;
var labelToBeDeleted;
var variableToBeEdited;
var variableToBeDeleted;
var variableToBeStated;
var stateToBeEdited;
var stateToBeDeleted;
var componentToResponsibility;
var responsibilityToBeEdited;
var responsibilityToBeDeleted;
var imageToBeDeleted;
var lastOpenedModal;

$(window).ready(function () {
    var cs_id = $("#control_structure_id").val();

    $.ajax({
        "type": 'get',
        "url": '/components/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            var backup = data;
            $.each(data, function (idx, obj) {
                if(obj.father == null && obj.name != "Environment"){
                    if(obj.type == "Controller" || obj.type == "ControlledProcess"){
                        $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editComponentModal' onclick = editComponent(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmComponentDeleteModal' onclick = loadComponentsAndConnectionsToBeDeleted(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-trash'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#addResponsibilityModal' onclick = loadComponentToResponsibility(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-plus'></span></button></td></tr>");
                        if(obj.responsibilities.length > 0){
                            $("#treeTable").append("<tr data-tt-id=\"" + obj.id +"-r" + "\" data-tt-parent-id=\"" + obj.id + "\"><td>" + "Responsibilities" + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + "System Safety Constraint Associated" + "</td><td>" + " " + "</td></tr>");
                            $.each(obj.responsibilities, function (idx, responsibility) {
                                $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "-r-" + responsibility.id + "\" data-tt-parent-id=\"" + obj.id + "-r" + "\"><td>" + responsibility.responsibility + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + responsibility.systemSafetyConstraintAssociated + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editResponsibilityModal' onclick = loadResponsibilityToBeEdited(this.id) type='button' id=\"" + responsibility.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmResponsibilityDeleteModal' onclick = loadResponsibilityToBeDeleted(this.id) type='button' id=\"" + responsibility.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
                            });
                        }
                        addChildren(obj.id,backup);
                    }else{
                        $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editComponentModal' onclick = editComponent(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmComponentDeleteModal' onclick = loadComponentsAndConnectionsToBeDeleted(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
                        addChildren(obj.id,backup);
                    }
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
        "url": '/connections/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                $("#connectionTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.sourceName + "</td><td>" + obj.targetName + "</td><td>" + obj.connectionType + "</td><td>" + obj.style + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editConnectionModal' onclick = editConnection(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmConnectionDeleteModal' onclick = loadConnectionToBeDeleted(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-trash'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#addLabelModal' onclick = loadConnectionToBeLabeled(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-plus'></span></button></td></tr>");
                $.each(obj.labels, function (idx, label) {
                    $("#connectionTable").append("<tr data-tt-id=\"" + obj.id + "-l-" + label.id + "\" data-tt-parent-id=\"" + obj.id + "\"><td>" + label.label + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + "LABEL" + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editLabelModal' onclick = loadLabelToBeEdited(this.id) type='button' id=\"" + label.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmLabelDeleteModal' onclick = loadLabelToBeDeleted(this.id) type='button' id=\"" + label.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
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
        "url": '/components/control-structure/' + cs_id,
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                if(obj.type == "Controller" || obj.type == "ControlledProcess"){
                    $.each(obj.variables, function (idx, variable) {
                        var id = variable.id;
                        $("#variableTable").append("<tr data-tt-id=\"" + variable.id + "\" data-tt-parent-id=\"" + " " + "\"><td>" + variable.name + "</td><td>" + obj.name + "</td><td>" + " " + "</td><td>" + " " + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editVariableModal' onclick = editVariable(id) type='button' id=\"" + variable.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmVariableDeleteModal' onclick = loadVariableToBeDeleted(id) type='button' id=\"" + variable.id + "\"><span class='fa fa-trash'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#addStateModal' onclick = loadVariableToBeStated(id) type='button' id=\"" + variable.id + "\"><span class='fa fa-plus'></span></button></td></tr>");
                        $.each(variable.states, function (idx, state) {
                            $("#variableTable").append("<tr data-tt-id=\"" + variable.id + "-s-" + state.id + "\" data-tt-parent-id=\"" + variable.id + "\"><td>" + state.name + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + " " + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editStateModal' onclick = loadStateToBeEdited(this.id) type='button' id=\"" + state.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmStateDeleteModal' onclick = loadStateToBeDeleted(this.id) type='button' id=\"" + state.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
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

    $(".modal").on("show.bs.modal", function (e) {
        if($(this).attr('id') !== "errorModal"){
            lastOpenedModal = $(this).attr('id');
        }
    });

    $("#errorModal").on("hidden.bs.modal", function () {
        if (lastOpenedModal) {
            $("#" + lastOpenedModal).modal("show");
        }
    });

     $('#connection-source, #connection-target').change(validateConnectionType);
     $('#connection-edit-source, #connection-edit-target').change(validateEditConnectionType);
     validateConnectionType();
});

function addChildren(id, backup){
   $.each(backup, function (idx, obj) {
        if(obj.father != null){
            if(obj.father.id == id){
                if(obj.type == "Controller" || obj.type == "ControlledProcess"){
                    $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father.id + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editComponentModal' onclick = editComponent(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmComponentDeleteModal' onclick = loadComponentsAndConnectionsToBeDeleted(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-trash'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#addResponsibilityModal' onclick = loadComponentToResponsibility(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-plus'></span></button></td></tr>");
                    if(obj.responsibilities.length > 0){
                            $("#treeTable").append("<tr data-tt-id=\"" + obj.id +"-r" + "\" data-tt-parent-id=\"" + obj.id + "\"><td>" + "Responsibilities" + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + "System Safety Constraint Associated" + "</td><td>" + " " + "</td></tr>");
                            $.each(obj.responsibilities, function (idx, responsibility) {
                                $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "-r-" + responsibility.id + "\" data-tt-parent-id=\"" + obj.id + "-r" + "\"><td>" + responsibility.responsibility + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + responsibility.systemSafetyConstraintAssociated + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editResponsibilityModal' onclick = loadResponsibilityToBeEdited(this.id) type='button' id=\"" + responsibility.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmResponsibilityDeleteModal' onclick = loadResponsibilityToBeDeleted(this.id) type='button' id=\"" + responsibility.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
                            });
                        }
                    addChildren(obj.id,backup);
                }else{
                    $("#treeTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father.id + "\"><td>" + obj.name + "</td><td>" + obj.border + "</td><td>" + obj.isVisible + "</td><td>" + obj.type + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editComponentModal' onclick = editComponent(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmComponentDeleteModal' onclick = loadComponentsAndConnectionsToBeDeleted(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-trash'></span></button></td></tr>");
                    addChildren(obj.id,backup);
                }
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
