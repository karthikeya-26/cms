package com.servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.*;
import com.dbObjects.AccessTokensObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loggers.AppLogger;

@WebServlet("/api/v1/resource/profile")
public class ResourceProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String BEARER_HEADER = "Bearer";
    private static final long TOKEN_EXPIRY_TIME = 60 * 60 * 1000; // 1 hour
    private static final Gson GSON = new Gson();
    
    private static final Map<String, Integer> SCOPES = new HashMap<String, Integer>();
    static {
    	SCOPES.put( "profile", 1);
    	SCOPES.put("profile.read", 2);
    	SCOPES.put("contacts.read", 3);
    	SCOPES.put("contacts", 4);
    }
   
    private final AppLogger logger;
    private final AccessTokensDao accessDao;
    private final UserDetailsDao userDao;
    
    public ResourceProfile() {
        this.logger = new AppLogger(ResourceProfile.class.getName());
        this.accessDao = new AccessTokensDao();
        this.userDao = new UserDetailsDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            AccessTokensObj token = validateAccessToken(request, response);
            if (token == null) {
                return;
            }

            if (!isTokenValid(token)) {
                handleTokenExpiry(response, token.getAccessToken());
                return;
            }

            if (!hasRequiredScope(token, "profile", "profile.read")) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, 
                         "Insufficient permissions for this operation");
                return;
            }

            handleGetProfile(response, token.getUserId());
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            AccessTokensObj token = validateAccessToken(request, response);
            if (token == null) {
                return;
            }

            if (!hasRequiredScope(token, "profile")) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, 
                         "Insufficient permissions for this operation");
                return;
            }

            ProfileUpdateRequest updateRequest = validateUpdateRequest(request);
            if (updateRequest == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                         "Invalid or missing profile parameters");
                return;
            }

            updateUserProfile(token.getUserId(), updateRequest);
            sendResponse(response, HttpServletResponse.SC_OK, null);
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private void handleGetProfile(HttpServletResponse response, int userId) 
            throws IOException {
        try {
            UserDetailsObj user = userDao.getUserWithId(userId);
            if (user == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
            sendResponse(response, HttpServletResponse.SC_OK, user);
        } catch (Exception e) {
            throw new IOException("Error retrieving user profile", e);
        }
    }

    private AccessTokensObj validateAccessToken(HttpServletRequest request, 
            HttpServletResponse response) throws IOException {
        String accessToken = request.getHeader(BEARER_HEADER);
        if (accessToken == null || accessToken.trim().isEmpty()) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                     "Access Token Header required");
            return null;
        }

        try {
            AccessTokensObj token = accessDao.getAccessTokenObject(accessToken);
            if (token == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, 
                         "Invalid access token");
                return null;
            }
            return token;
        } catch (Exception e) {
            throw new IOException("Error validating access token", e);
        }
    }

    private boolean isTokenValid(AccessTokensObj token) {
        return token.getCreatedAt() + TOKEN_EXPIRY_TIME >= Instant.now().toEpochMilli();
    }

    private void handleTokenExpiry(HttpServletResponse response, String token) 
            throws IOException {
        try {
            accessDao.deleteAccessToken(token);
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting expired token", e);
            throw new IOException("Error handling token expiry", e);
        }
    }

    @SuppressWarnings("unlikely-arg-type")
	private boolean hasRequiredScope(AccessTokensObj token, String... requiredScopes) {
        for (String scope : requiredScopes) {
            if (token.getScopes().contains(String.valueOf(SCOPES.get(scope)))) {
                return true;
            }
        }
        return false;
    }

    private static class ProfileUpdateRequest {
        private final String userName;
        private final String firstName;
        private final String lastName;
        private final String contactType;

        private ProfileUpdateRequest(HttpServletRequest request) {
            this.userName = sanitizeInput(request.getParameter("user_name"));
            this.firstName = sanitizeInput(request.getParameter("first_name"));
            this.lastName = sanitizeInput(request.getParameter("last_name"));
            this.contactType = sanitizeInput(request.getParameter("contact_type"));
        }

        private boolean isValid() {
            return userName != null || firstName != null || lastName != null;
        }
    }

    private ProfileUpdateRequest validateUpdateRequest(HttpServletRequest request) {
        if (request.getParameterMap().isEmpty()) {
            return null;
        }
        ProfileUpdateRequest updateRequest = new ProfileUpdateRequest(request);
        return updateRequest.isValid() ? updateRequest : null;
    }

    private void updateUserProfile(int userId, ProfileUpdateRequest request) 
            throws DaoException {
        userDao.updateUser(userId, request.userName, request.firstName, 
                          request.lastName, request.contactType);
    }

    private static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^a-zA-Z0-9._-]", "");
    }

    private void sendResponse(HttpServletResponse response, int status, Object content) 
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().print(GSON.toJson(content));
    }

    private void sendError(HttpServletResponse response, int status, String message) 
            throws IOException {
        JsonObject error = new JsonObject();
        error.addProperty("error", message);
        sendResponse(response, status, error);
    }

    private void handleException(HttpServletResponse response, Exception e) 
            throws IOException {
        logger.log(Level.SEVERE, "Error processing request", e);
        sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                 "An unexpected error occurred. Please try again later.");
    }
}