function addImage(){
    var control_structure_id = $("#control_structure_id").val();
    var newFile = $("#image").prop("files")[0];
    var data = new FormData();
    data.append("file",newFile);
    $.ajax({
        url: '/images/control-structure/' + control_structure_id,
        type: 'POST',
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            location.reload();
        },
        data:data
    });
}

function loadImageToBeDeleted(id){
    imageToBeDeleted = id;
    $.ajax({
            url: '/images/'+ id,
            type: 'get',
            success: function (data) {
                 $("#image_delete_name").text(data.name);
            },
        });
}

function deleteImage(){
    $.ajax({
        url: '/images/'+ imageToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}