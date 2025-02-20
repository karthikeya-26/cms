<%@page import="java.time.LocalDateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="com.filters.*"%>
<%@ page import="com.dbObjects.*"%>
<%@ page import="com.session.*"%>
<%@ page import="com.dao.*"%>
<%@ page import="java.time.*"%>
<%@ page import="java.time.format.DateTimeFormatter"%>

<%
Integer userId = SessionFilter.USER_ID.get();
ContactsDao dao = new ContactsDao();
List<ContactsObj> contacts = dao.getContactsWithUserIdSorted(userId);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contacts</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous">
</script>
<script>
$(document).ready(function(){
	
	$('#profile').on('click', () => {
		$('#profile-pop-up').toggle();
	})
	
	$('#popup-close').on('click',() =>{
		$('#contact-popup').fadeOut(200);
		$(".FirstName span").text("");
        $(".LastName span").text("");
        $(".createdAt span").text("");
        $(".popup-numbers span").text("");
      	$('.popup-mails span').text("");
      	$('.popup-groups span').text("");
	})
	
	$('#viewprofile').on('click', () => {
		window.location.href = "/contacts/profile.jsp";
	})
	
	$('#logout').on('click', () => {
		$.ajax({
			url:"/contacts/logout",
			type:"POST",
			success: function (data) {
				window.location.href="/contacts/login.jsp"
			}
		});
	})
	
	$('#addcontact').on('click', () => {
		window.location.href = "/contacts/addcontact.jsp"
	})
	
	$('.edit').on('click', function() {
	    let contactId = $(this).attr('id');
	    window.location.href = "/contacts/editcontact.jsp?contactId="+contactId;
	});

	$('.delete').on('click',function() {
		let contactId = $(this).attr('id');
		console.log("Contact ID:", contactId);
		let payload = {
				contactId: contactId
		}
		$.ajax({
			url: "/contacts/contacts",
			type: "DELETE",
			data: JSON.stringify(payload),
			success: function(data){
				console.log(data);
				window.location.reload();
			}
		});
	});

    $('.contact-detail').on('click', function() {
        let contactId = $(this).find('.contact-id').text().trim();
        
        console.log("Contact ID:", contactId);
		let payLoad = {
			contactId: contactId
		}
		$.ajax({
			url: "/contacts/contacts",
			type: "GET",
			data: payLoad,
			success: function(data){
				let contact = data.contact;
				
				$(".FirstName span").text(contact.contactDetails.firstName);
				$(".LastName span").text(contact.contactDetails.lastName);
				$(".createdAt span").text(new Date(contact.contactDetails.createdAt).toLocaleString());
				if (contact.numbers && contact.numbers.length > 0) {
					console.log("Numbers:", contact.numbers);
					$(".popup-numbers span").text(contact.numbers.map(num =>num.number).join(", "));
				} 
				if(contact.mails){
					$('.popup-mails span').text(contact.mails.map(mail =>mail.email).join(", "));
				}
				if(contact.groups){
					$('.popup-groups span').text(contact.groups.map(group =>group.groupName).join(", "));
				}

				console.log("Contact ID:", contact.contactDetails.contactId);
				$(".button.edit").attr("id", contact.contactDetails.contactId);
				$(".button.delete").attr("id", contact.contactDetails.contactId);
				$(".button.close").attr("id", contact.contactDetails.contactId);
			}
		});
		
		$("#contact-popup").fadeIn(200);
       
    });
	
	

	 $(document).on('click', function(event) {
        if (!$(event.target).closest('#profile, #profile-pop-up').length) {
            $('#profile-pop-up').hide();
        }
        
        if(!$(event.target).closest('#contact-popup').length) {
        	$('contact-popup').hide();
        }
    });
	
});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".contact-img").forEach(function (element) {
        let randomColor = "#" + Math.floor(Math.random() * 16777215).toString(16);
        element.style.backgroundColor = randomColor;
    });
});

</script>
<style>
@import
	url('https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap')
	;

body {
	display: grid;
	place-items: center;
	height: 100vh;
	font-family: "Poppins", serif;
	margin: 0;
	background: #f2f2f2;
}

thead {
	position: sticky;
	top: 0;
	padding: 10px;
	background: white;
	z-index: 1;
}

tbody {
	overflow: contain;
	position: sticky;
}

th, td {
	padding: 10px;
	border: none;
	text-align: left;
}

th {
	background: #f8f9fa
}

.navbar {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px 20px;
	width: 98%;
	background: white;
}

.title {
	font-weight: bold;
	font-size: 20px;
}

.action-container {
	display: flex;
	gap: 30px;
}

#profile-pop-up {
	display: flex;
	flex-direction: column;
	padding: 5px;
	background: white;
	position: absolute;
	right: 2px;
	top: 60px;
	border: .2px solid;
	border-radius: 3px;
}

#profile-pop-up>* {
	border-radius: 3px;
	padding: 10px;
}

#profile-pop-up>*:hover {
	background: #f2f2f2;
}

