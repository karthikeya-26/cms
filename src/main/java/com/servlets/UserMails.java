package com.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DaoException;
import com.dao.UserMailsDao;
import com.dbObjects.UserMailsObj;
import com.google.gson.JsonObject;
import com.google.gson.*;
import com.loggers.AppLogger;

/**
 * Servlet implementation class UserMails
 */
@WebServlet("/usermails")
public class UserMails extends HttpServlet {
	private static AppLogger logger = new AppLogger(UserMails.class.getCanonicalName());
	private static final long serialVersionUID = 1L;
	
	private UserMailsDao mailsDao = new UserMailsDao();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserMails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();
		
		try {
			List<UserMailsObj> userMails = mailsDao.getUserMails(1);
			if(userMails == null || userMails.isEmpty()) {
				responseJson.addProperty("message", "No user Mails found");
			}else {
				responseJson.add("mails", new Gson().toJsonTree(userMails));
			}
			response.getWriter().print(responseJson);
		} catch (DaoException e) {
			logger.log(Level.SEVERE, e.getMessage(),e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Some error occured, try again later.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		JsonObject responseJson  = new JsonObject();
		
		try {
			JsonObject reqPayLoad = JsonParser.parseReader(request.getReader()).getAsJsonObject();
			
			if(!reqPayLoad.has("emailAddress")) {
				responseJson.addProperty("message", "Provide a  mail address");
				response.getWriter().print(responseJson);
				return;
			}
			
		    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); // Valid emails
		    
		    if(!emailPattern.matcher(reqPayLoad.get("emailAddress").getAsString()).matches()){
		    	responseJson.addProperty("message", "provide a valid mail address");
		    	response.getWriter().print(responseJson);
		    	return;
		 
		    }
		    
		    UserMailsDao dao = new UserMailsDao();
		    dao.addMailForUser(1, reqPayLoad.get("emailAddress").getAsString());
		    
		}catch(DaoException e) {
			logger.log(Level.SEVERE, e.getMessage(),e);
			response.sendError(500,"something went wrong, please try again later");
			return;
		}catch(Exception ex) {
			logger.log(Level.INFO, ex.getMessage(),ex);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid payload format");
			return;
		}
		
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		 JsonObject responseJson = new JsonObject();
		 try {
			 
			 JsonObject requestPayLoad = JsonParser.parseReader(request.getReader()).getAsJsonObject();
			 
			 if(!requestPayLoad.has("emailId")) {
				 responseJson.addProperty("message", "no mailId found");
				 response.getWriter().print(responseJson);
				 return;
			 }
			 int mailId = -1;
			 try {
				mailId = requestPayLoad.get("emailId").getAsInt();
				
			 }catch(NumberFormatException e) {
				 responseJson.addProperty("message", "not a valid mail id");
				 response.getWriter().print(responseJson);
				 return;
			 }
			 
			 if(!requestPayLoad.has("emailAddress")) {
				 responseJson.addProperty("message", "enter a mail Adrdress");
				 response.getWriter().print(responseJson);
				 return;
			 }
			 
			Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
			
			if(!emailPattern.matcher(requestPayLoad.get("emailAddress").getAsString()).matches()) {
				response.sendError(400,"Provide a valid email address");
				return;
			}
			
			mailsDao.updateMail(1, null, getServletInfo());

			
			 
			 
			 
		 }catch(Exception e){
			 response.sendError(400, "Invalid payload format");
			 return;
		 }
	}
	

}
