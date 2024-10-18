<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.models.Contact" %>
<%@ page import="java.util.Arrays" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Contact</title>
</head>
<body>
<% Contact contact =  (Contact) request.getAttribute	("contact"); %>
	<form action="contactOp?editContact" method="post">
	<label for="first-name">First Name :</label>
	<input type="text" value="<%= contact.getFirst_name()%>">
	<label for="last-name">Last Name :</label>
	<input type ="text" value="<%= contact.getLast_name() %>">
	<label for="mobile-numbers"> Mobile Numbers</label>
	<input type="text" value="<%=Arrays.asList(contact.getMobile_numbers())%>">
	<label for="emails">Emails :</label>
	<input type="text" value="<%=contact.getMail_ids()%>">
	<label for="groups"></label>
	<input type="text" value="<%=contact.getGroups()%>">
	
	
	
	
	</form>
</body>
</html>