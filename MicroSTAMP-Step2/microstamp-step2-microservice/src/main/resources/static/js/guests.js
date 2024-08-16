$(window).ready(function () {
    $.ajax({
        "type": 'get',
        "url": 'guests/control-structures',
        "dataType": "json",
        "success": function (data) {
            $.each(data, function (idx, obj) {
                $("#csTable").append("<tr data-tt-id=\"" + obj.id + "\" data-tt-parent-id=\"" + obj.father + "\"><td>" + obj.name + "</td><td><button style='cursor: pointer; border-radius: 5px;' type='button' id=\"" + obj.id + "\" onclick=location.href=\"" + "/guests/" + obj.id + "\"><span class='fa fa-search' aria-hidden='true'></span></button></td></tr>");
            });
            $("#csTable").treetable({
                expandable: true,
                initialState: "expanded",
                clickableNodeNames: true,
                indent: 30
            });
        }
    });
});