function addVariable(){
    var variable = {
        name: $("#variable-name").val(),
        componentId: $("#variable-component").val(),
    }

    $.ajax({
        url: '/variables',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(variable)
    });
}

function editVariable(id){

    variableToBeEdited = id;
    $.ajax({
        url: '/variables/'+ id,
        type: 'get',
        success: function (data) {
            $("#variable-edit-name").val(data.name);
        },
    });
}

function sendEditedVariable() {
    var variable = {
        name: $("#variable-edit-name").val(),
    }

    $.ajax({
        url: '/variables/' + variableToBeEdited,
        type: 'put',
        contentType: 'application/json',
        success: function () {
            location.reload();
        },
        data: JSON.stringify(variable)
    });
}

function loadVariableToBeDeleted(id){
    variableToBeDeleted = id;
    $.ajax({
            url: '/variables/'+ id,
            type: 'get',
            success: function (data) {
                 $("#variable_delete_name").text(data.name);
            },
        });
}

function deleteVariable(){
    $.ajax({
        url: '/variables/'+ variableToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}