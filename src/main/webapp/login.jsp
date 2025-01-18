<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<script>
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
                alert('Login successful: ' + JSON.stringify(result));
                // Redirect or handle successful login
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
</script>
</head>
<body>
    <div class="form-container">
        <h2>Login</h2>
        <form onsubmit="login(event)">
            <label for="email">Email</label>
            <input type="email" name="mail" id="email" required="required">
            <label for="password">Password</label>
            <input type="password" name="password" id="password" required="required">
            <button type="submit">Login</button>
        </form>
        
        <h2>New User? <a href="signup.jsp">Sign up here</a></h2>
        
        <form action="login?action=googleLogin" method="post">
            <input type="submit" value="Login with Google">
        </form>
    </div>
</body>
</html>