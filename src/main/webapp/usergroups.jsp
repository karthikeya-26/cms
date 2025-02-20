<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>User Groups</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
            color: #333;
        }

        .nav-link {
            display: inline-block;
            margin-bottom: 25px;
            color: #3498db;
            text-decoration: none;
            padding: 8px 15px;
            border-radius: 4px;
            background-color: #eef2f7;
            transition: background-color 0.2s;
        }

        .nav-link:hover {
            background-color: #d5e1ed;
        }

        .section {
            background-color: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 25px;
        }

        h3 {
            color: #2c3e50;
            margin-top: 0;
            margin-bottom: 20px;
        }

        form {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #2c3e50;
        }

        input[type="text"],
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            margin-bottom: 15px;
        }

        input[type="text"]:focus,
        select:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }

        input[type="submit"] {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.2s;
        }

        input[type="submit"]:hover {
            background-color: #2980b9;
        }

        .delete-btn {
            background-color: #e74c3c !important;
        }

        .delete-btn:hover {
            background-color: #c0392b !important;
        }

        .group-card {
            background-color: #fff;
            border-radius: 6px;
            padding: 15px;
            margin-bottom: 15px;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .group-name {
            font-size: 18px;
            font-weight: 500;
            margin-bottom: 15px;
            color: #2c3e50;
        }

        .group-actions {
            display: flex;
            gap: 10px;
        }

        .empty-state {
            text-align: center;
            padding: 20px;
            color: #7f8c8d;
        }
    </style>
    <%
    Integer user_id = SessionFilter.USER_ID.get();
    UserGroupsDao ugdao = new UserGroupsDao();
    ContactsDao cdao = new ContactsDao();
    
    List<UserGroupsObj> user_groups = ugdao.getUserGroups(user_id);
    List<ContactsObj> user_contacts = cdao.getContactsWithUserId(user_id);
    %>
</head>
<body>
    <a href="profile.jsp" class="nav-link">GO to Profile</a>
    
    <div class="section">
        <h3>Add Group</h3>
        <form action="userOp?action=addGroup" method="post">
            <label for="group_name">Enter Group Name:</label>
            <input type="text" name="group_name" id="group_name" required>
            <input type="submit" value="Add Group">
        </form>
    </div>

    <%if (user_contacts.size()>0 && user_groups.size()>0 && user_groups != null && user_contacts!= null){ %>
    <div class="section">
        <h3>Add Contact to Group</h3>
        <form action="userOp?action=addContactToGroup" method="post">
            <label for="contact_id">Select Contact:</label>
            <select required id="contact_id" name="contactId">
                <option selected="selected" disabled="disabled">Click to choose</option>
                <%for(ContactsObj contact : user_contacts){ %>
                <option value="<%=contact.getContactId()%>">
                    <%=contact.getFirstName()+" "+contact.getLastName()%>
                </option>
                <%} %>
            </select>

            <label for="group_id">Select Group:</label>
            <select required id="group_id" name="groupId">
                <option selected="selected" disabled="disabled">Click to choose</option>
                <%for(UserGroupsObj group : user_groups){ %>
                <option value="<%=group.getGroupId()%>"><%=group.getGroupName()%></option>
                <%} %>
            </select>
            <input type="submit" value="Add to Group">
        </form>
    </div>
    <%}else{ %>
    <div class="empty-state">
        <p>Create contacts/groups to add into groups</p>
    </div>
    <%} %>

    <div class="usergroups">
        <% if(user_groups != null && user_groups.size()>0){ %>
            <%for(UserGroupsObj user_group : user_groups){ %>
            <div class="group-card">
                <p class="group-name"><%=user_group.getGroupName()%></p>
                <div class="group-actions">
                    <form action="userOp?action=viewGroupContacts" method="post">
                        <input type="hidden" name="group_id" value="<%=user_group.getGroupId()%>">
                        <input type="submit" value="View Contacts">
                    </form>
                    <form action="userOp?action=deleteGroup" method="post">
                        <input type="hidden" name="group_id" value="<%=user_group.getGroupId()%>">
                        <input type="submit" value="Delete Group" class="delete-btn">
                    </form>
                </div>
            </div>
            <%} %>
        <%} %>
    </div>
</body>
</html>