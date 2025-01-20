package com.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.AccessTokensDao;
import com.dao.ClientDetailsDao;
import com.dao.ContactMailsDao;
import com.dao.ContactMobileNumbersDao;
import com.dao.ContactsDao;
import com.dao.DaoException;
import com.dao.UserDetailsDao;
import com.dao.UserMailsDao;
import com.dbObjects.AccessTokensObj;
import com.dbObjects.ContactsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;

/**
 * Servlet implementation class ResourceContacts
 */
@WebServlet("/api/v1/resources/contacts")
public class ResourceContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static AppLogger logger = new AppLogger(ResourceContacts.class.getName());
	private static final HashMap<String, Integer> SCOPES = new HashMap<>();

    static {
        SCOPES.put("profile", 1);
        SCOPES.put("profile.read", 2);
        SCOPES.put("contacts.read", 3);
        SCOPES.put("contacts", 4);
    }
    AccessTokensDao accessDao = new AccessTokensDao();
    ContactsDao contactsDao = new ContactsDao();
    ContactMobileNumbersDao mobileNumberDao = new ContactMobileNumbersDao();
    ContactMailsDao mailsDao = new ContactMailsDao();
	ClientDetailsDao clientDao = new ClientDetailsDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourceContacts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();
		String accessToken  = request.getHeader("Bearer");
		String personFields = request.getParameter("personFields");
		
		
		
		if(accessToken == null) {
			responseJson.addProperty("error", "Access Token Header required");
			response.setStatus(400);
			response.getWriter().print(responseJson);
			return;
		}
		
		try {
			AccessTokensObj tokenObject = accessDao.getAccessTokenObject(accessToken);
			if(tokenObject == null) {
				responseJson.addProperty("error", "Invalid access token");
				response.setStatus(400);
				response.getWriter().print(responseJson);
				return;
			}
			
			if(!(tokenObject.getScopes().contains(SCOPES.get("contacts.read"))||tokenObject.getScopes().contains(SCOPES.get("conacts.read")))){
				response.setStatus(401);
				
			}
			
			List<ContactsObj> contacts = contactsDao.getContactsWithUserId(tokenObject.getUser_id());
			if(contacts == null) {
				response.setStatus(404);
				responseJson.addProperty("message", "No contacts found");
				response.getWriter().print(responseJson);
				return;
			}
			else {
				responseJson.add("contacts", new Gson().toJsonTree(contacts));
				response.setStatus(200);
				
				
			}
			
		} catch (QueryException | DaoException e) {
			
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage(),e);
			responseJson.addProperty("error", "Something went wrong, try again later.");
			response.setStatus(500);
			response.getWriter().print(responseJson);
			return;
		}
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
