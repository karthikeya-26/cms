<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>Select a sync provider.</h3>
	<% if (request.getParameter("mail_id") == null){ %>
		<p>Looks like you came here by mistake. <a href="profile.jsp">Click here to go to profile.</a> </p>
	<%} %>
	
	<form action="userOp?syncfromGoogle" method="post">
		<input type="hidden" value="<%=request.getParameter("mail_id")%>">
		<input type="submit" value="GOOGLE">
	</form>
	
	<form action="userOp?syncfromGoogle" method="post">
		<input type="hidden" value="<%=request.getParameter("mail_id")%>">
		<input type="submit" value="ZOHO">
	</form>
	
	<form action="userOp?syncfromGoogle" method="post">
		<input type="hidden" value="<%=request.getParameter("mail_id")%>">
		<input type="submit" value="MICROSOFT">
	</form>
</body>
</html>