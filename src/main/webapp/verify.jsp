<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Email Verification</title>
</head>
<body>
    <h2>${status eq 'success' ? 'Success' : 'Error'}</h2>
    <p>${message}</p>
    <a href="login.jsp">Go to Login</a>
</body>
</html>
