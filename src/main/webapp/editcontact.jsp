<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%@ page import="java.time.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Contact</title>
</head>
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous">
</script>

<script>
$(document).ready(function(){
    let contactId;
    let oldContactNumbers = [];
    let oldMails = [];
    console.log("received contact id :" + <%=request.getParameter("contactId")%>);
    $.ajax({
        url: "/contacts/contacts",
        type: "GET",
        data: {
            contactId: <%=request.getParameter("contactId")%>
        },
        success: function(data){
            console.log(data.contact);
            let contact = data.contact;
            contactId = contact.contactDetails.contactId;
            console.log("contactId :" + contactId);
            $("#firstName").val(contact.contactDetails.firstName);
            $("#lastName").val(contact.contactDetails.lastName);
            $(".numbers-container").empty();
            $(".mails-container").empty();
            $(".groups-container").empty();

            // Add numbers
            if (contact.numbers && contact.numbers.length > 0) {
                console.log("Numbers:", contact.numbers);
                $(".numbers-container").empty().append('<h4>Numbers</h4>'); // Add heading inside the container
                contact.numbers.forEach(num => {
                    let numberDiv = $('<div>').addClass('number');
                    oldContactNumbers.push(num.number);
                    let numberInput = $('<input>').attr('type', 'tel').val(num.number);
                    numberDiv.append(numberInput);
                    $(".numbers-container").append(numberDiv);
                });
            }

            // Add emails
            if (contact.mails && contact.mails.length > 0) {
                console.log("Emails:", contact.mails);
                $(".mails-container").empty().append('<h4>Emails</h4>'); // Add heading inside the container
                contact.mails.forEach(mail => {
                    let mailDiv = $('<div>').addClass('mail');
                    oldMails.push(mail.email);
                    let mailInput = $('<input>').attr('type', 'email').val(mail.email);
                    mailDiv.append(mailInput);
                    $(".mails-container").append(mailDiv);
                });
            }

            // Add groups
            if (contact.groups && contact.groups.length > 0) {
                console.log("Groups:", contact.groups);
                $(".groups-container").empty().append('<h4>Groups</h4>'); // Add heading inside the container
                contact.groups.forEach(group => {
                    let groupDiv = $('<div>').addClass('group');
                    let groupInput = $('<input>').attr('type', 'text').val(group.groupName);
                    groupDiv.append(groupInput);
                    $(".groups-container").append(groupDiv);
                });
            }

            console.log("contact id ->" + contactId);
            console.log("old Numbers ->" + oldContactNumbers);
            console.log("old mails ->" + oldMails);
        }
    });

    $(".submit").on('click', function(){
        let newContactNumbers = [];
        let newMails = [];
        let newGroups = [];
        $(".numbers-container .number input").each(function(){
            newContactNumbers.push($(this).val());
        });
        $(".mails-container .mail input").each(function(){
            newMails.push($(this).val());
        });
        $(".groups-container .group input").each(function(){
            newGroups.push($(this).val());
        });
        let payload = {
            contactId: contactId,
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
            oldContactNumbers: oldContactNumbers,
            oldMails: oldMails,
            newContactNumbers: newContactNumbers,
            newMails: newMails,
            newGroups: newGroups
        }
        console.log(payload);
        
        $.ajax({
            url: "/contacts/contacts",
            type: "PUT",
            data: JSON.stringify(payload),
            contentType: "application/json",
            success: function(data){
                console.log(data);
                window.location.href="usercontacts.jsp";
            }
        });
    });
});
</script>
<style>

body{
	display: flex;
	justify-content: center;
	align-items: center;
	align-content: center;
}
.form-container{
    width: 75%;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.contact-details-container {
    margin-bottom: 20px;
}

.contact-details-container label {
    display: block;
    margin-bottom: 5px;
}

.contact-details-container input {
    width: 25%;
    padding: 5px;
    margin-bottom: 10px;
}

.numbers-container,
.mails-container,
.groups-container {
	width: 100%;
    margin-bottom: 20px;
}

.numbers-container h4,
.mails-container h4,
.groups-container h4 {
    margin-bottom: 10px;
}

.number,
.mail,
.group {
    margin-bottom: 10px;
}

.number input,
.mail input,
.group input {
    width: 100%;
    padding: 5px;
}

.submit {
	width: 25%;
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    text-align: center;
    cursor: pointer;
    border-radius: 5px;
}

.submit:hover {
    background-color: #45a049;
}

</style>
<body>
	<div class="form-contaier">
		<form action="contactOp?action=updateContact">
			<div class="contact-details-container">
				<h4>Contact Details :</h4>
				<label for="firstName">First name:</label>
				<input type="text" id="firstName" name="firstName"><br>
				<label for="firstName">Last name:</label>
				<input type="text" id="lastName" name="lastName"><br>
			</div>
			<div class="numbers-container">
				
			</div>
			<div class="mails-container">
			
			</div>
			<div class="groups-container">
				
			</div>
			<div class="submit"> Save </div> 
		</form>	
	</div>
</body>
</html>