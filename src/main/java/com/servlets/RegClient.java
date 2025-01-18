package com.servlets;

import java.io.IOException;
import java.util.HashMap;
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
import com.enums.RedirectUris;
import com.filters.SessionFilter;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;
import com.util.ClientCredentialsGenerator;
import com.util.IdGenerator;


@WebServlet("/api/v1/regclient")
public class RegClient extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static AppLogger logger = new AppLogger(RegClient.class.getName());
    private static final HashMap<String, Integer> SCOPES = new HashMap<>();
    
    static {
        SCOPES.put("profile", 1);
        SCOPES.put("profile.read", 2);
        SCOPES.put("contacts.read", 3);
        SCOPES.put("contacts", 4);
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegClient() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appName = request.getParameter("appName");
        String[] redirectUris = request.getParameterValues("redirectUris[]");
        String appType = request.getParameter("appType");
        String[] scopes = request.getParameterValues("scopes"); // Fetch scopes from the form

        // Log the received data for debugging
        System.out.println("App Name: " + appName);
        System.out.println("App Type: " + appType);
        System.out.println("Redirect URIs:");
        for (String uri : redirectUris) {
            System.out.println(uri);
        }
        StringJoiner scopeJoiner = new StringJoiner(",");
        System.out.println("Scopes:");
        if (scopes != null) {
            for (String scope : scopes) {
                System.out.println(scope);
                scopeJoiner.add(SCOPES.get(scope).toString());
            }
        } else {
            System.out.println("No scopes selected.");
        }

        ClientCredentialsGenerator credentialsGenerator = new ClientCredentialsGenerator();
        IdGenerator idGenerator = new IdGenerator();
        String clientId = idGenerator.genenrateClientId();
        String clientSecret = idGenerator.generateClientSecret();

        ClientDetailsDao clientDetailsDao = new ClientDetailsDao();
        RedirectUrisDao redirectUrisDao = new RedirectUrisDao();

        try {
            // Add client details to the database
            clientDetailsDao.addClient(
                clientId,
                SessionFilter.user_id.get(), // Assuming user_id is set in SessionFilter
                appName,
                appType,
                clientSecret,
                scopes != null ? scopeJoiner.toString() : "" // Join scopes into a single string
            );

            // Add redirect URIs to the database
            for (String redirectUri : redirectUris) {
                redirectUrisDao.addUri(clientId, redirectUri);
            }

        } catch (DaoException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.getMessage(),e);
            response.sendError(500, "Failed to register the client. Please try again.");
            return;
        } catch (QueryException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.getMessage(),e);
            response.sendError(500, "Something went wrong while processing the query.");
            return;
        }

        // Redirect to the clients page after successful registration
        response.sendRedirect("/contacts/clients.jsp");
    }
}
