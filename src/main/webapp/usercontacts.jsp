<%@page import="java.time.LocalDateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="java.time.*" %>

<% Integer user_id = SessionFilter.user_id.get();
	List<ContactsObj> contacts = NewDao.getUserContacts(user_id);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contacts</title>
</head>
<body>
	<h2>Contacts</h2>
	<a href="profile.jsp">Go back to Profile</a>
	<a href="addcontact.jsp"> Add Contact</a>
	<%if(contacts.size() >0) {%>
		<table>
			<tr>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Address</th>
				<th>Created At</th>
				<th>View Numbers</th>
				<th>View Mails</th>
				<th>View Groups</th>
				<th>Edit</th>
				<th>Delete</th>
			</tr>
			<% for(ContactsObj contact : contacts){ %>
			<tr>
				<td><%=contact.getFirst_name() %></td>
				<td><%=contact.getLast_name()%></td>
				<td><%=contact.getAddress() %></td>
				<td><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getCreated_at()), ZoneId.of("Asia/Kolkata")) %></td>
				<td>
					<form action="contactOp?action=viewnumbers" method="post">
						<input type="hidden" name="contact_id" value="<%=contact.getContact_id()%>">
						<input type="submit" value="click to view numbers">
					</form>
				</td>
				<td>
					<form action="contactOp?action=viewmails" method="post">
						<input type="hidden" name="contact_id" value="<%=contact.getContact_id()%>">
						<input type="submit" value="view Mails">
					</form>
				</td>
				<td>
					<form action="contactOp?action=viewGroups" method="post">
						<input type="hidden" name="contact_id" value="<%=contact.getContact_id()%>">
						<input type="submit" value="view Groups">
					</form>
				</td>
				<td><a>Click to edit</a></td>
				<td>
				<form action="contactOp?action=deleteContact">
					<input type="hidden" name="contact_id" value="<%=contact.getContact_id()%>">
					<input type="submit" value="click to delete">
				</form>
				</td>
			</tr>
			<%} %>
		</table>
	<%}else{ %>
		<p>No contacts Found. Create to see here</p>
	<%} %>
</body>
</html>