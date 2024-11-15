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
<title>Mails</title>
</head>
<body>
	<% List<ContactMailsObj> mails=(List<ContactMailsObj>)request.getAttribute("mails"); %>
	<% if(mails.size() > 0){ %>
		<% for (ContactMailsObj mail : mails){ %>
			<p><%=mail.getMail() %></p>
		<%} %>
		<button onClick="history_back()">Go back</button>
   	 <script>
        function history_back() {
            window.history.back();
        } 
    </script>
	<%} %>
</body>
</html>