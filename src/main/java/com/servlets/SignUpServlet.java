package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.dao.*;
import com.dbconn.BCrypt;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDetailsDao userDetailsDao;
    private final PasswordsDao passwordsDao;
    private final UserMailsDao mailsDao;

    public SignUpServlet() {
        super();
        userDetailsDao = new UserDetailsDao();
        passwordsDao = new PasswordsDao();
        mailsDao = new UserMailsDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject responseJson = new JsonObject();

        try {
            String requestBody = readRequestBody(request);
            JsonObject inputJson = JsonParser.parseString(requestBody).getAsJsonObject();
            System.out.println(inputJson);
            validateRequiredFields(inputJson);
            System.out.println("fields validated");
            registerUser(inputJson);
            System.out.println("user registered");
            responseJson.addProperty("status", "success");
            responseJson.addProperty("message", "User registered successfully");
            response.setStatus(HttpServletResponse.SC_CREATED);

        } catch (JsonParseException e) {
        	e.printStackTrace();
        	System.out.println("jsonParse");
            handleError(response, responseJson, HttpServletResponse.SC_BAD_REQUEST, 
                "Invalid JSON format: " + e.getMessage());
        } catch (ValidationException e) {
        	e.printStackTrace();
        	System.out.println("validation");
            handleError(response, responseJson, HttpServletResponse.SC_BAD_REQUEST, 
                e.getMessage());
        } catch (DaoException e) {
        	e.printStackTrace();
        	System.out.println("database");
            handleError(response, responseJson, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Database error occurred: " + e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("another");
            handleError(response, responseJson, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred");
        } finally {
            out.print(responseJson);
            out.flush();
        }
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder input = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                input.append(line);
            }
        }
        return input.toString();
    }

    private void validateRequiredFields(JsonObject inputJson) throws ValidationException {
        String[] requiredFields = {"userName", "firstName", "lastName", "profileType", 
            "password", "email"};
        
        for (String field : requiredFields) {
            if (!inputJson.has(field) || inputJson.get(field).getAsString().trim().isEmpty()) {
                throw new ValidationException("Missing or empty required field: " + field);
            }
        }
    }

    private int registerUser(JsonObject inputJson) throws DaoException {
        int userId = userDetailsDao.createUser(
            inputJson.get("userName").getAsString(),
            inputJson.get("firstName").getAsString(),
            inputJson.get("lastName").getAsString(),
            inputJson.get("profileType").getAsString()
        );
        
        passwordsDao.createPassword(
            userId,
            BCrypt.hashpw(inputJson.get("password").getAsString(), BCrypt.gensalt()),
            0
        );

        mailsDao.addMailForUser(
            userId,
            inputJson.get("email").getAsString()
        );

        return userId;
    }

    private void handleError(HttpServletResponse response, JsonObject responseJson, 
            int statusCode, String message) {
        response.setStatus(statusCode);
        responseJson.addProperty("status", "error");
        responseJson.addProperty("message", message);
    }

    private static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ValidationException(String message) {
            super(message);
        }
    }
}