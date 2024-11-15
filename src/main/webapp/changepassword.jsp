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
		<form action="changePass" method="post">
			<label for="prev-pass">Old Password:</label>
			<input required type="text" name="oldPass" ><br>
			<label for="new-pass">New Password:<br> !!! your password should not be more than 72 characters long.<br></label>	
			<input required type="text" name="new-pass" ><br>
			<input type="submit" value="Change Password">
		</form>
	</div>

</body>
</html>