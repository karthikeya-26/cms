<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Oauth Login</title>
</head>
<body>

<% 
    String currentUrl = request.getRequestURL().toString(); 
	response.getWriter().print(currentUrl);
%>

		<form action="api/v1/oauthLogin" method="post">
		<label>EMAIL :</label><br>
		<input type="email" name="email" placeholder="..@x.com"><br>
		
		<label>PASSWORD :</label>
		<input type="password" name ="password" >	
		
		<input type="submit" value="Login">	
		</form>
		
		<%
Map<String, String[]> reqParams = (Map<String, String[]>) request.getAttribute("client");
if (reqParams != null) {
    for (Map.Entry<String, String[]> param : reqParams.entrySet()) {
        out.println("Key: " + param.getKey() + "<br>");
        for (String value : param.getValue()) {
            out.println("Value: " + value + "<br>");
        }
    }
} else {
    out.println("No parameters received.<br>");
}
%>

</body>
</html>