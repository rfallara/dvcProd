<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@ include file="head-includes.jsp" %>
        <link href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/start/jquery-ui.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
        <title>Bank Points</title>
    </head>

    <body>

        <%@ include file="header.jsp"%>

        <div style="margin: 50px">
	    <form method="post" action="BankPoints.do">
		<table class="ui-widget ui-widget-content">
		    <tr><td>
			    Bank Date: <input name="bankDate" id="bankDate" class="myDateField ui-widget-content ui-corner-all">
			    <br>
			    Max available to bank: <span id="maxToBank">--</span>
			</td></tr>
		</table>
		<br>

		<table class="ui-widget ui-widget-content">
		    <tr>
			<td>Available to Bank:</td>
			<td><input name="bankablePoints" id="bankablePoints" class="ui-widget-content ui-corner-all"></td>
		    </tr>
		    <tr>
			<td><input type="hidden" name="action" value="markPointsAsBanked"></td>
			<td><input type="submit" id="btnBankPoints" class="myButton" value="Bank Points"></td>
		    </tr>
		</table>
	    </form>
        </div>

        <div id="dialog-confirm" title="Confirm Mark Points as Banked" style="visibility: hidden">
            <p>
                <span class="ui-icon ui-icon-alert"
                      style="float: left; margin: 0 7px 20px 0;"></span> 
                <span id="confirmMsg"></span>
            </p>
        </div>

	<div id="dialog-banked" title="Bank Points Result" style="visibility: hidden">
	    <p>
		<span id="dialog-confirm-icon" class="ui-icon"
		      style="float: left; margin: 0 7px 20px 0;"></span> 
		<span id="dialog-banked-message"></span>
	    </p>
	</div>

        <script src="js/bankPoints.js" type="text/javascript"></script>
    </body>
</html>
