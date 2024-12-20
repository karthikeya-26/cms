<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="java.time.*" %>
<% 
	Integer user_id = SessionFilter.user_id.get();
	UserDetailsObj user = SessionDataManager.users_data.get(user_id);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Profile</title>
</head>
<body>
	
	
	<div>
	<h2>Actions</h2>
	<a href="usercontacts.jsp">View Contacts</a>
	<a href="usergroups.jsp">View Groups</a>
	<form action="logout" method="post">
		<input type="submit" value="Logout">
	</form>
	</div>
	
	<div>
		<h2>Profile Details</h2>
		<h3>User Name : <%=user.getUserName() %></h3>
		<h3>First Name : <%=user.getFirstName() %></h3>
		<h3>Last Name : <%=user.getLastName() %></h3>
		<h3>Contact type : <%=user.getContactType() %></h3>
		
		<h3>Created At : <%=LocalDateTime.ofInstant(Instant.ofEpochMilli(user.getCreatedAt()), ZoneId.of("Asia/Kolkata")) %></h3>
		<a href="usermails.jsp">VIEW MAILS</a>
		<a href="editprofile.jsp">EDIT PROFILE</a>
	</div>
</body>
</html>