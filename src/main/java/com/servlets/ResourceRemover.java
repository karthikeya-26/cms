package com.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbObjects.SessionsObj;
import com.session.SessionDataManager;

/**
 * Servlet implementation class ResourceRemover
 * Handles session resource management operations
 */
@WebServlet("/sru")
public class ResourceRemover extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ResourceRemover.class.getName());
    
    /**
     * Handles POST requests for session management operations
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Action parameter is required");
            return;
        }
        
        try {
            switch (action) {
                case "SessionResourceUpdate":
                    handleSessionUpdate(request, response);
                    break;
                case "SessionResourceDelete":
                    handleSessionDelete(request, response);
                    break;
                default:
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "An unexpected error occurred while processing your request");
        }
    }
    
   
    private void handleSessionUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String sessionId = request.getParameter("session_id");
        String lastAccessedTimeStr = request.getParameter("last_accessed_time");
        
        if (sessionId == null || sessionId.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "session_id is required");
            return;
        }
        
        if (lastAccessedTimeStr == null || lastAccessedTimeStr.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "last_accessed_time is required");
            return;
        }
        
        Long lastAccessedTime;
        try {
            lastAccessedTime = Long.parseLong(lastAccessedTimeStr);
        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                    "last_accessed_time must be a valid number");
            return;
        }
        
        // Get and update session data
        SessionsObj sessionData = SessionDataManager.sessionData.get(sessionId);
        if (sessionData == null) {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Session not found");
            return;
        }
        
        logger.fine("Before updating session: " + sessionId + 
                ", current last accessed time: " + sessionData.getLastAccessedTime());
        
        if (sessionData.getLastAccessedTime() < lastAccessedTime) {
            sessionData.setLastAccessedTime(lastAccessedTime);
            logger.fine("Updated session last accessed time to: " + lastAccessedTime);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.fine("No update needed - provided time is older than current time");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    private void handleSessionDelete(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String sessionId = request.getParameter("session_id");
        if (sessionId == null || sessionId.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "session_id is required");
            return;
        }
        
        SessionsObj removedSession = SessionDataManager.sessionData.remove(sessionId);
        if (removedSession == null) {
            logger.warning("Attempted to remove non-existent session: " + sessionId);
        } else {
            logger.fine("Successfully removed session: " + sessionId);
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) 
            throws IOException {
        logger.warning("Error in ResourceRemover: " + message);
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}