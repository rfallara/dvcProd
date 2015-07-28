/**
 * 
 */

var addTripForm = $("#addTripForm");
var dialog;

dialog = $("#addNewTripForm").dialog(
	{
	    autoOpen: false,
	    height: 420,
	    width: 450,
	    modal: true,
	    resizable: false,
	    buttons: [
		{
		    text: "Create Trip",
		    click: function () {
			myValidateForm();
		    }
		},
		{
		    text: "Reset",
		    click: function () {
			myClearError(), document.getElementById(
				"addTripForm").reset();
		    }
		},
		{
		    text: "Close",
		    click: function () {
			myClearError(), document.getElementById(
				"addTripForm").reset(), $(this).dialog(
				"close");
		    }
		}]

	});

$("#createTrip").button().on("click", function () {
    dialog.dialog("open");
});

$(".myButton").button();

var formErrors = $("#formErrors"), addDateBooked = $("#addDateBooked"), addDateCheckIn = $("#addDateCheckIn"), addDateCheckOut = $("#addDateCheckOut"), addOwner = $("#addOwner"), addBookableRoom = $("#addBookableRoom"), addNotes = $("#addNotes"), addPointsNeeded = $("#addPointsNeeded"), allFields = $(
	[]).add(addDateBooked).add(addDateCheckIn).add(addDateCheckOut).add(
	addOwner).add(addBookableRoom).add(addNotes).add(addPointsNeeded);

function myUpdateDate(x, y, z) {

    var newDate = new Date();
    newDate = (y.datepicker("getDate"));

    newDate.setDate(newDate.getDate() + z);

    if (y.datepicker("getDate") && !x.datepicker("getDate")) {
	x.datepicker("setDate", newDate);
	x.datepicker("refresh");
    }
}

function myValidateForm() {
    myClearError();
    var valid = true;

    var bookDate = new Date(addDateBooked.val());

    if (!(bookDate.getFullYear() > 2012)) {
	formErrors.append("Booked date required<br>");
	addDateBooked.addClass("ui-state-error");
	valid = false;
    }

    if ((new Date(addDateCheckIn.val()) < new Date(addDateBooked.val()))
	    || addDateCheckIn.val() === "") {
	formErrors
		.append("Check In date must be greater or equeal to booked date<br>");
	addDateCheckIn.addClass("ui-state-error");
	valid = false;
    }

    if ((new Date(addDateCheckIn.val()) >= new Date(addDateCheckOut.val()))
	    || addDateCheckOut.val() === "") {
	formErrors
		.append("Check Out date must be greater than Check In date<br>");
	addDateCheckOut.addClass("ui-state-error");
	valid = false;
    }

    if (addOwner.val() === "0") {
	formErrors.append("Select a trip owner<br>");
	addOwner.addClass("ui-state-error");
	valid = false;
    }

    if (addBookableRoom.val() === "0") {
	formErrors.append("Select a Resort and Room<br>");
	addBookableRoom.addClass("ui-state-error");
	valid = false;
    }

    var x = parseInt(addPointsNeeded.val());

    if (!(x > 0 && x < 10000)) {
	formErrors.append("Points Needed from 1 to 9999<br>");
	addPointsNeeded.addClass("ui-state-error");
	valid = false;
    }

    if (!valid) {
	formErrors.addClass("ui-state-highlight");
	setTimeout(function () {
	    formErrors.removeClass("ui-state-highlight", 1500);
	}, 5000);
    } else {
	addTripForm.submit();
    }

}

function myClearError() {
    formErrors.text("");
    allFields.removeClass("ui-state-error");
}

$(".dateField").datepicker({
    changeMonth: true,
    changeYear: true,
    showAnim: "fadeIn",
    showOtherMonths: true,
    selectOtherMonths: true,
    showOn: "button"
});

$("#addTripButton").button({
});



$(".deleteTripButton").button({
});

$("#dialog-confirm").dialog({
    autoOpen: false,
    resizable: false,
    height: 140,
    modal: true,
    buttons: {
	"Confirm Delete Trip": function () {
	    $(this).dialog("close");
	    window.location.href = "ManageTrips.do?deleteID=" + deleteTripId;
	},
	Cancel: function () {
	    deleteTripId = 0;
	    $(this).dialog("close");
	}
    }
});

var deleteTripId = 0;

function myConfirmDelete(x) {
    deleteTripId = x;
    $("#dialog-confirm").dialog("open");
}