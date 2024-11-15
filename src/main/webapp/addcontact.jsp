<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Contact</title>
</head>
<body>
	<div>
		<form action="userOp?action=addContact" method="post">
			<h2>ADD CONTACT</h2>
			<label for="firstName">First Name</label>
			<input type="text" id="first_name" name="firstName"><br>
			<label for="lastName">Last Name</label>
			<input type="text" id="lastName" name="lastName"><br>
			<label for="address">Address</label>
			<input type="text" id="address" name="address"><br>
			<label for="phoneNumber">Phone</label> 
			<input type="tel" id="phoneNumber" name="phoneNumber"><br>
			<label for="email">Email</label>
			<input type="email" id="email" name="email"><br>
			<input type="submit" value="ADD"> 
		</form>
	</div>
</body>
</html>