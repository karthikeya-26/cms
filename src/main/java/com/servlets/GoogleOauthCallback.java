package com.servlets;

import com.loggers.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.ContactsSyncDao;
import com.dbObjects.ContactsSyncObj;
import com.filters.SessionFilter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.handlers.GoogleContactsSyncHandler;

@WebServlet("/goauth")
public class GoogleOauthCallback extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final AppLogger logger = new AppLogger("GoogleOauthCallback");
    private static final String ACCESS_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    
    private final GoogleContactsSyncHandler contactsSyncHandler;
    private final ContactsSyncDao contactsSyncDao;

    public GoogleOauthCallback() {
        super();
        this.contactsSyncHandler = new GoogleContactsSyncHandler();
        this.contactsSyncDao = new ContactsSyncDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String authCode = request.getParameter("code");
            System.out.println("auth code :"+authCode);
            JsonObject tokenResponse = getAccessToken(authCode);
            
            if (tokenResponse != null) {
            	System.out.println(tokenResponse.toString());
                processTokenResponse(tokenResponse, response);
            } else {
                sendErrorResponse(response, "token_error");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing OAuth callback", e);
            sendErrorResponse(response, "internal_error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    private JsonObject getAccessToken(String authCode) throws IOException {
        String params = buildTokenRequestParams(authCode);
        HttpURLConnection conn = createTokenConnection();
        
        try {
            sendRequest(conn, params);
            return handleTokenResponse(conn);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error getting access token", e);
            return null;
        }
    }

    private String buildTokenRequestParams(String authCode) throws IOException {
        return String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
            URLEncoder.encode(authCode, "UTF-8"),
            URLEncoder.encode(GoogleContactsSyncHandler.getClientid(), "UTF-8"),
            URLEncoder.encode(GoogleContactsSyncHandler.getClientsecret(), "UTF-8"),
            URLEncoder.encode(GoogleContactsSyncHandler.getRedirecturi(), "UTF-8"));
    }

    private HttpURLConnection createTokenConnection() throws IOException {
        URL url = new URL(ACCESS_TOKEN_ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        return conn;
    }

    private void sendRequest(HttpURLConnection conn, String params) throws IOException {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }

    private JsonObject handleTokenResponse(HttpURLConnection conn) throws IOException {
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readJsonResponse(conn.getInputStream());
        } else {
            logErrorResponse(conn.getErrorStream());
            return null;
        }
    }

    private JsonObject readJsonResponse(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println(response.toString());
            return JsonParser.parseString(response.toString()).getAsJsonObject();
        }
    }

    private void logErrorResponse(InputStream errorStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorResponse.append(line);
            }
            logger.log(Level.WARNING, "Error response from token endpoint: " + errorResponse.toString());
        }
    }

    private void processTokenResponse(JsonObject tokenResponse, HttpServletResponse response) throws IOException {
        String accessToken = tokenResponse.get("access_token").getAsString();
        String refreshToken = tokenResponse.get("refresh_token").getAsString();
        
        System.out.println("Access token :"+accessToken);
        System.out.println("Refresh token :"+refreshToken);
        
        String accountId = contactsSyncHandler.getAccountId(accessToken);

        if (accountId == null) {
            sendErrorResponse(response, "account_fetch_error");
            return;
        }
        
        ContactsSyncObj syncObj = new ContactsSyncObj();
        syncObj.setAccountId(accountId);
        syncObj.setRefreshToken(refreshToken);
        syncObj.setUserId(SessionFilter.USER_ID.get());

        handleAccountSync(syncObj, accessToken, response);
    }

    private void handleAccountSync(ContactsSyncObj syncObj, String accessToken, 
            HttpServletResponse response) throws IOException {
        try {
        	System.out.println(contactsSyncDao.getUserSyncTokens(syncObj.getUserId()));
        	System.out.println(syncObj.getAccountId());
            if (contactsSyncDao.getSyncObjWithAccountId(syncObj.getAccountId(), syncObj.getUserId()) == null) {
            	
                contactsSyncDao.addRefreshTokenToUser(syncObj.getUserId(),syncObj.getAccountId(), syncObj.getRefreshToken(), "GOOGLE");
                contactsSyncHandler.getAndHandleContacts(syncObj, accessToken, null);
                response.sendRedirect("usercontacts.jsp");
            } else {
                sendErrorResponse(response, "account_already_exists");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.log(Level.SEVERE, "Error syncing account", e);
            sendErrorResponse(response, "sync_error");
        }
    }

    public String getAccountId(String accessToken) {
        try {
            URL url = new URL(GoogleContactsSyncHandler.getPeopleAccountEndpoint());
          
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readJsonResponse(conn.getInputStream()).get("resoueceName").getAsString();
            } else {
                try (InputStream errorStream = conn.getErrorStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("Error Response: " + errorResponse);
                    logger.log(Level.WARNING, "Error fetching account ID: {0} ->" + errorResponse.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Error fetching account ID", e);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String errorCode) throws IOException {
        String errorPage = String.format("usercontacts.jsp?error=%s", errorCode);
        response.sendRedirect(errorPage);
    }
}