/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('#enableOverrideUser').on('change', function () {
    if ($('#enableOverrideUser').prop("checked")) {
	$('#overrideUser').prop("disabled", false);
    } else {
	$('#overrideUser').prop("disabled", true);
    }
});

$('#overrideUser').on('change', function () {
    $.post(
	"AdminOperations.do",
	{action: "overrideUser", newOwnerId: $('#overrideUser').val()},
	function(data) {
		window.location.href = "index.jsp";
	    }
    );
});


