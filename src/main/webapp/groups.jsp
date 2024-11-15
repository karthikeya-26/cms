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
	<% List<String> group_names = (List<String>) request.getAttribute("groupnames"); %>
	<div>
	
		<%if(group_names!=null && group_names.size()>0){
			for(String group_name : group_names){ %>
				<p><%=group_name %></p>
			<%} %>
			<a href="usercontacts.jsp">Click to go back</a>
		<%}else{ %>
			<p>Contact in no groups <a href="usercontacts.jsp">Click to go back</a></p>
		<%} %>
	</div>
</body>
</html>