#profile, #addcontact {
	cursor: pointer;
}

.overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.6);
	display: none; /* Hidden by default */
	z-index: 1000;
}

/* Centered Popup */
.contact {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 400px;
	padding: 20px;
	background: #f8f9fa;
	border-radius: 10px;
	box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
	font-family: Arial, sans-serif;
	z-index: 1001; /* Above overlay */
}

.popup-contactdetail, .popup-numbers, .popup-mails, .popup-groups {
	padding: 10px;
	background: #ffffff;
	margin-bottom: 10px;
	border-radius: 5px;
	border: 1px solid #ddd;
}

.popup-contactdetail div {
	margin-bottom: 5px;
}

.button-container {
	display: flex;
	justify-content: space-between;
	margin-top: 10px;
}

.button {
	padding: 8px 15px;
	background: #007bff;
	color: white;
	border-radius: 5px;
	text-align: center;
	cursor: pointer;
	flex-grow: 1;
	margin: 0 5px;
}

.button.delete {
	background: #dc3545;
}

.button.close {
	background: #6c757d;
}

.contacts-container {
	height: 80vh;
	width: 80%;
	background: white;
	border-radius: 5px;
	margin: 20px;
	padding: 30px;
	box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
}

.contact-headings {
	display: grid;
	grid-template-columns: 0.5fr 2fr 2fr 2fr;
	padding: 10px;
	border-bottom: 1px solid #ddd;
	font-weight: bold;
	text-align: left;
}

.contact-details {
	font-weight: normal;
	background: #f9f9f9;
	border-radius: 5px;
	padding: 10px;
	margin: 5px 0;
	overflow: scroll;
	height: 75vh;
}

.contact-detail {
	cursor: pointer;
	display: grid;
	grid-template-columns:0.5fr 2fr 2fr 2fr;
	padding: 10px;
	text-align: left;
	overflow: hidden;
	background: #dfdfdf;
	padding: 3px;
	margin: 2px;
}
.contact-img {
	border-radius: 50%;
	text-align:center;
	width: 30px;
	height: 30px;
	font-weight: bolder;
}
.contact-details:nth-child(even) {
	background: #f1f1f1;
}
</style>
</head>
<body>
	<div class="navbar">
		<div class="title">Contacts</div>
		<div class="action-container">
			<div id="addcontact" title="add new contact">
				<img width="40" height="40"
					src="https://img.icons8.com/stickers/100/plus-2-math.png"
					alt="plus-2-math" />
			</div>
			<div id="profile">
				<img width="40" height="40"
					src="https://img.icons8.com/stickers/100/user-male-circle.png"
					alt="user-male-circle" />
				<div id="profile-pop-up" style="display: none">
					<div id="viewprofile">view profile</div>
					<div id="logout">logout</div>
				</div>
			</div>
		</div>
	</div>
	<form action="syncContacts" method="post">

		<input type="hidden" name="provider" value="google"> <input
			type="submit" value="Sync contacts from Google">
	</form>
	<div class="contacts-container">
		<%
		if (contacts.size() > 0 && contacts != null) {
		%>
		<div class="contact-headings">
			 <div class="contact-im"></div>
			<div class="first-name">First Name</div>
			<div class="last-name">Last Name</div>
			<div class="created-at">Created At</div>
		</div>

		<div class="contact-details">
			<%
			for (ContactsObj contact : contacts) {
			%>

			<div class="contact-detail" title="<%=contact.getFirstName()%>">
			
				<div class="contact-id" style="display: none"><%=contact.getContactId()%></div>
				<div class="contact-img " style="align-content: center"><%=contact.getFirstName().charAt(0) %></div>
				<div class="first-name"><%=contact.getFirstName()%></div>
				<div class="last-name"><%=contact.getLastName()%></div>
	<div class="created-at">
					<%=LocalDateTime.ofInstant(Instant.ofEpochMilli(contact.getCreatedAt()), ZoneId.of("Asia/Kolkata"))
		.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"))%>
				</div>
			</div>
			<%
			}
			%>
		</div>
		<%
		} else {
		%>
		<p>No contacts found. Create to see here.</p>
		<%
		}
		%>
	</div>
	<div class="overlay" id="contact-popup">
		<!-- Popup -->
		<div class="contact">
			<div class="popup-contactid" style="display: none"></div>
			<div class="popup-contactdetail">
				<div class="FirstName">
					First Name: <span></span>
				</div>
				<div class="LastName">
					Last Name: <span></span>
				</div>
				<div class="createdAt">
					Created At: <span></span>
				</div>
			</div>
			<div class="popup-numbers">
				Mobile Numbers: <span></span>
			</div>
			<div class="popup-mails">
				Mails: <span></span>
			</div>
			<div class="popup-groups">
				Groups: <span></span>
			</div>
			<div class="button-container">
				<div class="button edit">Edit</div>
				<div class="button delete">Delete</div>
				<div class="button close" id="popup-close">Close</div>
			</div>
		</div>
	</div>


</body>
</html>