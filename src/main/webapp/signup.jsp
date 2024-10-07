<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Signup Page</title>
<link rel="stylesheet" type="text/css" href="signup.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<style type="text/css">
	@charset "UTF-8";

@import url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap');

*{
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Poppins';
}

.container{
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #3C3D37;
}

.signup-box{
    width: 320px;
    background-color: white;
    padding: 15px 30px;
    border-radius: 8px;
}

.signup-menu{
    margin-top: 20px;
    margin-bottom: 0;
    position: relative;
}

.signup-name h1{
    color: black;
    font-size: 30px;
    font-weight: 700;
    text-align: center;
}

.underline-name{
    position: relative;
    height: 4px;
    width: 32px;
    background-color: rgb(238, 9, 47);
    border-radius: 5px;
    bottom: 11px;
    left: 70px;
}

.input-box{
    margin-top: 20px;
    width: 260px;
    position: relative;
}

.input-box .inputs{
    margin-bottom: 15px;
    width: 100%;
}

.inputs input{
    height: 35px;
    width: 100%;
    border: none;
    outline: none;
    font-size: 16px;
}

.inputs input::placeholder{
    color: rgb(120, 113, 113);
}

.underline-input{
    height: 2px;
    width: 100%;
    background-color: rgb(200, 189, 189);
}

.underline-input::before{
    position: absolute;
    content: "";
    height: 2.2px;
    border-radius: 3px;
    width: 0%;
    background: linear-gradient(to right, #ba2d2d, #5335c9);
    transition: all .3s ease; 
}

.inputs input:focus ~ .underline-input::before{
    width: 100%;
}

.submit-button{
    margin: 30px 0px 10px 0px;
}

.submit-button input[type="submit"]{
    width: 100%;
    padding: 10px;
    color: white;
    background: linear-gradient(to right, #ba2d2d, #5335c9);
    cursor: pointer;
    border: none;
    outline: none;
    border-radius: 5px;
    font-size: 16px;
    transition: all .3s linear;
}

.submit-button input[type="submit"]:hover{
    letter-spacing: 1px;
    background: linear-gradient(to left, #ba2d2d, #5335c9);
}

.text{
    font-size: 13px;
    text-align: center;
    margin-bottom: 10px;
}

.btn{
    width: 100%;
    padding: 10px 20px;
    color: white;
    cursor: pointer;
    border: none;
    outline: none;
    font-size: 14px;
    border-radius: 5px;
    text-align: left;
    display: flex;
    align-items: center;
}

.btn i{
    font-size: 25px;
    margin-right: 9px;
}
</style>
</head>
<body>
	
	<div class="container">
        <div class="signup-box">

            <div class="signup-name">
                <h1>Sign up</h1>
                <div class="underline-name"></div>
            </div>

            <form action="signup" method="post">

                <div class="input-box">
                
                	<div class="inputs">
                        <input type="text" name="user_name" placeholder="Enter your User Name" required>
                        <div class="underline-input"></div>
                    </div>
                    
                    <div class="inputs">
                        <input type="text" name="first_name" placeholder="Enter your First Name" required>
                        <div class="underline-input"></div>
                    </div>
                    
                    <div class="inputs">
                        <input type="text" name="last_name" placeholder="Enter your Last Name" required>
                        <div class="underline-input"></div>
                    </div>
                    
                    <div class="inputs">
                        <input type="email" name="email" placeholder="Enter your Email" required>
                        <div class="underline-input"></div>
                    </div>

                    <div class="inputs">
                        <input type="password" name="password" placeholder="Enter your Password" required>
                        <div class="underline-input"></div>
                    </div>
                    
                    <div class="inputs">
    					<select name="account_type" required style="width: 100%">
        					<option value="" disabled selected>Select an Account_type</option>
        					<option value="public">Public</option>
        					<option value="private">Private</option>
    					</select>
    					<div class="underline-input"></div>
					</div>
                    
                    <div class="submit-button">
                        <input type="submit" value="Sign up">
                    </div>
                </div>
                
            </form>
            
            

            
        </div>
    </div>

</body>
</html>