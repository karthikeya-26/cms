
<%@page import="com.session.SessionDataManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.models.*" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.filters.UserFilter" %>


<%

// Check if the user is logged in


User user = (User)request.getAttribute("user_data");



response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="index.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<title>Home</title>
<style type="text/css">
body {
	height: 100%;
}

table {
	width: 100%;
}

table, th, td {
	border: 2px solid black;
	border-style: dashed;
	border-collapse: collapse;
	padding: 10px;
}

i {
	font-size: 2vw;
	margin: 10%;
	color: black;
}

i:hover {
	color: red;
}

.hidden {
	display: none;
}
</style>

</head>
<body>

	<div class="sidebar">
		<h2>
			Welcome,
			<%=user.getUser_name()%></h2>
		<a href="userOp?action=viewContacts">View Contacts</a> <a
			href="userOp?action=viewGroups">View Groups</a> <a
			href="#user-profile">Profile</a> <a href="#settings">Settings</a> <a
			href="logout">Logout</a>
	</div>

	<div class="main" style="display: flex; flex-direction: column">
		<!-- User Profile Section -->
		<div id="user-profile">
			<h2>
				Hello
				<%=user.getFirst_name() + " " + user.getLast_name()%></h2>
			<h3>
				Email:
				<%=user.getPrimaryEmail()%></h3>
			<h3>
				Your account is
				<%=user.getAccount_type()%></h3>

			<%
			if (user.getmails() != null && user.getmails().size() > 0) {
			%>
			<h3>Your other emails associated to this account are :</h3>
			<%
			for (String email : user.getmails()) {
			%>
			<p style="display: inline" class="mt-4"><%=email%> 			</p> 
			<form action="userOp?action=deleteemail" method="post" class="mt-4"
				style="display: inline">
				<input type="hidden" value="<%=email%>" name="emailtodelete">
				<input type="submit" class="mt-4 btn btn-danger" value="delete this email">
			</form> 
			<form action="userOp?action=setprimaryemail" method="post" class="mt-4" style="display:inline">
				<input type="hidden" value="<%=email %>" name="primaryemail">
				<input type="submit" value="set this email as primary" class="mt-4 btn btn-primary"> 
			</form>
			<br>

			<%
			}
			}
			%>
		</div>
		<div id="add-email">
			<form action="userOp?action=addemail" method="post">
				<label for="add-email">Add Email :</label> <input type="email"
					name="mail" id="add-email" required> <input type="submit"
					value="click to add">
			</form>
		</div>

		<!-- Contact List Section -->

		<div id="contact-list">

			<div id="add-contact">
				<h1>Add New Contact</h1>
				<form action="ContactOp?action=addcontact" method="post">
					<label for="first_name">First Name:</label> <input type="text"
						id="first_name" name="first_name" required><br> <br>
					<label for="last_name">Last Name:</label> <input type="text"
						id="last_name" name="last_name" required><br> <br>
					<label for="address">Address:</label>
					<textarea id="address" name="address" required></textarea>
					<br> <br> <label for="mobile_numbers">Mobile
						Numbers (comma separated):</label> <input type="text" id="mobile_numbers"
						name="mobile_numbers" required><br> <br> <label
						for="email_ids">Email IDs (comma separated):</label> <input
						type="text" id="email_ids" name="email_ids" required><br>
					<br>

					<button type="submit">Add Contact</button>
				</form>
			</div>

			<h1>Contact List</h1>
			<%
			ArrayList<Contact> user_contacts = (ArrayList<Contact>) user.getUser_contacts();

			if (user_contacts != null && user_contacts.size() > 0) {
			%>
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Address</th>
						<th>Mobile Numbers</th>
						<th>Email Id's</th>
						<th>Edit / Delete</th>
					</tr>
				</thead>
				<tbody>
					<%
					for (Contact contact : user_contacts) {
					%>
					<tr>
						<td><%=contact.getFirst_name() + " " + contact.getLast_name()%></td>
						<td><%=contact.getAddress()%></td>
						<td>
							<%
							for (String number : contact.getMobile_numbers()) {
							%> <%=number%><br> <%
 }
 %>
						</td>
						<td>
							<%
							for (String email : contact.getMail_ids()) {
							%> <%=email%><br> <%
 }
 %>
						</td>
						<td>
						<form action="editContact.jsp" method="post">
						<input name="contact" type="hidden" value="<%=contact%>">
						<button type="submit"><i class="bi bi-pencil-square"></i></button>
						</form>
						
							<form action="ContactOp?action=deletecontact" method="post"
								style="display: inline">
								<input  type="hidden" name="contact_id"
									value="<%=contact.getContact_id()%>">
								<button type="submit">
									<i class="bi bi-person-x"></i>
								</button>
							</form></td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
			<%
			} else {
			%>
			<p>No contacts found.</p>
			<%
			}
			%>
		</div>
		<h3>Group contacts</h3>
		<div id="group-contacts" style="padding: 20px;">
			<%
			ArrayList<Contact> group_contacts = (ArrayList<Contact>) user.getGroup_contacts();

			if (group_contacts != null && group_contacts.size() > 0) {
			%>
			<table>
				<thead>
					<tr>
						<th>Name</th>
						<th>Address</th>
						<th>Mobile Numbers</th>
						<th>Email Id's</th>
						<th>Group Name</th>
					</tr>
				</thead>
				<tbody>
					<%
					for (Contact contact : group_contacts) {
					%>
					<tr>
						<td><%=contact.getFirst_name() + " " + contact.getLast_name()%></td>
						<td><%=contact.getAddress()%></td>
						<td>
							<%
							for (String number : contact.getMobile_numbers()) {
							%> <%=number%><br> <%
 }
 %>
						</td>
						<td>
							<%
							for (String email : contact.getMail_ids()) {
							%> <%=email%><br> <%
 }
 %>
						</td>
						<td>
							<%
							for (String group : contact.getGroups()) {
							%> <%=group%><br> <%
 }
 %>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
			<%
			} else {
			%>
			<p>No contacts found.</p>
			<%
			}
			%>
		</div>


		<div>
			<h3>Create groups</h3>
			<form action="userOp?action=createGroup" method="post">
				<input type="hidden" name="user_id" value="<%=user.getUser_id()%>">
				<label for="group_name">Enter the group name :</label> <input
					type="text" name="group_name" id="group_name" required> <input
					type="submit" value="add Group">
			</form>
		</div>

		<div>
			<h3>Add contacts to groups</h3>
			<%
			if (user.getUserGroups() != null && !user.getUserGroups().isEmpty()) {
			%>
			<form action="userOp?action=addContacttoGroup" method="post">
				<label for="groups">Choose a group:</label> <select
					name="selectedgroup" id="groups" required>
					<option disabled="disabled" selected>select one from below</option>
					<%
					for (String group : user.getUserGroups()) {
					%>
					<option value="<%=group%>"><%=group%></option>
					<%
					}
					%>
				</select> <label for="contacts">Choose a contact:</label> <select
					name="selectedcontact" id="contacts" required>
					<option disabled="disabled" selected>select one from below</option>
					<%
					for (Contact contact : user.getUser_contacts()) {
					%>
					<option value="<%=contact.getContact_id()%>">
						<%=contact.getFirst_name() + " " + contact.getLast_name()%>
					</option>
					<%
					}
					%>
				</select> <input type="submit">
			</form>
			<h3>Delete contact from group</h3>
			<form action="userOp?action=deleteContactfromGroup" method="post">
				<label for="groups">Choose a group:</label> <select
					name="selectedgroup" id="groups" required>
					<option disabled="disabled" selected>select one from below</option>
					<%
					for (String group : user.getUserGroups()) {
					%>
					<option value="<%=group%>"><%=group%></option>
					<%
					}
					%>
				</select> <label for="contacts">Choose a contact:</label> <select
					name="selectedcontact" id="contacts" required>
					<option disabled="disabled" selected>select one from below</option>
					<%
					for (Contact contact : user.getUser_contacts()) {
					%>
					<option value="<%=contact.getContact_id()%>">
						<%=contact.getFirst_name() + " " + contact.getLast_name()%>
					</option>
					<%
					}
					%>
				</select> <input type="submit">

			</form>
			<%
			} else {
			%>
			<p>Create a group to add contacts</p>
			<%
			}
			%>

		</div>

	</div>
</body>
</html>
