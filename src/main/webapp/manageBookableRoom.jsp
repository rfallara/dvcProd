<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

    <head>
        <%@ include file="head-includes.jsp"%>
        <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
        <title>Manager Bookable Rooms</title>
    </head>

    <body>

        <%@ include file="header.jsp"%>

        <div style="margin: 50px">
            <div>
                <c:out value="${requestScope.sqlError }"></c:out>
                </div>
                <br>
                <form method="post" action="ManageBookableRooms.do">
                    <table class="ui-widget ui-widget-content">
                        <thead class="ui-widget-header">
                            <tr>
                                <td>Resort</td>
                                <td>Room Type</td>
                                <td></td>
                            </tr>
                        </thead>

                    <c:forEach var="br" items="${sessionScope.bookableRoomArray }">
                        <tr>
                            <td>${br.resort.resortName }</td>
                            <td>${br.roomType.roomTypeDesc }</td>
                            <td><a href='ManageBookableRooms.do?deleteID=${br.brId}'>Delete</a></td>
                        </tr>
                    </c:forEach>

                    <tr>
                        <td><input type="hidden" name="action" value="addBookableRoom">
                            <select name="addResortId">
                                <option value="0">---Resort---</option>
                                <c:forEach var="resort" items="${sessionScope.resortArray}">
                                    <option value=${resort.resortId}>${resort.resortName}</option>
                                </c:forEach>
                            </select></td>
                        <td><select name="addRoomTypeId">
                                <option value="0">---Room Type---</option>
                                <c:forEach var="rt" items="${sessionScope.roomTypeArray}">
                                    <option value=${rt.rtId }>${rt.roomTypeDesc}</option>
                                </c:forEach>
                            </select></td>
                        <td><input type="submit" value="Add New"></td>

                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>