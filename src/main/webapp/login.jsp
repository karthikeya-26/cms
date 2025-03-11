 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<script
 src="https://code.jquery.com/jquery-3.7.1.js"
 integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
 crossorigin="anonymous">
</script>
<script>
$(document).ready(function(){
	$('.signup').on("click", () => {
		window.location.href = '/contacts/signup.jsp';
	})
	
	$('.google-sign-in').on('click',() =>{
		window.location.href= '/contacts/glogin';
	})
	$('.github-sign-in').on('click',() =>{
		alert('Currently Not Available');
	})

	$('.login').on("click", login);
    async function login(event) {
        event.preventDefault(); // Prevent the default form submission
        
        // Collect input values
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        
        // Create request payload
        const payload = {
            email: email,
            password: password
        };
        
        try {
            // Send the fetch request
            const response = await fetch('http://localhost:8280/contacts/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const result = await response.json();
                window.location.href = '/contacts/profile.jsp';
            } else {
                const error = await response.text();
                alert('Login failed: ' + error);
            }
        } catch (error) {
            console.error('Error during login:', error);
            alert('An error occurred while logging in.');
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
	
	.other-sign-in-container > div:hover {
	  background: #e5e5e5;
	}
</style>
</head>
<body>
	<div class="form-container">
		<div class="input-container">
			<input placeholder="Email" id="email" type="email" /> 
			<input placeholder="Password" id="password" type="password" />
			<div class="button login">Login</div>
			<div class="button signup">Sign up</div>
		</div>
		<hr>
		<div class="other-sign-in-container">
			<div class="google-sign-in">
				<svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="32"
					height="32" viewBox="0 0 48 48">
					<path fill="#FFC107"
						d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z"></path>
					<path fill="#FF3D00"
						d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z"></path>
					<path fill="#4CAF50"
						d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z"></path>
					<path fill="#1976D2"
						d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z"></path>
				</svg>
			</div>
			<div class="github-sign-in">
				<svg height="32" aria-hidden="true" viewBox="0 0 24 24"
					version="1.1" width="32" data-view-component="true"
					class="octicon octicon-mark-github v-align-middle">
    				<path
						d="M12.5.75C6.146.75 1 5.896 1 12.25c0 5.089 3.292 9.387 7.863 10.91.575.101.79-.244.79-.546 0-.273-.014-1.178-.014-2.142-2.889.532-3.636-.704-3.866-1.35-.13-.331-.69-1.352-1.18-1.625-.402-.216-.977-.748-.014-.762.906-.014 1.553.834 1.769 1.179 1.035 1.74 2.688 1.25 3.349.948.1-.747.402-1.25.733-1.538-2.559-.287-5.232-1.279-5.232-5.678 0-1.25.445-2.285 1.178-3.09-.115-.288-.517-1.467.115-3.048 0 0 .963-.302 3.163 1.179.92-.259 1.897-.388 2.875-.388.977 0 1.955.13 2.875.388 2.2-1.495 3.162-1.179 3.162-1.179.633 1.581.23 2.76.115 3.048.733.805 1.179 1.825 1.179 3.09 0 4.413-2.688 5.39-5.247 5.678.417.36.776 1.05.776 2.128 0 1.538-.014 2.774-.014 3.162 0 .302.216.662.79.547C20.709 21.637 24 17.324 24 12.25 24 5.896 18.854.75 12.5.75Z"></path>
				</svg>
			</div>
		</div>
	</div>
</body>
</html>