<%@page import="java.time.LocalDateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="java.time.*" %>

<% Integer userId = SessionFilter.user_id.get();
	ContactsDao dao = new ContactsDao();
	List<ContactsObj> contacts = dao.getContactsWithUserId(userId);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contacts</title>
<script
 src="https://code.jquery.com/jquery-3.7.1.js"
 integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
 crossorigin="anonymous">
</script>
<script>
$(document).ready(function(){
	$('#profile').on('click', () => {
		$('#profile-pop-up').toggle();
	})
	
	$('#viewprofile').on('click', () => {
		window.location.href = "/contacts/profile.jsp";
	})
	
	$('#logout').on('click', () => {
		window.location.href = "/contacts/logout";
	})
	
	$('#addcontact').on('click', () => {
		window.location.href = "/contacts/addcontact.jsp"
	})
	
})
</script>
<style>
	@import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap');

	body {
	  display: grid;
	  place-items: center;
	  height: 100vh;
	  font-family: "Poppins", serif;
	  margin: 0;
	  background: #f2f2f2;
	}
	
	.navbar {
	  display: flex;
	  justify-content: space-between;
	  align-items: center;
	  padding: 10px 20px;
	  width: 98%;
	  background: white;
	}
	
	.title {
	  font-weight: bold;
	  font-size: 20px;
	}
	
	.action-container {
	  display: flex;
	  gap: 30px;
	}
	
	#profile-pop-up {
	  display: flex;
	  flex-direction: column;
	  padding: 5px;
	  background: white;
	  position: absolute;
	  right: 2px;
	  top: 60px;
	  border: .2px solid;
	  border-radius: 3px;
	}
	
	#profile-pop-up > * {
	  border-radius: 3px;
	  padding: 10px;
	}
	
	#profile-pop-up > *:hover {
	  background: #f2f2f2;
	}
	
	#profile, #addcontact {
	  cursor: pointer;
	}
	
	.contacts-container {
	  height: 80vh;
	  overflow: scroll;
	  background: white;
	  border-radius: 5px;
	  margin: 20px;
	  padding: 30px;
	  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
	}
	
</style>
</head>
<body>
	<div class="navbar">
		<div class="title">Contacts</div>
		<div class="action-container">
			<div id="addcontact" title="add new contact">
				<img width="40" height="40" src="https://img.icons8.com/stickers/100/plus-2-math.png" alt="plus-2-math"/>
			</div>
			<div id="profile">
				<img width="40" height="40" src="https://img.icons8.com/stickers/100/user-male-circle.png" alt="user-male-circle"/>
				<div id="profile-pop-up" style="display:none">
					<div id="viewprofile">view profile</div>
					<div id="logout">logout</div>
				</div>
			</div>
		</div>
	</div>
	<form action="syncContacts" method="post">
		
		<input type="hidden" name="provider" value="google">
		<input type="submit" value="Sync contacts from Google">
	</form>
	<div class="contacts-container">
		<%if(contacts.size() >0 && contacts!=null) {%>
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Created At</th>
					<th>View Numbers</th>
					<th>View Mails</th>
					<th>View Groups</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
				<% for(ContactsObj contact : contacts){ %>
				<tr>
					<td><%= contact.getFirstName() %></td>
					<td><%=contact.getLastName()%></td>
					<td><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getCreatedAt()), ZoneId.of("Asia/Kolkata")) %></td>
					<td>
						<form action="contactOp?action=viewnumbers" method="post">
							<input type="hidden" name="contact_id" value="<%=contact.getContactId()%>">
							<input type="submit" value="click to view numbers">
						</form>
					</td>
					<td>
						<form action="contactOp?action=viewmails" method="post">
							<input type="hidden" name="contact_id" value="<%=contact.getContactId()%>">
							<input type="submit" value="view Mails">
						</form>
					</td>
					<td>
						<form action="contactOp?action=viewGroups" method="post">
							<input type="hidden" name="contact_id" value="<%=contact.getContactId()%>">
							<input type="submit" value="view Groups">
						</form>
					</td>
					<td><a>Click to edit</a></td>
					<td>
					<form action="contactOp?action=deleteContact">
						<input type="hidden" name="contact_id" value="<%=contact.getContactId()%>">
						<input type="submit" value="click to delete">
					</form>
					</td>
				</tr>
				<%} %>
			</table>
		<%}else{ %>
		<p>No contacts Found. Create to see here</p>
		<%} %>
	</div>
</body>
</html>