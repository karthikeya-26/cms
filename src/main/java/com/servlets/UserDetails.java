package com.servlets;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DaoException;
import com.dao.UserDetailsDao;
import com.dbObjects.UserDetailsObj;

import com.google.gson.*;

/**
 * Servlet implementation class Users
 */
@WebServlet("/users")
public class UserDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = -1 ;
		
		if(userId == -1) {
			response.sendError(403,"Please login and try again");
			return;
		}
		
		UserDetailsObj user ;
		
		try {
			user = new UserDetailsDao().getUserWithId(userId);
		} catch (DaoException e) {
			response.sendError(404, "User not found");
			return;
		}
		
		System.out.println("user "+user);
		response.setContentType("application/json");
		if(user!= null) {
			response.getWriter().print(new Gson().toJson(user));
		}else {
			response.sendError(403,"Please Login and Try again");
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("applocation/json");
		
		Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		        Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
		String userName = request.getParameter("userName");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String contactType = request.getParameter("contactType");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if( userName == null || userName.isEmpty()) {
			response.sendError(400, "User name cant be empty");
			return;
		}
		if(firstName == null || firstName.isEmpty()){
			response.sendError(400, "first name cant be empty");
			return;
		}
		if(lastName == null || lastName.isEmpty()){
			response.sendError(400, "last name cant be empty");
			return;
		}
		if(contactType == null || contactType.isEmpty()){
			response.sendError(400, "contact type be empty");
			return;
		}
		if(email == null || email.isEmpty()) {
			response.sendError(400,"emailAddress cant be empty");
		}
		if(password == null || password.isEmpty()){
			response.sendError(400, "password cant be empty");
			return;
		}
		
		if(!emailPattern.matcher(email).matches()) {
			response.sendError(400, "provide a valid email");
			return;
		}
		
		if(!passwordPattern.matcher(password).matches()) {
			response.sendError(400, "provide a valid password");
		}
		
		UserDetailsDao userDao = new UserDetailsDao();
		try {
			userDao.createUser(userName, firstName, lastName, contactType);
		} catch (DaoException e) {
			response.sendError(500, "please try again");
			return;
		}
		
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("message", "user created successfully");
		response.getWriter().print(responseJson);
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId =-1;
		if(userId == -1) {
			response.sendError(403, "Forbidden, please login and try again");
			return;
		} 
		
		
		String userName = request.getParameter("userName");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String contactType = request.getParameter("contactType");
		
		UserDetailsDao userDao = new UserDetailsDao();
		try {
			userDao.updateUser(userId, userName, firstName, lastName, contactType);
		} catch (DaoException e) {
			response.sendError(500, "some error occured, try again later");
			return;
		}
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("status", true);
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId =-1;
		if(userId == -1) {
			response.sendError(403, "please login and try again");
			return;
		}
		
		UserDetailsDao userDao = new UserDetailsDao();
		try {
			userDao.deleteUser(userId);
		} catch (DaoException e) {
			response.sendError(500, "some thing went wrong please try again later");
			return;
		}
		
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("status", true);
		response.getWriter().print(responseJson);
	}

	/**
	 * @see HttpServlet#doOptions(HttpServletRequest, HttpServletResponse)
	 */
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
