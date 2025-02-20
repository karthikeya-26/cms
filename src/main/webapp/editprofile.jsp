<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.filters.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            line-height: 1.6;
            color: #333;
            background-color: #f5f5f5;
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        h3 {
            color: #34495e;
            margin-bottom: 20px;
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

        .profile-form {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-group {
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

        button {
            background-color: #3498db;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.2s;
        }

        button:hover {
            background-color: #2980b9;
        }
    </style>
</head>

<% 
Integer user_id = SessionFilter.USER_ID.get();
UserDetailsObj user = SessionDataManager.usersData.get(user_id);
%>

<body>
    <h2>Hi <%=user.getUserName()%></h2>
    <a href="profile.jsp" class="nav-link">Go to Profile</a>
    
    <div class="profile-form">
        <h3>Edit your Profile:</h3>
        <form action="userOp?action=profileUpdate" method="post">
            <div class="form-group">
                <label for="user_name">Username:</label>
                <input type="text" id="user_name" name="user_name" value="<%=user.getUserName()%>">
            </div>

            <div class="form-group">
                <label for="first_name">First Name:</label>
                <input type="text" id="first_name" name="first_name" value="<%=user.getFirstName()%>">
            </div>

            <div class="form-group">
                <label for="last_name">Last Name:</label>
                <input type="text" id="last_name" name="last_name" value="<%=user.getLastName()%>">
            </div>

            <div class="form-group">
                <label for="contactType">Contact Type:</label>
                <select id="contactType" name="contactType" required>
                    <option selected="selected" value="public">Public</option>
                    <option value="private">Private</option>
                </select>
            </div>

            <button type="submit">Update</button>
        </form>
    </div>
</body>
</html>