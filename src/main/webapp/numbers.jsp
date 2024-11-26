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
<title>Numbers</title>
</head>
<body>
	
	<%
		List<ContactMobileNumbersObj> numbers = (List<ContactMobileNumbersObj>)request.getAttribute("numbers");
		if(numbers.size()>0){
	%>
		<% for(ContactMobileNumbersObj number : numbers){ %>
			<p><%=number.getMobileNumber() %></p>
			
		<%} %>
		<button onClick="history_back()">Go back</button>
    <script>
        function history_back() {
            window.history.back();
        } 
    </script>
	<%} else { %>
		<p>No numbers to display <a href="usercontacts.jsp">Click here to view contacts.</a></p>
	<%} %>

</body>
</html>