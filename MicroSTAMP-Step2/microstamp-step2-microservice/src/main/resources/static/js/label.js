function loadConnectionToBeLabeled(id){
    connectionToBeLabeled = id;
}

function addLabel(){
    var label = {
        label: $("#label-name").val(),
        connectionId: connectionToBeLabeled,
    }

    $.ajax({
        url: '/labels',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(label)
    });
}

function loadLabelToBeEdited(id){
    labelToBeEdited = id;
    $.ajax({
        url: '/labels/'+ id,
        type: 'get',
        success: function (data) {
            $("#label-edit-name").val(data.label);
        },
    });
}

function editLabel() {
    var label = {
        label: $("#label-edit-name").val(),
    }

    $.ajax({
        url: '/labels/' + labelToBeEdited,
        type: 'put',
        contentType: 'application/json',
        success: function () {
            location.reload();
        },
        data: JSON.stringify(label)
    });
}

function loadLabelToBeDeleted(id){
    labelToBeDeleted = id;
    $.ajax({
            url: '/labels/'+ id,
            type: 'get',
            success: function (data) {
                 $("#label_delete_name").text(data.label);
            },
        });
}

function deleteLabel(){
    $.ajax({
        url: '/labels/'+ labelToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}