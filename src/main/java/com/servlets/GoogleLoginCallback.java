package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.*;
import com.dbObjects.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.handlers.GoogleContactsSyncHandler;
import com.loggers.AppLogger;
import com.session.SessionDataManager;
import com.util.IdGenerator;

@WebServlet("/glogincallback")
public class GoogleLoginCallback extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final AppLogger logger = new AppLogger(GoogleLoginCallback.class.getCanonicalName());
    private static final String REDIRECT_URI = "http://localhost:8280/contacts/glogincallback";
    private static final String PEOPLE_API_URL = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final int COOKIE_MAX_AGE = 3600;
    
    private final UserDetailsDao userDao = new UserDetailsDao();
    private final UserMailsDao mailDao = new UserMailsDao();
    private final SessionsDao sessionDao = new SessionsDao();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String authCode = validateAuthCode(request, response);
            System.out.println("ho ");
            if (authCode == null) {
            System.out.println("sunni");
            return;}
            String accessToken = getAccessToken(authCode, response);
            System.out.println("sunni 2");
            if (accessToken == null) {
            	System.out.println("2222");
            	return;
            }
            System.out.println(accessToken);
            processUserData(accessToken, response);
        } catch (Exception e) {
        	System.out.println("Exception :"+e.getLocalizedMessage());
        	e.printStackTrace();
            logger.log(Level.SEVERE, "Error processing OAuth callback", e);
            sendErrorResponse(response, "Authentication failed");
        }
    }

    private String validateAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authCode = request.getParameter("code");
        if (authCode == null) {
            sendErrorResponse(response, "Authentication failed");
            return null;
        }
        return authCode;
    }

    private String getAccessToken(String authCode, HttpServletResponse response) throws IOException {
        String params = buildTokenRequestParams(authCode);
        HttpURLConnection conn = createConnection(TOKEN_URL, "POST");
        
        if (conn == null) {
            sendErrorResponse(response, "Authentication failed");
            return null;
        }

        writeRequestBody(conn, params);
        return handleTokenResponse(conn, response);
    }

    private String buildTokenRequestParams(String authCode) throws IOException {
        return String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
            URLEncoder.encode(authCode, "UTF-8"),
            URLEncoder.encode(GoogleContactsSyncHandler.getClientid(), "UTF-8"),
            URLEncoder.encode(GoogleContactsSyncHandler.getClientsecret(), "UTF-8"),
            URLEncoder.encode(REDIRECT_URI, "UTF-8"));
    }

    private void processUserData(String accessToken, HttpServletResponse response) throws IOException {
        HttpURLConnection conn = createConnection(PEOPLE_API_URL, "GET");
        if (conn == null) {
        	System.out.println("connectio n null");
            sendErrorResponse(response, "Authentication failed");
            return;
        }

        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        JsonObject userData = readJsonResponse(conn);
        System.out.println("user data :"+userData);
        if (userData == null) {
        	System.out.println("null user");
            sendErrorResponse(response, "Authentication failed");
            return;
        }
        System.out.println("handling user data");
        handleUserAuthentication(userData, response);
    }

    private void handleUserAuthentication(JsonObject userData, HttpServletResponse response) throws IOException {
        try {
            String accountId = userData.get("resourceName").getAsString();
            JsonObject names = userData.get("names").getAsJsonArray().get(0).getAsJsonObject();
            JsonObject emailAddresses = userData.get("emailAddresses").getAsJsonArray().get(0).getAsJsonObject();

            UserDetailsObj user = userDao.getUserWithAccountId(accountId);
            System.out.println("user ;"+user);
            if (user != null) {
                handleExistingUser(user, response);
            } else {
            	System.out.println("new user");
                handleNewUser(names, emailAddresses, accountId, response);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.log(Level.SEVERE, "Error handling user authentication", e);
            sendErrorResponse(response, "Authentication failed");
        }
    }

    private void handleExistingUser(UserDetailsObj user, HttpServletResponse response) throws IOException, DaoException {
        String sessionId = createUserSession(user.getUserId());
        setSessionCookie(sessionId, response);
        response.sendRedirect("profile.jsp");
    }

    private void handleNewUser(JsonObject names, JsonObject emailAddresses, String accountId, HttpServletResponse response) throws IOException, DaoException {
        int userId = userDao.createUser(
            names.get("displayName").getAsString(),
            names.get("givenName").getAsString(),
            names.get("displayNameLastFirst").getAsString().split(" ")[0],
            "private",
            accountId
        );

        mailDao.addMailForUser(userId, emailAddresses.get("value").getAsString());
        String sessionId = createUserSession(userId);
        setSessionCookie(sessionId, response);
        response.sendRedirect("profile.jsp");
    }

    private String createUserSession(int userId) throws DaoException {
        String sessionId = idGenerator.generateSessionId();
        Long currentTime = Instant.now().toEpochMilli();
        
        SessionsObj session = new SessionsObj(sessionId, userId, currentTime, currentTime);
        SessionDataManager.sessionData.put(sessionId, session);
        SessionDataManager.usersData.put(userId, userDao.getUserWithId(userId));
        sessionDao.insertSession(sessionId, userId, currentTime, currentTime);
        
        return sessionId;
    }

    private void setSessionCookie(String sessionId, HttpServletResponse response) {
        Cookie cookie = new Cookie("session_id", sessionId);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private HttpURLConnection createConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        return conn;
    }

    private void writeRequestBody(HttpURLConnection conn, String body) throws IOException {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }

    private String handleTokenResponse(HttpURLConnection conn, HttpServletResponse response) throws IOException {
        JsonObject tokenResponse = readJsonResponse(conn);
        if (tokenResponse != null && tokenResponse.has("access_token")) {
            return tokenResponse.get("access_token").getAsString();
        }
        return null;
    }

    private JsonObject readJsonResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return JsonParser.parseString(response.toString()).getAsJsonObject();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error reading response", e);
            return null;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("error", message);
        response.getWriter().print(errorJson);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}