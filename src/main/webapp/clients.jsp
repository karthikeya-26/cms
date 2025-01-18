<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.dao.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Clients</title>
</head>
<body>

	<% ClientDetailsDao dao = new ClientDetailsDao();
	
		List<ClientDetailsObj> userClients = dao.getUserClients(SessionFilter.user_id.get());
		
	%>
	<% if(userClients == null || userClients.size()== 0){ %>
		<p>NO client found</p>
	<%} else {%>
	
		<% for(ClientDetailsObj client : userClients) { %>
			<% response.getWriter().print(client); %>
		<%} %>
	<%} %>	
</body>
</html>