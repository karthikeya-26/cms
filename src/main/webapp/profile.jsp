<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*"%>
<%@ page import="com.dao.*"%>
<%@ page import="com.session.*"%>
<%@ page import="com.filters.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.time.*"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%
Integer userId = SessionFilter.USER_ID.get();
UserDetailsObj user = SessionDataManager.usersData.get(userId);
UserMailsDao emailsDao = new UserMailsDao();
List<UserMailsObj> emails = emailsDao.getUserMails(userId);
UserGroupsDao groupsDao = new UserGroupsDao();
List<UserGroupsObj> groups = groupsDao.getUserGroups(userId);
ContactsSyncDao syncDao = new ContactsSyncDao();
List<ContactsSyncObj> syncTokens = syncDao.getUserSyncTokens(userId);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Profile</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
	integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<style>
@import
	url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap')
	;

body {
	display: grid;
	place-items: center;
	height: 100vh;
	font-family: "Poppins", serif;
	background: #f1f1f1;
	margin: 0;
}

.navbar {
	display: grid;
	grid-template-columns: 15% 85%;
}

.back-navigation-icon {
	display: flex;
	align-content: center;
	align-items: center;
	justify-content: center;
	font-size: 20px;
}

.back-navigation-icon:hover {
	background: #efefef;
	font-size: 24px;
	cursor: pointer;
}

.logo {
	font-size: 40px;
	background:
}

.dashboard {
	display: grid;
	grid-template-columns: 1fr 1fr 1fr;
	gap: 20px;
}

.profile-details-container {
	grid-column: 1/4;
	background: #fff;
	padding: 20px;
	border-radius: 5px;
}

.profile-details {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 10px;
}

.profile-details-item {
	display: flex;
	flex-direction: column;
}

.profile-details-item-label {
	font-weight: bold;
}

.emails-container {
	background: #fff;
	padding: 20px;
	border-radius: 5px;
}

.emails {
	display: grid;
	gap: 10px;
}

.email {
	display: grid;
	grid-template-columns: 1fr 1fr 1fr;
	gap: 10px;
}

.user-groups-container {
	background: #fff;
	padding: 20px;
	border-radius: 5px;
	overflow: hidden;
}

.user-groups {
	display: grid;
	grid-template-columns: 1fr;
	width: 100%;
	gap: 10px;
	justify-content: left;
}

.user-group {
	display: grid;
	grid-template-columns: 1fr 1fr 1fr;
	justify-content: space-around;
	align-items: left;
	align-content: left;
}

#edit-profile-link {
	color: black;
	font-size: 20px;
}

#edit-profile-link>i {
	padding: 5px;
}

#edit-profile-link>i:hover {
	cursor: pointer;
	background: #dfdfdf;
}

#delete-account-link {
	font-size: 20px;
}

#delete-account-link>i {
	padding: 5px;
}

#delete-account-link>i:hover {
	cursor: pointer;
	background: #dfdfdf;
}

.email-value>.email-delete-icon:hover {
	cursor: pointer;
}

.email-primary {
	padding: 5px;
}

.email-primary:hover {
	cursor: pointer;
	background: #dfdfdf;
}

.email-delete-icon {
	padding: 10px;
}

.email-delete-icon:hover {
	cursor: pointer;
	background: #efefef
}

.group-name {
	font-weight: bold;
}

.group-actions>div {
	display: inline;
	flex-direction: row;
	gap: 10px;
	padding: 5px;
	margin: 5px;
	justify-content: space-between;
}

