<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Contact</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous">
</script>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f2f2f2;
        margin: 0;
        padding: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
	.form-container {
		background: #cfcfcf;
		padding:20px;
		border-radius:4px;
	}
    .form-container >div{
        padding: 10px;
        margin:10px;
        gap:10px;
        border-radius: 8px;
    }
    .input-container {
    	padding: 20px;
    }
	#email-container {
		display: flex;
		flex-direction: column;
		gap: 5px;
	}

	#phone-no-container {
		display: flex;
		flex-direction: column;
		gap: 5px;
	}
	h4 {
		display: inline;
	}

    input[type="text"],
    input[type="tel"],
    input[type="email"] {
        width: 100%;
        padding: 8px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-sizing: border-box;
    }

	.add-contact-button {
		background-color: #4CAF50;
		color: white;
		padding: 10px 20px;
		margin: 8px 0;
		border: none;
		border-radius: 4px;
		cursor: pointer;
	}

	.add-contact-button:hover {
		background-color: #45a049;
	}

	#add-phone-input, #add-email-input {
		width: 15px;
		display: inline;
		cursor: pointer;
		padding: 5px;
	}

	#add-phone-input:hover, #add-email-input:hover {
		background-color: #f1f1f1;
	}
    br {
        display: none;
    }
</style>
<script>
    $(document).ready(function(){
        $("#add-phone-input").click(function(){
            $("#phone-no-container").append('<input type="tel" id="phone-no" name="phone-no" placeholder="+91 xxxxx xxxxx" required></input>');
        });
        $("#add-email-input").click(function(){
            $("#email-container").append('<input type="email" id="email" name="email" placeholder="Email" required></input>');
        });
        $(".add-contact-button").click(function(){
            var firstName = $("#first-name-input").val();
            console.log(firstName);
            var lastName = $("#last-name-input").val();
            console.log(lastName)
            var phoneNumbers = [];
            var emails = [];
            var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            var phonePattern = /^\+?[0-9]{1,4}?[-.\s]?(\(?\d{1,3}?\)?[-.\s]?)?[\d\s]{5,15}$/;
            
            if (!firstName) {
                alert("First Name is required");
                return;
            }

            if (!lastName) {
                alert("Last Name is required");
                return;
            }

            var isValid = true;

            $("#phone-no-container input").each(function(){
                console.log($(this).val());
                if (!phonePattern.test($(this).val())) {
                    alert("Invalid Phone Number format");
                    isValid = false;
                    return false; // exit the .each() loop
                }
                phoneNumbers.push($(this).val());
            });
            console.log(phoneNumbers);
            
            if (!isValid) {
                return;
            }

            $("#email-container input").each(function(){
                console.log($(this).val());
                if (!emailPattern.test($(this).val())) {
                    alert("Invalid Email Address format");
                    isValid = false;
                    return false; // exit the .each() loop
                }
                emails.push($(this).val());
            });
            console.log(emails);
            
            if (!isValid) {
                return;
            }

            var contact = {
                "firstName": firstName,
                "lastName": lastName,
                "phoneNumbers": phoneNumbers,
                "emails": emails
            };

            console.log(contact);
             $.ajax({
                type: "POST",
                url: "http://localhost:8280/contacts/contacts",
                data: JSON.stringify(contact),
                contentType: "application/json",
                success: function(data){
                    alert("Contact added successfully");
                    window.location.href = "usercontacts.jsp";
                },
                error: function(data){
                    alert("Error adding contact");
                }
            }); 
        });
    });
</script>
</head>
<body>
    <h2>Add Contact</h2>
    <div class="form-container">

		<div class="input-container" id="first-name">
			<h4>First Name</h4>
			<input type="text" id="first-name-input" name="first-name" placeholder="First Name" required></input>
		</div>
		<div class="input-container" id="last-name">
			<h4>Last Name</h4>
			<input type="text" id="last-name-input" name="last-name" placeholder="Last Name" required></input>
		</div>
		<div class="input-container" id="phone-no-container">
			<h4 >Phone Number</h4>  <span id="add-phone-input" title="add phone"><i class="fa-solid fa-plus"></i></span>
			<input type="tel" id="phone-no" name="phone-no" placeholder="+91 xxxxx xxxxx" required></input>
		</div>
		<div class="input-container" id="email-container">
			<h4 >Email Address</h4><span id="add-email-input" title="add email"><i class="fa-solid fa-plus"></i></span>
			<input type="email" id="email" name="email" placeholder="Email" required></input>
		</div>	
        
    </div>
    <div class="add-contact-button">Add</div>
</body>
</html>