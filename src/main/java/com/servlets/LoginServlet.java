package com.servlets;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.NewDao;
import com.dao.UserDetailsDao;
import com.dbObjects.UserDetailsObj;
import com.dto.SessionData;
import com.handlers.LoginHandler;
import com.session.Session;
import com.session.SessionDataManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("login.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("action") != null) {
	        if (request.getParameter("action").equals("googleLogin")) {
	            // Redirect to Google's OAuth 2.0 endpoint for authorization
	            String clientId = "278443584424-q0nbt03i92268ici8ktj5uhi3ee65044.apps.googleusercontent.com";
	            String redirectUri = "http://localhost:8280/contacts/glogin"; // e.g., http://localhost:8080/yourapp/oauth2callback
	            String scope = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/contacts";
	            String state = "xyz"; // Optional, use a random state for security.

	            String authorizationUrl = "https://accounts.google.com/o/oauth2/auth" +
	                "?client_id=" + clientId +
	                "&redirect_uri=" + redirectUri +
	                "&response_type=code" +
	                "&scope=" + scope +
	                "&access_type=online" +
	                "&state=" + state;
	            
	            String session_id = Session.getSessionId();
	    		Long created_at = System.currentTimeMillis();
	    		Long last_accessed_at = created_at;
	    		Long expires_at = created_at + 60000*30; // 30 minute expiration
	    		NewDao.insertUserInSessions(session_id,1,created_at,last_accessed_at);
	    		// Store session data
	    		SessionDataManager.session_data.put(session_id, new SessionData(
	    		    1, 
	    		    created_at, 
	    		    last_accessed_at,
	    		    expires_at 
	    		));
//	    		SessionDataManager.users_data.put(, user);
//	    		System.out.println(SessionDataManager.session_data);
//	    		System.out.println(SessionDataManager.users_data);
	    		// Set the session ID in a cookie
	    		Cookie sessionCookie = new Cookie("session_id", session_id);
	    		sessionCookie.setMaxAge(30 * 60); // 30 minutes
	    		response.addCookie(sessionCookie);

	            response.sendRedirect(authorizationUrl);
	            return;
	        }
	    }
		
		UserDetailsObj  user = LoginHandler.validateUser(request.getParameter("mail"), request.getParameter("password"));
		
	
//		System.out.println(user);
		if (user == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid login details");
			return;
		}
		
//		System.out.println("request parameter map :"+request.getParameterMap());

		// Create a new session ID
		String session_id = Session.getSessionId();
		Long created_at = System.currentTimeMillis();
		Long last_accessed_at = created_at;
		Long expires_at = created_at + 60000*30; // 30 minute expiration
		NewDao.insertUserInSessions(session_id,user.getUserId(),created_at,last_accessed_at);
		// Store session data
		SessionDataManager.session_data.put(session_id, new SessionData(
		    user.getUserId(), 
		    created_at, 
		    last_accessed_at,
		    expires_at 
		));
		SessionDataManager.users_data.put(user.getUserId(), user);
//		System.out.println(SessionDataManager.session_data);
//		System.out.println(SessionDataManager.users_data);
		// Set the session ID in a cookie
		Cookie sessionCookie = new Cookie("session_id", session_id);
		sessionCookie.setMaxAge(30 * 60); // 30 minutes
		response.addCookie(sessionCookie);

		response.sendRedirect("profile.jsp");
	}
}
