package com.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.handlers.GoogleContactsSyncHandler;
import com.loggers.AppLogger;

@WebServlet("/glogin")
public class GoogleLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final AppLogger logger = new AppLogger(GoogleLogin.class.getCanonicalName());
    private static final String REDIRECT_URI = "http://localhost:8280/contacts/glogincallback";
    private static final String RESPONSE_TYPE = "code";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String authUrl = buildAuthorizationUrl();
            response.sendRedirect(authUrl);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing Google login", e);
            handleError(response);
        }
    }

    private String buildAuthorizationUrl() {
        try {
            return String.format("%s?%s",
                GoogleContactsSyncHandler.getAccountsEndpoint(),
                buildQueryParams());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error building authorization URL", e);
            throw new RuntimeException("Failed to initialize authentication");
        }
    }

    private String buildQueryParams() {
        try {
            return String.format("client_id=%s&redirect_uri=%s&scope=%s&response_type=%s",
                URLEncoder.encode(GoogleContactsSyncHandler.getClientid(), StandardCharsets.UTF_8.name()),
                URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8.name()),
                URLEncoder.encode("openid " + GoogleContactsSyncHandler.getProfileScope(), StandardCharsets.UTF_8.name()),
                RESPONSE_TYPE);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error building query parameters", e);
            throw new RuntimeException("Failed to build authentication parameters");
        }
    }

    private void handleError(HttpServletResponse response) throws IOException {
        response.sendRedirect("error.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}