function addComponent(){
    if(document.getElementById('isVisible').checked) {
        var checkedValue = new Boolean(1);
    }else{
        var checkedValue = new Boolean(0);
    }

    var component = {
        name: $("#component-name").val(),
        fatherId: $("#component-father").val(),
        border: $("#component-border").val(),
        isVisible: checkedValue,
        controlStructureId: $("#control_structure_id").val(),
    }

    if($("#component-type").val() == "controlled-process")
        var componentType = $("#component-type").val() + "es";
    else var componentType = $("#component-type").val() + "s";

    $.ajax({
        url: '/'+ componentType,
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(component)
    });
}

function editComponent(id){
    componentSelected = id;
    $.ajax({
        url: '/components/'+ id,
        type: 'get',
        success: function (data) {
            $("#component-edit-name").val(data.name);
            switch(data.type){
                case "Controller":
                    $("#component-edit-type").val("controller");
                    break;
                case "Actuator":
                    $("#component-edit-type").val("actuator");
                    break;
                case "Sensor":
                    $("#component-edit-type").val("sensor");
                    break;
                case "ControlledProcess":
                    $("#component-edit-type").val("controlled-process");
                    break;
            }
            $("#component-edit-border").val(data.border);
            if(data.father == null)
                $("#component-edit-father").val("null");
            else
                $("#component-edit-father").val(data.father.id);
            if(data.isVisible == true)
                $("#editIsVisible").prop("checked", true);
            else
                $("#editIsNotVisible").prop("checked", true);

            componentSelectedOriginalType = $("#component-edit-type").val();
        },
    });
}

function sendEditedComponent(){
    if(document.getElementById('editIsVisible').checked) {
        var checkedValue = new Boolean(1);
    }else{
        var checkedValue = new Boolean(0);
    }

    var type = $("#component-edit-type").val();
    if(type == "controlled-process")
        type = "ControlledProcess"

    type = type.charAt(0).toUpperCase() + type.slice(1);

    var component = {
        name: $("#component-edit-name").val(),
        fatherId: $("#component-edit-father").val(),
        border: $("#component-edit-border").val(),
        isVisible: checkedValue,
        controlStructureId: $("#control_structure_id").val(),
        type: type,
    }

    var componentType = componentSelectedOriginalType + "s";

    if(componentSelectedOriginalType == "controlled-process")
        componentType = componentSelectedOriginalType + "es";

    var father_id = $("#component-edit-father").val();

    $.ajax({
        url: '/'+ componentType + '/' + componentSelected,
        type: 'put',
        contentType: 'application/json',
        data: JSON.stringify(component),
        success: function () {
            location.reload();
        },
    });
}

function loadComponentsAndConnectionsToBeDeleted(id){
    componentToBeDeleted = id;
    $("#itemsToBeDeleted").css("display","grid");
    $("#itemsToBeDeletedSpan").css("display","flex");
    $("#componentsToBeDeleted").css("display","grid");
    $("#connectionsToBeDeleted").css("display","grid");
    $("#variablesToBeDeleted").css("display","grid");
    $("#componentsToBeDeletedLi").css("display","");
    $("#connectionsToBeDeletedLi").css("display","");
    $("#variablesToBeDeletedLi").css("display","");

    $.ajax({
        url: '/components/'+ id,
        type: 'get',
        success: function (data) {
             $("#componentToBeDeletedName").text(data.name);
        },
    });

    $("#componentsToBeDeleted").empty();
    $("#connectionsToBeDeleted").empty();
    $("#variablesToBeDeleted").empty();

    $.ajax({
        url: '/components/'+ id + '/dependencies',
        type: 'get',
        success: function (data) {
            var haveComponents = false;
            var haveConnections = false;
            var haveVariables = false;

            if (data.components.length > 0) {
                data.components.map(function(component) {
                    $("#componentsToBeDeleted").append("<li>" + component.name + "</li>");
                });
                haveComponents = true;
            }

            if (data.connections.length > 0) {
                data.connections.map(function(connection) {
                    $("#connectionsToBeDeleted").append("<li>" + connection.source.name + " ---> " + connection.target.name + "</li>");
                });
                haveConnections = true;
            }

            if (data.variables.length > 0) {
                data.variables.map(function(variable) {
                    $("#variablesToBeDeleted").append("<li>" + variable.name + "</li>");
                });
                haveVariables = true;
            }

            if(!haveComponents && !haveConnections && !haveVariables){
                $("#itemsToBeDeleted").css("display","none");
                $("#itemsToBeDeletedSpan").css("display","none");
            }

            if(!haveComponents){
                $("#componentsToBeDeleted").css("display","none");
                $("#componentsToBeDeletedLi").css("display","none");
            }

            if(!haveConnections){
                $("#connectionsToBeDeleted").css("display","none");
                $("#connectionsToBeDeletedLi").css("display","none");
            }

            if(!haveVariables){
                $("#variablesToBeDeleted").css("display","none");
                $("#variablesToBeDeletedLi").css("display","none");
            }
        },
    });
}

function deleteComponent(){
    $.ajax({
        url: '/components/'+ componentToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}