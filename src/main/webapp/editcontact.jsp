<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="java.time.*" %>

<%ContactsDao dao = new ContactsDao();
ContactsObj contact = dao.getContactWithId(Integer.parseInt(request.getParameter("contact_id"))); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Contact</title>
</head>
<body>
	<div class="form-contaier">
		<form action="contactOp?action=updateContact">
			<label for="first-name">First Name</label>
			<input type="text" value="<%=contact.getFirst_name()%>">
		</form>	
	</div>
</body>
</html>