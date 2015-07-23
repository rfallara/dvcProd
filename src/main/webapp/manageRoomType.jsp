<%@ page language="java" contentType="text/html; charset=US-ASCII"
	import="net.fallara.*" pageEncoding="US-ASCII"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<%@ include file="head-includes.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Manager Room Types</title>
</head>

<body>
	<%@ include file="header.jsp"%>

	<div style="margin: 50px">
		<div>
			<c:out value="${requestScope.sqlError }"></c:out>
		</div>
		<br>
		<table class="ui-widget ui-widget-content">
			<thead class="ui-widget-header">
				<tr>
					<td>Room Type</td>
					<td>Sleeps</td>
					<td></td>
				</tr>
			</thead>

			<c:forEach var="rt" items="${sessionScope.roomTypeArray}">
				<tr>
					<td>${rt.roomTypeDesc}</td>
					<td>${rt.roomSleeps}</td>
					<td><a href='ManageRoomTypes.do?deleteID=${rt.rtId}'>Delete</a></td>
				</tr>
			</c:forEach>

			<tr>
				<form method=post action="ManageRoomTypes.do">
					<input type="hidden" name="action" value="addRoomType">
					<td><input name="txtRoomTypeDesc"></td>
					<td><input name="txtSleeps"></td>
					<td><input type="submit" value="Add New"></td>
				</form>
			</tr>
		</table>
	</div>
</body>
</html>