.group-actions>div:hover {
	cursor: pointer;
	background: #efefef;
}
</style>
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous">
</script>
<script>
$(document).ready(function(){

	$('#profile-back').on('click', () =>{
		window.location.href = "/contacts/usercontacts.jsp";
	})
	$(".email-delete-icon").click(function(){
		console.log("clicked email delete icon");
		var emailId = $(this).siblings(".email-id").text().trim();
		console.log(emailId);
		if(confirm("Are you sure to delete this mail")){
			$.ajax({
				url: "userOp?action=deleteEmail",
				type: "POST",
				data: {
					emailId: emailId
				},
				success: function(data){
					location.reload();
				}
			});
		}
	});
	
	$(".email-primary").click(function(){
		console.log("clicked set primary");
		var emailId = $(this).siblings(".email-id").text().trim();
		console.log(emailId);
		if(confirm("Are you sure to set this mail as primary")){
		$.ajax({
			url: "userOp?action=setPrimaryEmail",
			type: "POST",
			data: {
				emailId: emailId
			},
			success: function(data){
				location.reload();
			}
		});
		}
	});
	
	$("#edit-profile-link").click(function(){
		window.location.href = "editprofile.jsp";
	});
	
	$("#delete-account-link").click(function(){
		if(confirm("Are you sure you want to delete your account?")){
			$.ajax({
				url: "deleteaccount",
				type: "POST",
				success: function(data){
					window.location.href = "login.jsp";
				}
			});
		}
	});
})
</script>
</head>
<body>

	<%-- 	
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
	</div> --%>

	<div class="dashboard">
		<div class="navbar">
			<div id="profile-back" class="back-navigation-icon">
				<i class="fa-solid fa-chevron-left"></i>
			</div>
			<div class="logo">Kontactz</div>
		</div>

		<div class="profile-details-container">
			<h3>Profile Info</h3>

			<div class="profile-details">
				<div class="profile-details-item">
					<span class="profile-details-item-label">User Name</span> <span
						class="profile-details-item-value"><%=user.getUserName()%></span>
				</div>
				<div class="profile-details-item">
					<span class="profile-details-item-label">First Name</span> <span
						class="profile-details-item-value"><%=user.getFirstName()%></span>
				</div>
				<div class="profile-details-item">
					<span class="profile-details-item-label">Last Name</span> <span
						class="profile-details-item-value"><%=user.getLastName()%></span>
				</div>
				<div class="profile-details-item">
					<span class="profile-details-item-label">Contact Type</span> <span
						class="profile-details-item-value"><%=user.getContactType()%></span>
				</div>
				<div class="profile-details-item">
					<span class="profile-details-item-label">Created At</span> <span
						class="profile-details-item-value"><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(user.getCreatedAt()), ZoneId.of("Asia/Kolkata"))
		.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))%></span>
				</div>

				<div class="profile-details-item">
					<span id="edit-profile-link" title="click to edit profile"
						class="profile-details-item-value"><i
						class="fa-regular fa-pen-to-square"></i></span> <span
						id="delete-account-link" title="click to delete account"
						class="profile-details-item-value"><i
						class="fa-solid fa-trash"></i></span>
				</div>
			</div>

			<div class="emails-container">
				<h3>Emails</h3>
				<div class="emails">
					<%
					for (UserMailsObj email : emails) {
					%>
					<div class="email">
						<span class="email-id" style="display: none"><%=email.getMailId()%></span>
						<span class="email-value"><%=email.getMail()%> </span> <span
							class="email-delete-icon"> <i title="click to delete mail"
							class="fa-solid fa-trash " style="cursor: pointer"></i></span> <span
							title="click to set as primary mail" class="email-primary"><%=email.getIsPrimary() == 1 ? "⭐" : "☆"%></span>
					</div>
					<%
					}
					%>
				</div>
			</div>

			<div class="user-groups-container">
				<h3>Groups</h3>
				<div class="user-groups">
					<%
					for (UserGroupsObj group : groups) {
					%>
					<div class="user-group <%=group.getGroupId()%>">
						<span class="group-name"><%=group.getGroupName()%></span> <span
							class="group-created-at"><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(user.getCreatedAt()), ZoneId.of("Asia/Kolkata"))
		.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))%></span>

						<div>
							<div class="group-actions">
								<div title="click to edit group" class="edit-group">
									<i class="fa-regular fa-pen-to-square"></i>
								</div>
								<div title="click to delete group" class="delete-group">
									<i class="fa-solid fa-trash"></i>
								</div>
							</div>
						</div>
					</div>
					<%
					}
					%>
				</div>
			</div>

			<div class="sync-details-container">
				<h3>Sync Details</h3>
				<div class="sync-details">
					<%
					if (syncTokens.size() > 0) {
					%>
					<%
					for (ContactsSyncObj contact : syncTokens) {
					%>
					<div class="sync-detail">
						<span class="sync-detail-label">Provider</span> <span
							class="sync-detail-value"><%=contact.getProvider()%></span> <span
							class="sync-detail-label">Account Id</span> <span
							class="sync-detail-value"><%=contact.getAccountId()%></span> <span
							class="sync-detail-label">Created At</span> <span
							class="sync-detail-value"><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getCreatedAt()), ZoneId.of("Asia/Kolkata"))
		.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))%></span>
						<%-- <span class="sync-detail-label">Last Updated At</span>
							<span class="sync-detail-value"><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getLastUpdatedAt()), ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")) %></span> --%>
					</div>
					<%
					}
					%>
					<%
					} else {
					%>
					<div class="sync-detail">
						<span class="sync-detail-label">No Sync Details Found</span>
					</div>
					<%
					}
					%>

				</div>
			</div>
		</div>
</body>
</html>