/**
 * 
 */
function signInCallback(authResult) {
	console.log("calling signInCallback function");
	console.log(authResult);
	if (authResult['code']) {

		// Hide the sign-in button now that the user is authorized, for example:
		// $('#signinButton').attr('style', 'display: none');

		// Send the code to the server
		var x = 'GoogleAuth.do?state=' + $('#CSFRToken').html();
		$.ajax({
			type : 'POST',
			url : x,
			contentType : 'application/octet-stream; charset=utf-8',
			success : function(result) {
				// Handle or verify the server response if necessary.
				var x = $('#CSFRToken').html();
				if (result == "VALID") {
					console.log(result);
					console.log(authResult);
					window.location = "index.jsp";
				}

				if (result == "INVALID_DOMAIN") {
					alert("User must be part of the fallara.net domain.");
					gapi.auth.signOut();
				}

			},
			processData : false,
			data : authResult['code'],

			statusCode : {
				500 : function() {
					alert("ERROR OCCURED");
				},
			}

		});
	} else if (authResult['error']) {
		// There was an error.
		// Possible error codes:
		// "access_denied" - User denied access to your app
		// "immediate_failed" - Could not automatially log in the user
		// console.log('There was an error: ' + authResult['error']);
	}
}

function Logout() {
	gapi.auth.signOut();
	window.location.href = "Logout.do";
}
