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
<title>Insert title here</title>
</head>
<body>
	<div class="pass-change">
		<% 
    long ninetyDaysInMillis = 7776000000L; 
    if (System.currentTimeMillis() > user.getPw_last_changed_at() + ninetyDaysInMillis) { 
%>
    <p>Your password is over 90 days old. Please <a href="changepassword.jsp">change your password</a>.</p>
<% 
    } 
%>

	</div>
	
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
		<h3>User Name : <%=user.getUser_name() %></h3>
		<h3>First Name : <%=user.getFirst_name() %></h3>
		<h3>Last Name : <%=user.getLast_name() %></h3>
		<h3>Contact type : <%=user.getContact_type() %></h3>
		
		<h3>Created At : <%=LocalDateTime.ofInstant(Instant.ofEpochMilli(user.getCreated_at()), ZoneId.of("Asia/Kolkata")) %></h3>
		<a href="usermails.jsp">VIEW MAILS</a>
		<a href="editprofile.jsp">EDIT PROFILE</a>
	</div>
</body>
</html>