package com.servlets;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.handlers.LoginHandler;
import com.session.SessionDataManager;
import com.util.IdGenerator;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    private static final int SESSION_COOKIE_MAX_AGE = 3600;
    private static final String COOKIE_NAME = "session_id";
    
    private final SessionsDao sessionsDao;
    private final IdGenerator sessionIdGenerator;
    private final LoginHandler loginHandler;

    public LoginServlet() {
        super();
        this.sessionsDao = new SessionsDao();
        this.sessionIdGenerator = new IdGenerator();
        this.loginHandler = new LoginHandler();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JsonObject requestData = parseRequestBody(request);
            processLoginRequest(requestData, response);
        } catch (JsonSyntaxException e) {
        	logger.warn(e.getMessage(),e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format");
        } catch (Exception e) {
            logger.info("Unexpected error during login", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private JsonObject parseRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonInput = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }
        }
        return JsonParser.parseString(jsonInput.toString()).getAsJsonObject();
    }

    private void processLoginRequest(JsonObject input, HttpServletResponse response) throws IOException {
        String email = getRequiredField(input, "email");
        String password = getRequiredField(input, "password");
        
        if (email == null || password == null) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Email and password are required");
            return;
        }

        try {
            UserDetailsObj user = loginHandler.validateUser(email, password);
            handleLoginResult(user, response);
        } catch (DaoException e) {
            logger.info("Database error during login", e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid login details");
        }
    }

    private String getRequiredField(JsonObject json, String fieldName) {
        return json.has(fieldName) ? json.get(fieldName).getAsString() : null;
    }

    private void handleLoginResult(UserDetailsObj user, HttpServletResponse response) throws IOException {
        if (user == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
            return;
        }

        try {
            SessionsObj session = createUserSession(user);
            updateSessionStorage(session, user);
            addSessionCookie(response, session.getSessionId());
            sendSuccessResponse(response);
        } catch (DaoException e) {
            logger.info( "Error creating user session", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating session");
        }
    }

    private SessionsObj createUserSession(UserDetailsObj user) throws DaoException {
        String sessionId = sessionIdGenerator.generateSessionId();
        Long currentTime = Instant.now().toEpochMilli();
        
        SessionsObj session = new SessionsObj(sessionId, user.getUserId(), currentTime, currentTime);
        sessionsDao.insertSession(sessionId, user.getUserId(), currentTime, currentTime);
        
        return session;
    }

    private void updateSessionStorage(SessionsObj session, UserDetailsObj user) {
        SessionDataManager.sessionData.put(session.getSessionId(), session);
        SessionDataManager.usersData.put(user.getUserId(), user);
    }

    private void addSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie sessionCookie = new Cookie(COOKIE_NAME, sessionId);
        sessionCookie.setMaxAge(SESSION_COOKIE_MAX_AGE);
        sessionCookie.setHttpOnly(true); // Protect against XSS
        sessionCookie.setSecure(true);   // Only send over HTTPS
        response.addCookie(sessionCookie);
    }

    private void sendSuccessResponse(HttpServletResponse response) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_OK, createResponseJson("success", "Login successful"));
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        sendJsonResponse(response, statusCode, createResponseJson("failed", message));
    }

    private void sendJsonResponse(HttpServletResponse response, int statusCode, JsonObject jsonResponse) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);
        
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private JsonObject createResponseJson(String status, String message) {
        JsonObject json = new JsonObject();
        json.addProperty("status", status);
        json.addProperty("message", message);
        return json;
    }
}