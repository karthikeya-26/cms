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
<title>User groups</title>
<% Integer user_id = SessionFilter.user_id.get(); 
	UserGroupsDao ugdao = new UserGroupsDao();
	ContactsDao cdao = new ContactsDao();
	
	List<UserGroupsObj> user_groups = ugdao.getUserGroups(user_id);
	List<ContactsObj> user_contacts = cdao.getContactsWithUserId(user_id);%>
</head>
<body>
	<a href="profile.jsp"> GO to Profile</a>
	<h3>Add group</h3>
	<form action="userOp?action=addGroup" method="post">
		<label for="group_name">Enter group Name :</label>
		<input type="text" name="group_name" id="group_name">
		<input type="submit" value="add group">
	</form>
	<%if (user_contacts.size()>0 && user_groups.size()>0 && user_groups != null && user_contacts!= null){ %>
	<h3>Add Contact to group</h3>
	<form action="userOp?action=addContactToGroup">
		<label for="contact_id">Select contact :</label>
		<select required id="contact_id" name="contact_id">
			<option selected="selected" disabled="disabled">click to choose</option>
			<%for(ContactsObj contact : user_contacts){ %>
				<option value="<%=contact.getContactId() %>" ><%=contact.getFirstName()+" "+contact.getLastName() %>
			<%} %>
		</select>
		<label for="group_id">Select Group :</label>
		<select required id="group_id" name="group_id">
			<option selected="selected" disabled="disabled">Click to choose</option>
			<%for(UserGroupsObj group : user_groups){ %>
				<option value="<%=group.getGroupId()%>"><%=group.getGroupName() %></option>
			<%} %>
		</select>
		<input type="submit" value="ADD">
	</form>
	<%}else{ %>
		<p>Create contacts/groups to add into groups</p>
	<%} %>
	<div class="usergroups">
		<% if(user_groups != null && user_groups.size()>0){ %>
			<%for(UserGroupsObj user_group : user_groups){ %>
				<p><%= user_group.getGroupName() %></p>
				<form action="userOp?action=viewGroupContacts" method="post">
					<input type="hidden" name="group_id" value="<%=user_group.getGroupId()%>">
					<input type="submit" value="click to view contacts ">
				</form>
				<form action="userOp?action=deleteGroup" method="post">
					<input type="hidden" name="group_id" value="<%=user_group.getGroupId()%>">
					<input type="submit" value="click to delete this group">
				</form>
				
				<a href=userOp?viewGroupContacts></a>
			<%} %>
		<%} %>
	</div>
</body>
</html>