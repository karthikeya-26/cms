package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.NewDao;
import com.dbObjects.UserDetailsObj;
import com.handlers.LoginHandler;
import com.models.SessionData;
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

		UserDetailsObj user = LoginHandler.validate_user(request.getParameter("mail"), request.getParameter("password"));
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
		NewDao.insertUserInSessions(session_id,user.getUser_id(),created_at,last_accessed_at);
		// Store session data
		SessionDataManager.session_data.put(session_id, new SessionData(
		    user.getUser_id(), 
		    created_at, 
		    last_accessed_at,
		    expires_at 
		));
		SessionDataManager.users_data.put(user.getUser_id(), user);
//		System.out.println(SessionDataManager.session_data);
//		System.out.println(SessionDataManager.users_data);
		// Set the session ID in a cookie
		Cookie sessionCookie = new Cookie("session_id", session_id);
		sessionCookie.setMaxAge(30 * 60); // 30 minutes
//		sessionCookie.setPath("/"); // Cookie accessible across the entire application
		response.addCookie(sessionCookie);

		response.sendRedirect("profile.jsp");
	}
}
