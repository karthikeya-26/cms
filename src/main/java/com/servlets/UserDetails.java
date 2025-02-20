package com.servlets;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.DaoException;
import com.dao.PasswordsDao;
import com.dao.UserDetailsDao;
import com.dao.UserMailsDao;
import com.dbObjects.UserDetailsObj;
import com.filters.SessionFilter;
import com.google.gson.*;

@WebServlet("/users")
public class UserDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	private static final Pattern PASSWORD_PATTERN = Pattern
			.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
	private static final String JSON_CONTENT_TYPE = "application/json";
	private final UserDetailsDao userDao;
	private final PasswordsDao passwordDao;
	private final UserMailsDao mailsDao;
	private final Gson gson;

	public UserDetails() {
		super();
		this.userDao = new UserDetailsDao();
		this.mailsDao = new UserMailsDao();
		this.passwordDao = new PasswordsDao();
		this.gson = new Gson();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = SessionFilter.USER_ID.get();
		if (!validateUser(userId, response)) {
			return;
		}

		try {
			UserDetailsObj user = userDao.getUserWithId(userId);
			if (user == null) {
				sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			sendJsonResponse(response, user);
		} catch (DaoException e) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDetailsObj userData = validateUserData(request, response);
		String email  = request.getParameter("email");
		String password = request.getParameter("password");
		
		boolean isValidEmail = validPattern(email, EMAIL_PATTERN);
		boolean isValidPassword = validPattern(password, PASSWORD_PATTERN);
		
		
		if (userData == null || isValidEmail == false || isValidPassword == false) {
			return;
		}

		try {
			int userId = userDao.createUser(userData.getUserName(), userData.getFirstName(), userData.getLastName(), userData.getContactType());
			mailsDao.addMailForUser(userId, email);
			passwordDao.createPassword(userId, password, 0);
			
			sendSuccessResponse(response);
		} catch (DaoException e) {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = SessionFilter.USER_ID.get();
		if (!validateUser(userId, response)) {
			return;
		}

		UserDetailsObj userData = validateUserData(request, response);
		if (userData == null) {
			return;
		}

		try {
			userDao.updateUser(userId, userData.getUserName(), userData.getFirstName(), userData.getLastName(), userData.getContactType());
			sendSuccessResponse(response);
		} catch (DaoException e) {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = SessionFilter.USER_ID.get();
		if (!validateUser(userId, response)) {
			return;
		}

		try {
			userDao.deleteUser(userId);
			sendSuccessResponse(response);
		} catch (DaoException e) {
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validateUser(int userId, HttpServletResponse response) throws IOException {
		if (userId <= 0) {
			sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		return true;
	}

	private UserDetailsObj validateUserData(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userName = request.getParameter("userName");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String contactType = request.getParameter("contactType");

		if (isNullOrEmpty(userName) || isNullOrEmpty(firstName) || isNullOrEmpty(lastName) || isNullOrEmpty(contactType)) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		return new UserDetailsObj(userName, firstName, lastName, contactType);
	}

	private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
		response.setContentType(JSON_CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(gson.toJson(data));
	}

	private void sendError(HttpServletResponse response, int status) throws IOException {
		response.sendError(status);
	}

	private void sendSuccessResponse(HttpServletResponse response) throws IOException {
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("status", true);
		sendJsonResponse(response, responseJson);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	private boolean validPattern(String value, Pattern pattern) {
		if(isNullOrEmpty(value)) {
			return false;
		}
		return pattern.matcher(value).matches();
	}

}