<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.dbObjects.*" %>
<%@ page import="java.util.*" %>

<%
	List<ContactsObj> contacts =  (List<ContactsObj>) request.getAttribute("contacts");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contacts</title>
</head>
<body>
	<h2>Your Contacts</h2>
	<table>
		<tr>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Address</th>
			<th>Created At</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		
		<% if(!contacts.isEmpty()){
			for (ContactsObj contact : contacts){%>
				<tr>
					<td><%= contact.getFirst_name() %> </td>
					<td><%= contact.getLast_name() %> </td>
					<td><%= contact.getAddress() %> </td>
					<td><%= contact.getCreated_at() %> </td>
					<td><form action=""></form></td>
					<td><form action="userOp?action=delete">
							<h3>Delete</h3>
							<input type="hidden" value="<%=contact.getContact_id()%>">
						</form>
					</td>
					
				</tr>
			<%} %>
			
		<%} %>		
	</table>
</body>
</html>