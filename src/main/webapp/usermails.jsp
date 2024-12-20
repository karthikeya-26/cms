<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.filters.*" %>
<%@ page import="com.dbObjects.*" %>
<%@ page import="com.session.*" %>
<%@ page import="com.dao.*" %>
<%   
	Integer user_id = SessionFilter.user_id.get();
	List<UserMailsObj> user_mails = NewDao.getUserMails(user_id);
	UserDetailsObj user = SessionDataManager.users_data.get(user_id);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
	<h2>Hi <%=user.getUserName() %></h2>
	<a href="profile.jsp">Go to profile</a>
	<h3>Add Mail</h3>
	<form action="userOp?action=addEmail" method="post">
		<label for="email">Enter mail</label>
		<input type="text" name="email" id="email">
		<input type="submit" value="Add mail">
	</form>
	
	<h2>Your Emails :</h2>
	<%for(UserMailsObj mail : user_mails){%>
		<p><%=mail.getMail() %></p>
		<form style="display:inline" action="userOp?action=setprimarymail" method="post">
			<input type="hidden" name="mail_id" value="<%=mail.getMailId()%>">
			<% if(mail.getIsPrimary()==0) {%>
				<input type="submit" value="set as Primary">
			<%}else{ %>
				<span><b>PRIMARY MAIL</b></span>
			<%} %>
		</form>
		<form action="userOp?action=deleteMail" method="post">
			<input type="hidden" name="mail_id" value="<%=mail.getMailId()%>">
			<input type="submit" value="Delete this email"> 
		</form>
		
		
	<% }%>
	<a href="profile.jsp">go to profile</a>
</body>
</html>