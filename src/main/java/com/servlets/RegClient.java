package com.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.ClientDetailsDao;
import com.dao.DaoException;
import com.dao.RedirectUrisDao;
import com.filters.SessionFilter;
import com.loggers.AppLogger;
import com.util.IdGenerator;
import com.google.gson.JsonObject;

@WebServlet("/api/v1/regclient")
public class RegClient extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String REDIRECT_SUCCESS_PATH = "/contacts/clients.jsp";
    
    private static final Map<String, Integer> SCOPES = new HashMap<String, Integer>();
    static {
    	SCOPES.put( "profile", 1);
    	SCOPES.put("profile.read", 2);
    	SCOPES.put("contacts.read", 3);
    	SCOPES.put("contacts", 4);
    }
    
    private final AppLogger logger;
    private final ClientDetailsDao clientDetailsDao;
    private final RedirectUrisDao redirectUrisDao;
    private final IdGenerator idGenerator;

    public RegClient() {
        this.logger = new AppLogger(RegClient.class.getName());
        this.clientDetailsDao = new ClientDetailsDao();
        this.redirectUrisDao = new RedirectUrisDao();
        this.idGenerator = new IdGenerator();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ClientRegistrationRequest regRequest = validateRequest(request);
            if (regRequest == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, 
                         "Invalid registration request. Please check all required fields.");
                return;
            }

            String clientId = idGenerator.genenrateClientId();
            String clientSecret = idGenerator.generateClientSecret();
            
            registerClient(clientId, clientSecret, regRequest);
            
            response.sendRedirect(REDIRECT_SUCCESS_PATH);
            
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private static class ClientRegistrationRequest {
        private final String appName;
        private final String[] redirectUris;
        private final String appType;
        private final String[] scopes;
        private final String scopesString;

        private ClientRegistrationRequest(HttpServletRequest request) {
            this.appName = request.getParameter("appName");
            this.redirectUris = request.getParameterValues("redirectUris");
            this.appType = request.getParameter("appType");
            this.scopes = request.getParameterValues("scopes");
            this.scopesString = convertScopesToString(scopes);
        }

        private boolean isValid() {
            return appName != null && !appName.trim().isEmpty() &&
                   redirectUris != null && redirectUris.length > 0 &&
                   appType != null && !appType.trim().isEmpty() &&
                   Arrays.stream(redirectUris).allMatch(uri -> uri != null && !uri.trim().isEmpty());
        }

        private String convertScopesToString(String[] scopes) {
            if (scopes == null || scopes.length == 0) {
                return "";
            }

            StringJoiner joiner = new StringJoiner(",");
            for (String scope : scopes) {
                Integer scopeId = SCOPES.get(scope);
                if (scopeId != null) {
                    joiner.add(scopeId.toString());
                }
            }
            return joiner.toString();
        }
    }

    private ClientRegistrationRequest validateRequest(HttpServletRequest request) {
        ClientRegistrationRequest regRequest = new ClientRegistrationRequest(request);
        return regRequest.isValid() ? regRequest : null;
    }

    private void registerClient(String clientId, String clientSecret, 
                              ClientRegistrationRequest request) throws DaoException {
        try {
            clientDetailsDao.addClient(
                clientId,
                SessionFilter.USER_ID.get(),
                request.appName,
                request.appType,
                clientSecret,
                request.scopesString
            );

            for (String redirectUri : request.redirectUris) {
                redirectUrisDao.addUri(clientId, redirectUri.trim());
            }
        } catch (Exception e) {
            throw new DaoException("Failed to register client", e);
        }
    }

    private void sendError(HttpServletResponse response, int status, String message) 
            throws IOException {
        JsonObject error = new JsonObject();
        error.addProperty("error", message);
        
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().print(error.toString());
    }

    private void handleException(HttpServletResponse response, Exception e) 
            throws IOException {
        logger.log(Level.SEVERE, "Error registering client", e);
        sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                 "Failed to register the client. Please try again later.");
    }
}