<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Sign Up</title>
<script
 src="https://code.jquery.com/jquery-3.7.1.js"
 integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
 crossorigin="anonymous">
</script>
<script type="text/javascript">
$(document).ready(function(){
	$('.login').on("click",()=>{
		window.location.href = '/contacts/login.jsp';
	});
	
	$('.signup').on("click",signup);
	async function signup(event) {
		event.preventDefault();
		let email = document.getElementById('email').value;
		let userName = document.getElementById('username').value;
		let firstName = document.getElementById('firstname').value;
		let lastName = document.getElementById('lastname').value;
		let password = document.getElementById('password').value;
		let profileTypes = document.getElementsByName("profile-type");
		let selectedValue = "";

		for (const radio of profileTypes) {
			if (radio.checked) {
				selectedValue = radio.value;
				break;
			}
		}

		const payload = {
			email: email,
			userName: userName,
			firstName: firstName,
			lastName: lastName,
			password: password,
			profileType: selectedValue
		};

		console.log(payload);

		try {
			const response = await fetch('http://localhost:8280/contacts/signup', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(payload)
			});

			if (response.ok) {
				const result = await response.json();
				alert('SignUp successful: ' + result);
			} else {
				const error = await response.json();
				alert('SignUp failed: ' + error.error);
			}
		} catch (error) {
			console.error('Error during signup:', error);
			alert('An error occurred while signing up.');
		}
	}
});

</script>
<style>
	@import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap');

	body {
	  display: grid;
	  place-items: center;
	  height: 100vh;
	  font-family: "Poppins", serif;
	  background: url("https://img.freepik.com/free-vector/hand-drawn-style-line-doodle-pattern-background_1409-3950.jpg?t=st=1737282357~exp=1737285957~hmac=1d662ef2fea173eac0631b7c048ca1b5db92fc684009c2124d3cce3c806538c8&w=1800");
	  margin: 0;
	}
	
	.form-container {
	  display: grid;
	  gap: 10px;
	  padding: 40px 50px;
	  background: #252525;
	  border-radius: 5px;
	  min-width: 15%;
	}
	
	.input-container {
	  display: flex;
	  flex-direction: column;
	  gap: 10px;
	}
	
	input {
	  padding: 10px;
	  border-radius: 3px;
	  border: 1px solid grey;
	  font-size: 16px;
	  background: white;
	}
	
	input:-webkit-autofill,
	input:-webkit-autofill:hover, 
	input:-webkit-autofill:focus, 
	input:-webkit-autofill:active{
	    -webkit-box-shadow: 0 0 0 30px white inset !important;
	}
	
	*:focus {
	    outline: none;
	}
	
	
	.button {
	  padding: 10px;
	  background: dodgerblue;
	  color: white;
	  display: flex;
	  justify-content: center;
	  font-weight: bold;
	  border-radius: 3px;
	  cursor: pointer;
	}
	
	.login:hover {
	  background: cornflowerblue;
	}
	
	.signup {
	  background: none;
	  color: #f2f2f2;
	  border: 1px solid tomato;
	}
	.signup:hover {
	  background: tomato;
	  color: white;
	}
	
	hr {
	  width: 100%;
	}
	
	.other-sign-in-container {
	  display: flex;
	  justify-content: center;
	  gap: 10px;
	}
	
	.other-sign-in-container > div {
	  border-radius: 3px;
	  background: #efefef;
	  padding: 5px;
	  justify-content: center;
	  align-items: center;
	  display: flex;
	  cursor: pointer;
	}
	
	.radio-text{
		color: white;
	}
	
	
	
</style>
</head>
<body>
	<div class="form-container">
		<div class="input-container">
			<input required placeholder="Email" id="email" name="email" type="email">
			<input required placeholder="UserName" id="username" name="username" type="text">
			<input required placeholder="FirstName" id="firstname" name="firstname" type="text">
			<input required placeholder="LastName" id="lastname" name="lastname" type="text">
			<input required placeholder="Password" id="password" name="password" type="password">

			<label class="radio-text"><input type="radio" name="profile-type" value="public"> Public</label>
			<label class="radio-text"><input type="radio" name="profile-type" value="private"> Private</label>
			<div class="button signup">Sign up</div>
			<div class="button login">Login</div>
			
		</div>
	</div>
</body>
</html>
