package com.servlets;

import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.SessionsDao;
import com.filters.SessionFilter;
import com.session.SessionDataManager;
import com.loggers.AppLogger;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AppLogger logger = new AppLogger(LogoutServlet.class.getName());
    private static final String COOKIE_NAME = "session_id";
    private static final String LOGIN_PAGE = "login.jsp";
    private final SessionsDao sessionDao = new SessionsDao();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleLogout(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Integer userId = SessionFilter.USER_ID.get();
            if (userId != null) {
                cleanupSession(userId);
            }
            String sessionId = null;
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies) {
            	if(cookie.getName().equals(COOKIE_NAME)) {
            		sessionId = cookie.getValue();
            	}
            }
            if(sessionId != null) {
            	sessionDao.deleteSession(sessionId);
            }
            invalidateSessionCookie(response);
        
            redirectToLogin(response);
            
            logger.log(Level.INFO, "User successfully logged out: " + userId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during logout process", e);
            sendErrorResponse(response);
        }
    }

    private void cleanupSession(Integer userId) {
        SessionDataManager.usersData.remove(userId);
    }
    private void invalidateSessionCookie(HttpServletResponse response) {
    	
        Cookie sessionCookie = new Cookie(COOKIE_NAME, "");
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/contacts");        
        sessionCookie.setHttpOnly(true);    
        sessionCookie.setSecure(true);      
        response.addCookie(sessionCookie);
    }

    private void redirectToLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(LOGIN_PAGE);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during logout process");
    }
}