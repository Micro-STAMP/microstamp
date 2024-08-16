function loadComponentToResponsibility(id){
    componentToResponsibility = id;
}

function addResponsibility(){
    var responsibility = {
        responsibility: $("#responsibility-name").val(),
        systemSafetyConstraintAssociated: $("#responsibility-ssc").val(),
        componentId: componentToResponsibility,
    }

    $.ajax({
        url: '/responsibilities',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(responsibility)
    });
}

function loadResponsibilityToBeEdited(id){
    responsibilityToBeEdited = id;
    $.ajax({
        url: '/responsibilities/'+ id,
        type: 'get',
        success: function (data) {
            $("#responsibility-edit-name").val(data.responsibility);
            $("#responsibility-edit-ssc").val(data.systemSafetyConstraintAssociated);
        },
    });
}

function sendEditedResponsibility() {
    var responsibility = {
        responsibility: $("#responsibility-edit-name").val(),
        systemSafetyConstraintAssociated: $("#responsibility-edit-ssc").val(),
    }

    $.ajax({
        url: '/responsibilities/' + responsibilityToBeEdited,
        type: 'put',
        contentType: 'application/json',
        success: function () {
            location.reload();
        },
        data: JSON.stringify(responsibility)
    });
}

function loadResponsibilityToBeDeleted(id){
    responsibilityToBeDeleted = id;
    $.ajax({
            url: '/responsibilities/'+ id,
            type: 'get',
            success: function (data) {
                 $("#responsibility_delete_name").text(data.responsibility);
            },
        });
}

function deleteResponsibility(){
    $.ajax({
        url: '/responsibilities/'+ responsibilityToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}