<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .ui-menu {
        width: 150px;
    }
</style>

<script src="js/googleAuth.js" type="text/javascript"></script>
<div style="visibility: hidden">
    <span id="CSFRToken"><c:out value="${sessionScope.state}"></c:out></span>
    </div>

    <div class="ui-widget ui-widget-header">

	<H1>DVC Vacation Points Management <span style="color: red;"><c:out value="${sessionScope.headerNameTag}"/></span></H1>

	<table style="width: 100%">
	    <tr>
		<td style="vertical-align: bottom">
		    <div id="mainMenuBar">
			<ul id='menu' class='ui-widget-header'>

			</ul>
		    </div>
		</td>
	    <c:if test="${sessionScope.token != null}">
		<td style="text-align: center">
		    Actual DVC Points [ 
		    Banked:<c:out value="${sessionScope.loggedInUser.bankedActualPoints}"></c:out>&nbsp;
		    Current:<c:out value="${sessionScope.loggedInUser.currentActualPoints}"></c:out>
		    <c:if test="${sessionScope.loggedInUser.currentBankedActualPoints > 0}">
			(<c:out value="${sessionScope.loggedInUser.currentBankedActualPoints}"></c:out> Banked)&nbsp;
		    </c:if>
		    Borrow:<c:out value="${sessionScope.loggedInUser.borrowActualPoints}"></c:out>&nbsp;
			]
		    </td>
	    </c:if>    
            <td align="right">
                <div id="signinButton">
                    <span class="g-signin"
                          data-scope="https://www.googleapis.com/auth/userinfo.email"
                          data-clientid="624350122436-f3h0e16docp6p0ivhstiq5r7oi0m5rf1.apps.googleusercontent.com"
                          data-redirecturi="postmessage" data-accesstype="offline"
                          data-prompt="login"
                          data-cookiepolicy="single_host_origin"
                          data-callback="signInCallback"> </span>
                </div> 
		<c:if test="${sessionScope.token != null}">
		    <c:if test="${sessionScope.loggedInUser.accessLevel == 7}">
			<input type="checkbox" id="enableOverrideUser"/>Override User:
			<select id="overrideUser" disabled>
			    <option value="0">---Select User---</option>
			    <c:forEach var="user" items="${sessionScope.loggedInUser.allOwnerList}">
				<option value="${user.ownerId}" >${user.ownerName}</option>
			    </c:forEach>
			</select>
			<br>
		    </c:if>
		    <c:out value="${sessionScope.loggedInUser.dvcOwner.ownerName}"></c:out>
			<br>
			Available [ 
			Banked:<c:out value="${sessionScope.loggedInUser.bankedPersonalPoints}"></c:out>&nbsp;
		    Current:<c:out value="${sessionScope.loggedInUser.currentPersonalPoints}"></c:out>&nbsp;
		    Borrow:<c:out value="${sessionScope.loggedInUser.borrowPersonalPoints}"></c:out>&nbsp;
			]
			<br>
			<a href="javascript:Logout()">LOGOUT</a>
		</c:if>
	    </td>
        </tr>
    </table>
</div>

<c:if test="${sessionScope.token != null}">
    <script type="text/javascript">
	$(function () {

	    //$('#signinButton').hide();

	    var mainMenuBar = $("#mainMenuBar");
	    var menu = $("#menu");
	    var menuManage = $("#menuManage");
	    menu
		    .append("<li id='menuManage'>Manage"
			    + "<ul>"
			    + "<li onclick='location.href=\"ManageTrips.do\"'>Trips</li>"
			    + "<li onclick='location.href=\"BankPoints.do\"'>Banked Points</li> "
			    + "<li>Rooms"
			    + "<ul>"
			    + "<li onclick='location.href=\"ManageResorts.do\"'>Resorts</li>"
			    + "<li onclick='location.href=\"ManageRoomTypes.do\"'>Room Types</li>"
			    + "<li onclick='location.href=\"ManageBookableRooms.do\"'>Bookable Rooms</li>"
			    + "</ul>" + "</li>" + "</ul>" + "</li>");

	    $("#menu").menu({
		position: {
		    my: "left top",
		    at: "left+5 bottom"
		},
		icons: {
		    submenu: "ui-icon-circle-triangle-s"
		}
	    });
	});
    </script>
    <c:if test="${sessionScope.loggedInUser.accessLevel == 7}">
	<script src="js/adminUser.js" type="text/javascript"></script>
    </c:if>
</c:if>
