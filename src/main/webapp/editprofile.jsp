<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.filters.*" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Profile</title>
</head>

<% 
	Integer user_id = SessionFilter.user_id.get();
	UserDetailsObj user = SessionDataManager.users_data.get(user_id);
%>
<body>
	
	<h2>HI <%=user.getUser_name() %></h2>
	<a href="profile.jsp"> Go to Profile</a>
	<div>
	<h3>Edit your Profile :</h3>
	<form action="userOp?action=profileUpdate" method="post">
		<label for="user_name">User_name :</label>
		<input type="text" id="user_name" name="user_name" value="<%=user.getUser_name()%>"><br>
		<label for="first_name">First Name :</label>
		<input type="text" id="first_name" name="first_name" value="<%=user.getFirst_name()%>"><br>
		<label for="last_name">Last Name :</label>
		<input type="text" id="last_name" name="last_name" value="<%=user.getLast_name()%>"><br>
		<label for="contactType">Contact Type:</label> <select
				id="contactType" name="contactType" required>
				<option selected="selected" value="public">Public</option>
				<option value="private">Private</option>
			</select><br>
			<br>

			<button type="submit">Update</button>
		
	</form>
	</div>
</body>
</html>