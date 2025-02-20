<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="java.time.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%List<ContactsObj> contacts = (List<ContactsObj>)request.getAttribute("contacts"); %>
	<a href="usergroups.jsp"> Go back to see groups</a>
	<%if(contacts.size() >0) {%>
		<table>
			<tr>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Created At</th>
			</tr>
			<% for(ContactsObj contact : contacts){ %>
			<tr>
				<td><%=contact.getFirstName() %></td>
				<td><%=contact.getLastName()%></td>
				<td><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getCreatedAt()), ZoneId.of("Asia/Kolkata")) %></td>
				
			</tr>
			<%} %>
		</table>
	<%}else{ %>
		<p>No contacts Found. Create to see here</p>
	<%} %>
</body>
</html>