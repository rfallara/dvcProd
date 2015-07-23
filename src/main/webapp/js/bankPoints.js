var maxBankablePoints = 0;
var maxToBankSpan = $("#maxToBank");
var bankDate = $('#bankDate');
var bankablePoints = $("#bankablePoints");
var btnBankPoints = $("#btnBankPoints");
var dialogBankedMessage = $('#dialog-banked-message');
var dialogBanked = $('#dialog-banked');
var dialogConfirm = $('#dialog-confirm');
var dialogConfirmIcon = $('#dialog-confirm-icon');


$( ".myDateField" ).datepicker({
    changeMonth: true,
    changeYear: true,
    showAnim: "fadeIn",
    showOtherMonths: true,
    selectOtherMonths: true,
    showOn: "button"
});

$(".myButton").button();

$( function() {
    bankablePoints.prop("disabled", true);
    btnBankPoints.prop("disabled", true);
    dialogConfirm.css("visibility", 'visible');
    dialogBanked.css('visibility', 'visible');
});

bankDate.on("change", function(){
    if ( Date.parse( bankDate.val() ) > 0 ){
        var x = new Date( bankDate.val() );
        bankDate.val( x.toLocaleDateString() );

        $.post(
            "BankPoints.do",
            { action : "getBankablePoints", bankDate : bankDate.val() },
            function(data){
                maxBankablePoints = data;
                maxToBankSpan.html(maxBankablePoints);
                if (maxBankablePoints > 0){
                    bankablePoints.val(data);
                    bankablePoints.prop("disabled", false);
                    btnBankPoints.prop("disabled", false);
                }
            }
        );
    } else if ( bankDate.val() === ""){
        maxToBankSpan.html("--");
        bankablePoints.val("");
        bankablePoints.prop("disabled", true);
        btnBankPoints.prop("disabled", true);
    } else {
        alert ("Invalid Date");
        bankDate.val("");
        maxToBankSpan.html("--");
        bankablePoints.val("");
        bankablePoints.prop("disabled", true);
        btnBankPoints.prop("disabled", true);
    }
});


bankablePoints.on("change", function() {
    if (
        (parseInt(bankablePoints.val()) > 0) &&
        (parseInt(bankablePoints.val()) <= parseInt(maxBankablePoints))
    ){
        btnBankPoints.prop("disabled", false);
    } else {
        btnBankPoints.prop("disabled", true);
    }

});

btnBankPoints.on("click", function() {
    var strConfirmMsg = "Are you sure you want to mark " + bankablePoints.val() +
        " points as banked with a banking date of " + $('#bankDate').val() + "?";
    $('#confirmMsg').html(strConfirmMsg);
    $("#dialog-confirm").dialog("open");
    return false;
});

$("#dialog-confirm").dialog({
    autoOpen : false,
    resizable : false,
    height : 160,
    modal : true,
    buttons : {
        "Mark Points as Banked" : function() {
            $(this).dialog("close");
            $.post(
            "BankPoints.do",
            { action : "markPointsAsBanked", bankDate : bankDate.val(), bankCount: bankablePoints.val() },
            function(data){
		var myResult = JSON.parse(data);
		
		if (myResult["status"] === "OK"){
		    dialogConfirmIcon.attr("class", "ui-icon ui-icon-check");
		} else {
		    dialogConfirmIcon.attr("class", "ui-icon ui-icon-alert");
		}
		
		dialogBankedMessage.html(myResult["msg"]);
		dialogBanked.dialog("open");
            }
        );
        },
        Cancel : function() {
            $(this).dialog("close");
        }
    }
});

$('#dialog-banked').dialog({
    autoOpen : false,
    resizable : false,
    height : 140,
    modal : true,
    buttons : {
        Close : function() {
            $(this).dialog("close");
	    window.location.href = "BankPoints.do";
        }
    }
});