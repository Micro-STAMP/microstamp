$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
    var modalMessage = "An unexpected error occurred.";

    try {
        const errorMessage = JSON.parse(jqxhr.responseText);
        if (errorMessage.errors.length > 0) {
            modalMessage = errorMessage.errors
                .map(function(error) {
                     return error.message;
                }).join('<br>');
        }
        showErrorModal(modalMessage);
    } catch (e) {
        showErrorModal(modalMessage);
    }
});

function showErrorModal(message) {
    $(".modal").modal("hide");
    $("#errorModal .modal-body").html(message);
    $("#errorModal").modal("show");
}

