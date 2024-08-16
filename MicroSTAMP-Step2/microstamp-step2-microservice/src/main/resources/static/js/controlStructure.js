var controlStructureSelected;
var controlStructureToBeDeleted;
var lastOpenedModal;

$(window).ready(function () {
    var user_id = document.getElementById("user_id").innerText;
    $.ajax({
        "type": 'get',
        "url": '/control-structures/user/' + user_id,
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                $("#csTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.name + "</td><td><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#editControlStructureModal' onclick = loadEditControlStructure(this.id) type='button' id=\"" + obj.id + "\"><span class='fa fa-pencil' aria-hidden='true'></span></button><button style='cursor: pointer; border-radius: 5px;' data-toggle='modal' data-target='#confirmControlStructureDeleteModal' type='button' id=\"" + obj.id + "\" onclick = loadControlStructureToBeDeleted(this.id)><span class='fa fa-trash'></span></button><button style='cursor: pointer; border-radius: 5px;' type='button' id=\"" + obj.id + "\" onclick=location.href=\"" + obj.id + "\"><span class='fa fa-search' aria-hidden='true'></span></button></td></tr>");
            });
            $("#csTable").treetable({
                expandable: true,
                initialState: "expanded",
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
});

function addControlStructure() {
    var control_structure = {
        name: $("#control_structure-name").val(),
        userId: document.getElementById("user_id").innerText,
    }

    $.ajax({
        url: '/control-structures',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            location.reload();
        },
        data: JSON.stringify(control_structure)
    });

}

function loadEditControlStructure(id){
    controlStructureSelected = id;
    $.ajax({
    url: '/control-structures/'+ id,
    type: 'get',
    success: function (data) {
        $("#control_structure-edit-name").val(data.name);
    },
});
}

function editControlStructure() {
    var control_structure = {
        name: $("#control_structure-edit-name").val(),
    }

    $.ajax({
        url: '/control-structures/' + controlStructureSelected,
        type: 'put',
        contentType: 'application/json',
        success: function () {
            location.reload();
        },
        data: JSON.stringify(control_structure)
    });
}

function loadControlStructureToBeDeleted(id){
    controlStructureToBeDeleted = id;
    $.ajax({
        url: '/control-structures/'+ id,
        type: 'get',
        success: function (data) {
             $("#control_structure_delete_name").text(data.name);
        },
    });
}

function deleteControlStructure(){
    $.ajax({
        url: '/control-structures/'+ controlStructureToBeDeleted,
        type: 'delete',
        success: function () {
            location.reload();
        },
    });
}