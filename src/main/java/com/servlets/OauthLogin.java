package com.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.*;
import com.dbObjects.ClientDetailsObj;
import com.dbObjects.RedirectUrisObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.JsonObject;
import com.handlers.LoginHandler;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;
import com.util.IdGenerator;

@WebServlet("/api/v1/oauth2/login")
public class OauthLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final HashMap<String, Integer> SCOPES = new HashMap<>();
    private static AppLogger logger= new AppLogger(OauthLogin.class.getName());
    static {
        SCOPES.put("profile", 1);
        SCOPES.put("profile.read", 2);
        SCOPES.put("contacts.read", 3);
        SCOPES.put("contacts", 4);
    }

    public OauthLogin() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, false);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response, true);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isPost) throws IOException {
        try {
            // Validate common parameters
            String clientId = getParameter(request, response, "client_id");
            String redirectUri = getParameter(request, response, "redirect_uri");
            String requestedScopes = getParameter(request, response, "scopes");
            String responseType = getParameter(request, response, "response_type");
            String accessType = getParameter(request, response, "access_type");

            validateResponseType(responseType, response);
            validateAccessType(accessType, response);

            // Validate client and redirect URI
            ClientDetailsObj clientDetails = validateClient(clientId, response);
            
            validateRedirectUri(clientDetails, redirectUri, response);
            String applicationName = clientDetails.getClient_name();
            // Validate requested scopes
            List<Integer> scopeIds = validateScopes(clientDetails, requestedScopes, response);

            if (isPost) {
                handleLoginPost(request, response, clientId, redirectUri, scopeIds, accessType);
            } else {
                renderLoginForm(response, clientId, redirectUri, requestedScopes, responseType, accessType, applicationName);
            }
        } catch (IllegalArgumentException e) {
        	logger.log(Level.WARNING, e.getMessage(),e);
            response.sendError(400, e.getMessage());
            return;
        } catch (QueryException e) {
        	logger.log(Level.WARNING, e.getMessage(),e);
            response.sendError(500, "Something went wrong, please try again.");
            return;
        }
    }

    private void handleLoginPost(HttpServletRequest request, HttpServletResponse response, String clientId, String redirectUri, List<Integer> scopeIds, String accessType) throws IOException {
        String email = getParameter(request, response, "email");
        String password = getParameter(request, response, "password");
        
//        System.out.println(email+password); 
        UserDetailsObj user = null;
		try {
			user = LoginHandler.validateUser(email, password);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			response.sendError(500,"Something went wrong, Please try again later.");
			e.printStackTrace();
		}
        if (user == null) {
            response.sendError(400, "Invalid credentials");
            return;
        }

        // Generate authorization code
        String authorizationCode = new IdGenerator().generateAuthCode();
        AuthorizationCodesDao authDao = new AuthorizationCodesDao();
        URL url = new URL(redirectUri);
        HttpURLConnection connection;
        try {
            authDao.addAuthorizationCode(authorizationCode, clientId, user.getUserId(), scopeIds.toString(), accessType);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()){
            	String out = "code="+authorizationCode;
            	os.write(out.getBytes(StandardCharsets.UTF_8));
            	os.flush();
            }
            
            int statusCode = connection.getResponseCode();
            
            
        } catch (QueryException e) {
            e.printStackTrace();
            response.sendError(500, "Error generating auth code ,pleas try again");
            return;
        }

        // Respond with the authorization code
//        JsonObject responseJson = new JsonObject();
//        responseJson.addProperty("code", authorizationCode);
//        response.setContentType("application/json");
//        response.getWriter().print(responseJson);
        return;
    }

    private void renderLoginForm(HttpServletResponse response, String clientId, String redirectUri, String requestedScopes, String responseType, String accessType,String applicationName) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("<form action='/contacts/api/v1/oauth2/login' method='post'>");
        response.getWriter().println("<input type='hidden' name='client_id' value='" + clientId + "'>");
        response.getWriter().println("<input type='hidden' name='redirect_uri' value='" + redirectUri + "'>");
        response.getWriter().println("<input type='hidden' name='scopes' value='" + requestedScopes + "'>");
        response.getWriter().println("<input type='hidden' name='response_type' value='" + responseType + "'>");
        response.getWriter().println("<input type='hidden' name='access_type' value='" + accessType + "'>");
        response.getWriter().println("<label>Email:</label><br><input type='email' name='email' required><br>");
        response.getWriter().println("<label>Password:</label><br><input type='password' name='password' required><br>");
        response.getWriter().println("<input type='submit' value='Login'>");
        response.getWriter().println("<p>By logging in,"+applicationName+" can access: " + requestedScopes + "</p>");
        response.getWriter().println("</form>");
    }

    private String getParameter(HttpServletRequest request, HttpServletResponse response, String paramName) throws IOException {
        String value = request.getParameter(paramName);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing parameter: " + paramName);
        }
        return value;
    }

    private void validateResponseType(String responseType, HttpServletResponse response) throws IOException {
        if (!"code".equals(responseType)) {
            throw new IllegalArgumentException("Invalid response type");
        }
    }

    private void validateAccessType(String accessType, HttpServletResponse response) throws IOException {
        List<String> validAccessTypes = Arrays.asList("offline", "online");
        if (!validAccessTypes.contains(accessType)) {
            throw new IllegalArgumentException("Invalid access type");
        }
    }

    private ClientDetailsObj validateClient(String clientId, HttpServletResponse response) throws QueryException, IOException {
        ClientDetailsDao clientDao = new ClientDetailsDao();
        ClientDetailsObj clientDetails = clientDao.getClient(clientId);
        if (clientDetails == null) {
            throw new IllegalArgumentException("Invalid client");
        }
        return clientDetails;
    }

    private void validateRedirectUri(ClientDetailsObj clientDetails, String redirectUri, HttpServletResponse response) throws QueryException, IOException {
        RedirectUrisDao urisDao = new RedirectUrisDao();
        RedirectUrisObj uri = urisDao.getRedirectUri(clientDetails.getClient_id(), redirectUri);
        if (uri == null) {
            throw new IllegalArgumentException("Invalid redirect URI");
        }
    }

    private List<Integer> validateScopes(ClientDetailsObj clientDetails, String requestedScopes, HttpServletResponse response) throws IOException {
        List<Integer> scopeIds = new ArrayList<>();
        Set<Integer> clientScopes = clientDetails.getScopes();
        System.out.println("client details :"+clientScopes);
        System.out.println("request Scope :"+requestedScopes);
        
        for (String scope : requestedScopes.split(" ")) {
            Integer scopeId = SCOPES.get(scope);
            
            if (scopeId == null || !clientScopes.contains(scopeId)) {
                throw new IllegalArgumentException("Invalid or unauthorized scope: " + scope);
            }
            scopeIds.add(scopeId);
        }
        System.out.println(scopeIds);
        return scopeIds;
    }
}
