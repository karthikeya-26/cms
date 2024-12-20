<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="form-container">
	<h2>Login</h2>
	<form action="login" method="post">
		<label for="mail">Email</label>
		<input type = "email" name="mail" id ="email" required="required"> 
		<label for="password">Password</label>
		<input type="password" name="password" id="password" required="required">
		<button type="submit">Login</button>
		
		<h2>New User ? <a href="signup.jsp">Sign up here</a></h2>
		
		
	</form>
	
	<form action="login?action=googleLogin" method="post">
		<input type="submit" value="Login with Google">
	</form>
	</div>
</body>
</html>