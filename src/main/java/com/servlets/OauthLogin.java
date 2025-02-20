package com.servlets;

import java.io.IOException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.*;
import com.dbObjects.ClientDetailsObj;
import com.dbObjects.RedirectUrisObj;
import com.dbObjects.UserDetailsObj;
import com.handlers.LoginHandler;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;
import com.util.IdGenerator;

@WebServlet("/api/v1/oauth2/login")
public class OauthLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AppLogger logger = new AppLogger(OauthLogin.class.getName());    
    private static final Map<String, Integer> SCOPES = Collections.unmodifiableMap(new HashMap<String, Integer>() {private static final long serialVersionUID = 1L;

	{
        put("profile", 1);
        put("profile.read", 2);
        put("contacts.read", 3);
        put("contacts", 4);
    }});
    
    private static final Set<String> VALID_ACCESS_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("offline", "online")));
    private static final String RESPONSE_TYPE_CODE = "code";
    
    private final ClientDetailsDao clientDao;
    private final RedirectUrisDao urisDao;
    private final AuthorizationCodesDao authCodesDao;
    private final LoginHandler loginHandler;
    private final IdGenerator idGenerator;

    public OauthLogin() {
        this.clientDao = new ClientDetailsDao();
        this.urisDao = new RedirectUrisDao();
        this.authCodesDao = new AuthorizationCodesDao();
        this.loginHandler = new LoginHandler();
        this.idGenerator = new IdGenerator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleRequest(request, response, false);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        handleRequest(request, response, true);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response, boolean isPost) 
            throws IOException {
        try {
            OAuthRequestParams params = validateAndExtractParams(request);
            ClientDetailsObj clientDetails = validateClientAndRedirectUri(params);

            if (isPost) {
                handleLoginPost(request, response, params, clientDetails);
            } else {
                renderLoginForm(response, params, clientDetails);
            }
        } catch (OAuthException e) {
            handleOAuthError(response, e);
        } catch (Exception e) {
            handleUnexpectedError(response, e);
        }
    }

    private static class OAuthRequestParams {
        final String clientId;
        final String redirectUri;
        final String scopes;
        final String responseType;
        final String accessType;
        final List<Integer> scopeIds;

        OAuthRequestParams(HttpServletRequest request) throws IOException {
            this.clientId = getRequiredParameter(request, "client_id");
            this.redirectUri = getRequiredParameter(request, "redirect_uri");
            this.scopes = getRequiredParameter(request, "scopes");
            this.responseType = getRequiredParameter(request, "response_type");
            this.accessType = getRequiredParameter(request, "access_type");
            this.scopeIds = new ArrayList<>();
        }
    }

    private static String getRequiredParameter(HttpServletRequest request, String paramName) throws IOException {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new OAuthException("Missing required parameter: " + paramName, HttpServletResponse.SC_BAD_REQUEST);
        }
        return value.trim();
    }

    private OAuthRequestParams validateAndExtractParams(HttpServletRequest request) throws IOException {
        OAuthRequestParams params = new OAuthRequestParams(request);
        
        if (!RESPONSE_TYPE_CODE.equals(params.responseType)) {
            throw new OAuthException("Invalid response_type", HttpServletResponse.SC_BAD_REQUEST);
        }
        
        if (!VALID_ACCESS_TYPES.contains(params.accessType)) {
            throw new OAuthException("Invalid access_type", HttpServletResponse.SC_BAD_REQUEST);
        }
        
        return params;
    }

    private ClientDetailsObj validateClientAndRedirectUri(OAuthRequestParams params) throws QueryException, IOException {
        ClientDetailsObj clientDetails = clientDao.getClient(params.clientId);
        if (clientDetails == null) {
            throw new OAuthException("Invalid client_id", HttpServletResponse.SC_BAD_REQUEST);
        }

        RedirectUrisObj uri = urisDao.getRedirectUri(params.clientId, params.redirectUri);
        if (uri == null) {
            throw new OAuthException("Invalid redirect_uri", HttpServletResponse.SC_BAD_REQUEST);
        }

        validateScopes(clientDetails, params);
        
        return clientDetails;
    }

    private void validateScopes(ClientDetailsObj clientDetails, OAuthRequestParams params) throws IOException {
        Set<Integer> clientScopes = clientDetails.getScopes();
        
        for (String scope : params.scopes.split(" ")) {
            Integer scopeId = SCOPES.get(scope);
            if (scopeId == null || !clientScopes.contains(scopeId)) {
                throw new OAuthException("Invalid or unauthorized scope: " + scope, HttpServletResponse.SC_BAD_REQUEST);
            }
            params.scopeIds.add(scopeId);
        }
    }

    private void handleLoginPost(HttpServletRequest request, HttpServletResponse response, 
            OAuthRequestParams params, ClientDetailsObj clientDetails) throws IOException {
        try {
            String email = getRequiredParameter(request, "email");
            String password = getRequiredParameter(request, "password");
            
            UserDetailsObj user = loginHandler.validateUser(email, password);
            if (user == null) {
                throw new OAuthException("Invalid credentials", HttpServletResponse.SC_UNAUTHORIZED);
            }

            String authCode = generateAndStoreAuthCode(user, params);
            redirectWithAuthCode(response, params.redirectUri, authCode);
            
        } catch (DaoException | QueryException e) {
            logger.log(Level.SEVERE, "Database error during login", e);
            throw new OAuthException("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String generateAndStoreAuthCode(UserDetailsObj user, OAuthRequestParams params) throws QueryException {
        String authCode = idGenerator.generateAuthCode();
        authCodesDao.addAuthorizationCode(
            authCode, 
            params.clientId, 
            user.getUserId(), 
            params.scopeIds.toString(), 
            params.accessType
        );
        return authCode;
    }

    private void redirectWithAuthCode(HttpServletResponse response, String redirectUri, String authCode) 
            throws IOException {
        URL url = new URL(redirectUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = ("code=" + authCode).getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new OAuthException("Error redirecting to client", HttpServletResponse.SC_BAD_GATEWAY);
        }
    }

    private void renderLoginForm(HttpServletResponse response, OAuthRequestParams params, 
            ClientDetailsObj clientDetails) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        StringBuilder html = new StringBuilder()
            .append("<!DOCTYPE html>")
            .append("<html><head><title>Login</title></head><body>")
            .append("<form action='/contacts/api/v1/oauth2/login' method='post'>")
            .append(createHiddenField("client_id", params.clientId))
            .append(createHiddenField("redirect_uri", params.redirectUri))
            .append(createHiddenField("scopes", params.scopes))
            .append(createHiddenField("response_type", params.responseType))
            .append(createHiddenField("access_type", params.accessType))
            .append("<div>")
            .append("<label for='email'>Email:</label><br>")
            .append("<input type='email' id='email' name='email' required><br>")
            .append("<label for='password'>Password:</label><br>")
            .append("<input type='password' id='password' name='password' required><br>")
            .append("<input type='submit' value='Login'>")
            .append("</div>")
            .append("<p>By logging in, ").append(clientDetails.getClientName())
            .append(" can access: ").append(params.scopes).append("</p>")
            .append("</form></body></html>");
        
        response.getWriter().write(html.toString());
    }

    private String createHiddenField(String name, String value) {
        return String.format("<input type='hidden' name='%s' value='%s'>", 
            escapeHtml(name), escapeHtml(value));
    }

    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private void handleOAuthError(HttpServletResponse response, OAuthException e) throws IOException {
        logger.log(Level.WARNING, "OAuth error: " + e.getMessage(), e);
        response.sendError(e.getStatusCode(), e.getMessage());
    }

    private void handleUnexpectedError(HttpServletResponse response, Exception e) throws IOException {
        logger.log(Level.SEVERE, "Unexpected error in OAuth flow", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
            "An unexpected error occurred. Please try again later.");
    }

    private static class OAuthException extends RuntimeException {
        private static final long serialVersionUID = 1L;
		private final int statusCode;

        OAuthException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        int getStatusCode() {
            return statusCode;
        }
    }
}