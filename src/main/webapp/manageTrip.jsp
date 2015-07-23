<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <%@ include file="head-includes.jsp"%>

        <title>Manage Trips</title>
    </head>

    <body>
        <%@ include file="header.jsp"%>

        <div style="margin: 50px">

            <div id="sql-operation-results">
                <c:out value="${requestScope.sqlError }"></c:out>

                <c:if test="${requestScope.createdTripDetails.newTripId == -1}">
                    There was an error booking this trip: <c:out
                        value="${requestScope.createdTripDetails.createTripError }"></c:out>
                        <br>
                        Banked Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countBankedPersonalPoints }"></c:out>
                        <br>
                        Current Year Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countCurrentPersonalPoints }"></c:out>
                        <br>
                        Borrowed Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countBorrowPersonalPoints }"></c:out>
                        <br>
                </c:if>

                <c:if test="${requestScope.createdTripDetails.newTripId > 0}">
                    Trip was booked successfully!!!<br>
                    New Trip ID: <c:out
                        value="${requestScope.createdTripDetails.newTripId }"></c:out>
                        <br>
                        Banked Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countBankedPersonalPoints }"></c:out>
                        <br>
                        Current Year Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countCurrentPersonalPoints }"></c:out>
                        <br>
                        Borrowed Personal Points: <c:out
                        value="${requestScope.createdTripDetails.countBorrowPersonalPoints }"></c:out>
                        <br>
                        Banked Actual Points: <c:out
                        value="${requestScope.createdTripDetails.countBankedActualPoints }"></c:out>
                        <br>
                        Current Year Actual Points: <c:out
                        value="${requestScope.createdTripDetails.countCurrentActualPoints }"></c:out>
                        <br>
                        Borrowed Actual Points: <c:out
                        value="${requestScope.createdTripDetails.countBorrowActualPoints }"></c:out>
                        <br>
                </c:if>
            </div>

            <div id="all-trips" class="ui-widget">
                <input type="button" value="Add Trip" id="createTrip"> <br>
                <table class="ui-widget ui-widget-content">
                    <thead class="ui-widget-header">
                    <td>Booked</td>
                    <td>Check-In</td>
                    <td>Check-Out</td>
                    <td>Owner</td>
                    <td>Resort</td>
                    <td>Room</td>
                    <td>Notes</td>
                    <td>Points</td>
                    <td></td>
                    </thead>
                    <tbody>
                    	<c:out value="${sessionScope.tripArray}"></c:out>
                        <c:forEach var="myTrip" items="${sessionScope.tripArray}">
                            <tr>
                                <td>${myTrip.bookedDate}</td>
                                <td>${myTrip.checkInDate}</td>
                                <td>${myTrip.checkOutDate}</td>
                                <td>${myTrip.dOwner.ownerName}</td>
                                <td>${myTrip.br.resort.resortName}</td>
                                <td>${myTrip.br.roomType.roomTypeDesc}</td>
                                <td>${myTrip.notes}</td>
                                <td>${myTrip.pointsNeeded}</td>
                                <td><input type="button" value="Delete Trip" id="deleteTrip"
                                           class="deleteTripButton"
                                           onclick="myConfirmDelete(${myTrip.tripId})"></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div id="addNewTripForm" title="Create a New Trip">
                <form method="post" id="addTripForm" name="addTripForm"
                      action="ManageTrips.do">
                    <table>
                        <tr>
                            <td>Booked</td>
                            <td><input name="addDateBooked" id="addDateBooked"
                                       class="dateField ui-widget-content ui-corner-all"></td>
                        </tr>
                        <tr>
                            <td>Check IN</td>
                            <td><input name="addDateCheckIn" id="addDateCheckIn"
                                       class="dateField ui-widget-content ui-corner-all"
                                       onfocus="myUpdateDate($('#addDateCheckIn'), $('#addDateBooked'), 0)"></td>
                        </tr>
                        <tr>
                            <td>Check OUT</td>
                            <td><input name="addDateCheckOut" id="addDateCheckOut"
                                       class="dateField text ui-widget-content ui-corner-all"
                                       onfocus="myUpdateDate($('#addDateCheckOut'), $('#addDateCheckIn'), 7)"></td>
                        </tr>
                        <tr>
                            <td>Owner</td>
                            <td><select name="addOwner" id="addOwner"
                                        class="mySelector ui-widget-content">
                                    <option value="0">---Owner---</option>
                                    <c:forEach var="dvcOwner" items="${sessionScope.dvcOwnerArray }">
                                        <option value="${dvcOwner.ownerId }">${dvcOwner.ownerName }</option>
                                    </c:forEach>
                                </select></td>
                        </tr>
                        <tr>
                            <td>Resort | Room</td>
                            <td><select name="addBookableRoom" id="addBookableRoom"
                                        class="mySelector ui-widget-content">
                                    <option value="0">---Resort | Room---</option>
                                    <c:forEach var="br" items="${sessionScope.bookableRoomArray }">
                                        <option value="${br.brId }">${br.resort.resortName }|${br.roomType.roomTypeDesc }</option>
                                    </c:forEach>
                                </select></td>
                        </tr>
                        <tr>
                            <td>Notes</td>
                            <td><textarea name="addNotes" id="addNotes"
                                          style="height: 60px; width: 299px;"
                                          class="text ui-widget-content ui-corner-all"></textarea></td>
                        </tr>
                        <tr>
                            <td>Points Needed</td>
                            <td><input name="addPointsNeeded" id="addPointsNeeded"
                                       class="text ui-widget-content ui-corner-all"></td>
                        </tr>
                        <tr>
                            <td><input type="hidden" name="action" value="addTrip"></td>
                            <td></td>
                        </tr>
                    </table>
                </form>
                <p id="formErrors"></p>
            </div>

            <div id="dialog-confirm" title="Confirm Delete Trip">
                <p>
                    <span class="ui-icon ui-icon-alert"
                          style="float: left; margin: 0 7px 20px 0;"></span> Are you sure you
                    want to permanently delete this trip?
                </p>
            </div>

        </div>

        <script src="js/manageTrip.js"></script>

    </body>
</html>