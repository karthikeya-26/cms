package com.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.*;
import com.dbObjects.AccessTokensObj;
import com.dbObjects.ContactsObj;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;

@WebServlet("/api/v1/resources/contacts")
public class ResourceContacts extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AppLogger logger = new AppLogger(ResourceContacts.class.getName());
    private static final Map<String, Integer> SCOPES;
    private static final Gson gson = new Gson();
    private static final String BEARER_HEADER = "Bearer";
    private static final int MAX_CONTACTS = 1000;
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_PHONE_LENGTH = 15;
    
    static {
        Map<String, Integer> scopeMap = new HashMap<>();
        scopeMap.put("profile", 1);
        scopeMap.put("profile.read", 2);
        scopeMap.put("contacts.read", 3);
        scopeMap.put("contacts", 4);
        SCOPES = Collections.unmodifiableMap(scopeMap);
    }

    private final AccessTokensDao accessDao;
    private final ContactsDao contactsDao;
    private final ContactMobileNumbersDao mobileNumberDao;
    private final ContactMailsDao mailsDao;
    private final ClientDetailsDao clientDao;

    public ResourceContacts() {
        this.accessDao = new AccessTokensDao();
        this.contactsDao = new ContactsDao();
        this.mobileNumberDao = new ContactMobileNumbersDao();
        this.mailsDao = new ContactMailsDao();
        this.clientDao = new ClientDetailsDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        JsonObject responseJson = new JsonObject();
        try {
            AccessTokensObj tokenObject = validateAccessToken(request, response);
            if (tokenObject == null) return;

            if (!hasRequiredScope(tokenObject, "contacts.read", "contacts")) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Not authorized.");
                return;
            }

            List<ContactsObj> contacts = contactsDao.getContactsWithUserId(tokenObject.getUserId());
            if (contacts == null || contacts.isEmpty()) {
                sendResponse(response, HttpServletResponse.SC_NOT_FOUND, "No contacts found");
                return;
            }

            responseJson.add("contacts", gson.toJsonTree(contacts));
            sendResponse(response, HttpServletResponse.SC_OK, responseJson);

        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            AccessTokensObj tokenObject = validateAccessToken(request, response);
            if (tokenObject == null) return;

            if (!validateInput(request)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters");
                return;
            }

            String firstName = sanitizeInput(request.getParameter("first_name"));
            String lastName = sanitizeInput(request.getParameter("last_name"));
            JsonArray numbers = parseJsonArray(request.getParameter("numbers"));
            JsonArray mails = parseJsonArray(request.getParameter("mails"));

            if (!validateContactData(numbers, mails)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid contact data");
                return;
            }

            int contactId = addContact(tokenObject.getUserId(), firstName, lastName, numbers, mails);
            
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("contactId", contactId);
            sendResponse(response, HttpServletResponse.SC_OK, responseJson);

        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            AccessTokensObj tokenObject = validateAccessToken(request, response);
            if (tokenObject == null) return;

            int contactId = validateContactId(request);
            if (contactId == -1) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid contact ID");
                return;
            }

            // Update contact logic here
            // TODO: Implement contact update logic
            
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            AccessTokensObj tokenObject = validateAccessToken(request, response);
            if (tokenObject == null) return;

            int contactId = validateContactId(request);
            if (contactId == -1) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid contact ID");
                return;
            }

            // Delete contact logic here
            // TODO: Implement contact deletion logic
            
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private AccessTokensObj validateAccessToken(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, QueryException {
        String accessToken = request.getHeader(BEARER_HEADER);
        if (accessToken == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Access Token Header required");
            return null;
        }

        AccessTokensObj tokenObject = accessDao.getAccessTokenObject(accessToken);
        if (tokenObject == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid access token");
            return null;
        }

        return tokenObject;
    }

    private boolean hasRequiredScope(AccessTokensObj token, String... requiredScopes) {
        for (String scope : requiredScopes) {
            if (token.getScopes().contains(SCOPES.get(scope))) {
                return true;
            }
        }
        return false;
    }

    private JsonArray parseJsonArray(String json) {
        try {
            return gson.toJsonTree(json).getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }
    }

    private boolean validateInput(HttpServletRequest request) {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty();
    }

    private boolean validateContactData(JsonArray numbers, JsonArray mails) {
        return numbers != null && mails != null &&
               numbers.size() <= MAX_CONTACTS && mails.size() <= MAX_CONTACTS &&
               validatePhoneNumbers(numbers) && validateEmails(mails);
    }

    private boolean validatePhoneNumbers(JsonArray numbers) {
        for (JsonElement number : numbers) {
            String phoneNumber = number.getAsJsonObject().get("number").getAsString();
            if (phoneNumber == null || phoneNumber.length() > MAX_PHONE_LENGTH ||
                !phoneNumber.matches("^\\+?[0-9]{10,}$")) {
                return false;
            }
        }
        return true;
    }

    private boolean validateEmails(JsonArray mails) {
        for (JsonElement mail : mails) {
            String email = mail.getAsJsonObject().get("mail").getAsString();
            if (email == null || email.length() > MAX_EMAIL_LENGTH ||
                !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return false;
            }
        }
        return true;
    }

    private int validateContactId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("contactId"));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) return "";
        return input.replaceAll("[^a-zA-Z0-9.@+_-]", "");
    }

    private int addContact(int userId, String firstName, String lastName, 
                         JsonArray numbers, JsonArray mails) throws DaoException {
        int contactId = contactsDao.addContact(firstName, lastName, userId);
        
        for (JsonElement number : numbers) {
            String phoneNumber = number.getAsJsonObject().get("number").getAsString();
            mobileNumberDao.addNumberToContactId(contactId, phoneNumber);
        }

        for (JsonElement mail : mails) {
            String email = mail.getAsJsonObject().get("mail").getAsString();
            mailsDao.addMailToContact(contactId, email);
        }

        return contactId;
    }

    private void sendResponse(HttpServletResponse response, int status, Object content) 
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().print(gson.toJson(content));
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