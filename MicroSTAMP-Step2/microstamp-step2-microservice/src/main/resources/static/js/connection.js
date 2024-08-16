function addConnection() {

    var connection = {
        sourceId: $("#connection-source").val(),
        targetId: $("#connection-target").val(),
        connectionType: $("#connection-type").val(),
        style: $("#connection-style").val(),
        controlStructureId: $("#control_structure_id").val(),
    }

    $.ajax({
        url: '/connections',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(connection)
    });
}

function editConnection(id){
    connectionSelected = id;
     $.ajax({
        url: '/connections/'+ id,
        type: 'get',
        success: function (data) {
            $("#connection-edit-style").val(data.style);
            $("#connection-edit-source").val(data.source.id);
            $("#connection-edit-target").val(data.target.id);

            validateEditConnectionType();
            $("#connection-edit-type").val(data.connectionType);
        },
    });
}

function sendEditedConnection(){
    var connection = {
        sourceId: $("#connection-edit-source").val(),
        targetId: $("#connection-edit-target").val(),
        connectionType: $("#connection-edit-type").val(),
        style: $("#connection-edit-style").val(),
        controlStructureId: $("#control_structure_id").val(),
    }

    $.ajax({
        url: '/connections/' + connectionSelected,
        type: 'put',
        contentType: 'application/json',
        data: JSON.stringify(connection),
        success: function () {
            location.reload();
        },
    });
}

function loadConnectionToBeDeleted(id){
    connectionToBeDeleted = id;
    $.ajax({
        url: '/connections/'+ id,
        type: 'get',
        success: function (data) {
             $("#connectionToBeDeletedType").text(data.connectionType);
             $("#connectionToBeDeletedSourceName").text(data.source.name);
             $("#connectionToBeDeletedTargetName").text(data.target.name);
        },
    });
    $("#labelsToBeDeleted").empty();
    $.ajax({
        url: '/labels/connection/'+ id,
        type: 'get',
        success: function (data) {
             $.each(data, function (idx, obj) {
                $("#labelsToBeDeleted").append("<li>" + obj.label + "</li>");
            });
        },
    });
}

function deleteConnection(){
    $.ajax({
        url: '/connections/'+ connectionToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}

function validateConnectionType() {
    var source = $("#connection-source option:selected").text();
    var target = $("#connection-target option:selected").text();
    var connectionType = $("#connection-type");

    connectionType.empty();

    updateConnectionType(source, target, connectionType)
}

function validateEditConnectionType() {
    var source = $("#connection-edit-source option:selected").text();
    var target = $("#connection-edit-target option:selected").text();
    var connectionType = $("#connection-edit-type");

    connectionType.empty();

    updateConnectionType(source, target, connectionType)
}

function updateConnectionType(source, target, connectionType){
    if (source === "Environment") {
        connectionType.append(new Option("DISTURBANCE", "DISTURBANCE"));
        connectionType.append(new Option("PROCESS_INPUT", "PROCESS_INPUT"));
    } else if (target === "Environment") {
        connectionType.append(new Option("PROCESS_OUTPUT", "PROCESS_OUTPUT"));
    } else {
        connectionType.append(new Option("CONTROL_ACTION", "CONTROL_ACTION"));
        connectionType.append(new Option("FEEDBACK", "FEEDBACK"));
        connectionType.append(new Option("COMMUNICATION_CHANNEL", "COMMUNICATION_CHANNEL"));
    }
}