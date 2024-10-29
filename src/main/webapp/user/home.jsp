<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="com.models.User" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Profile</title>
</head>
<body>
	<%User user = (User) request.getAttribute("user"); %>
	
	
	<div class="user-details">
		<h2>User Name :<%=user.getUser_name() %></h2>
		<h2>First Name :<%=user.getFirst_name() %></h2>
		<h2>Last Name :<%=user.getLast_name() %></h2>
		<h2>Account Type :<%=user.getAccount_type() %></h2>
	</div>
	<div> EDIT PROFILE</div>
	<div> DELETE ACCOUNT</div>
	
	<div>
		
		<h2>VIEW CONTACTS</h2>
		<h2>VIEW GROUPS</h2>
	</div>
</body>
</html>