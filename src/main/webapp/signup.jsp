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
		<h2>Signup Form</h2>
		<form action="/submit-signup" method="post">
			<label for="username">Username:</label> <input type="text"
				id="username" name="username" required><br>
			<br> <label for="firstName">First Name:</label> <input
				type="text" id="firstName" name="firstName" required><br>
			<br> <label for="lastName">Last Name:</label> <input type="text"
				id="lastName" name="lastName" required><br>
			<br> <label for="email">Email:</label> <input type="email"
				id="email" name="email" required><br>
			<br> <label for="password">Password:</label> <input
				type="password" id="password" name="password" required><br>
			<br> <label for="contactType">Contact Type:</label> <select
				id="contactType" name="contactType" required>
				<option value="public">Public</option>
				<option value="private">Private</option>
			</select><br>
			<br>

			<button type="submit">Sign Up</button>
		</form>
		<h2>Already registered ? <a href="login.jsp">Login here</a></h2>
	</div>
</body>
</html>