package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.util.EmailSender;
import com.dao.DaoException;
import com.dao.UserMailsDao;
import com.dbObjects.UserMailsObj;
import com.dbconn.BCrypt;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserMailsDao mailsDao;
    // Token store with expiration
    static final ConcurrentHashMap<String, VerificationEntry> verificationTokens = new ConcurrentHashMap<>();
    
    // Scheduled cleanup for expired tokens
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SignUpServlet() {
        super();
        mailsDao = new UserMailsDao();
        startTokenCleanupTask();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("hi alkfsdfkljsd;lgas");
        response.setContentType("application/json");
        JsonObject responseJson = new JsonObject();

        try (PrintWriter out = response.getWriter()) {
            String requestBody = readRequestBody(request);
            System.out.println("read request body");
            JsonObject inputJson = JsonParser.parseString(requestBody).getAsJsonObject();
            validateRequiredFields(inputJson);
            System.out.println("validated json input");
            String email = inputJson.get("email").getAsString();

            // Check if email is already registered (Mocked here, replace with DB check)
            if (isEmailRegistered(email)) {
                handleError(response, responseJson, HttpServletResponse.SC_CONFLICT, "Email is already in use.");
                out.print(responseJson);
                return;
            }
            
            System.out.println("chechked whether email exists");

            // Secure password hashing before storing
            String password = inputJson.get("password").getAsString();
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println("hashed password");
            // Store hashed password
            inputJson.addProperty("password", hashedPassword);

            // Generate and store verification token
            String token = UUID.randomUUID().toString();
            verificationTokens.put(token, new VerificationEntry(inputJson));
            System.out.println("put token in vetokens");
            // Generate verification link dynamically
            String verificationLink = generateVerificationLink(request, token);
            System.out.println(verificationLink);
            // Send verification email
            EmailSender.sendVerificationEmail(email, verificationLink);
            System.out.println("sent mail");
            responseJson.addProperty("status", "success");
            responseJson.addProperty("message", "Verification email sent. Please check your inbox.");
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println(responseJson);
            out.print(responseJson);
        } catch (Exception e) {
        	e.printStackTrace();
            handleError(response, responseJson, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
        } 
//        catch (ValidationException e) {
//        	e.printStackTrace();
//            handleError(response, responseJson, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
//        } catch (Exception e) {
//        	e.printStackTrace();
//            System.err.println("Unexpected error: " + e.getMessage());
//            handleError(response, responseJson, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
//        }
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
        String[] requiredFields = {"userName", "firstName", "lastName", "profileType", "password", "email"};
        for (String field : requiredFields) {
            if (!inputJson.has(field) || inputJson.get(field).getAsString().trim().isEmpty()) {
                throw new ValidationException("Missing or empty required field: " + field);
            }
        }
    }

    private void handleError(HttpServletResponse response, JsonObject responseJson, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        responseJson.addProperty("status", "error");
        responseJson.addProperty("message", message);
        System.out.println(responseJson);
        response.getWriter().print(responseJson); 
    }

    private boolean isEmailRegistered(String email) throws DaoException {
        UserMailsObj mail = mailsDao.getMail(email);
        return (mail == null)? false : true;
    }

    private String generateVerificationLink(HttpServletRequest request, String token) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        return baseUrl + "/verify-email?token=" + token;
    }

    private void startTokenCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            verificationTokens.entrySet().removeIf(entry -> entry.getValue().isExpired(currentTime));
        }, 1, 1, TimeUnit.HOURS); // Cleanup runs every 1 hour
    }

    public static class VerificationEntry {
        private final JsonObject userData;
        private final long timestamp;

        public VerificationEntry(JsonObject userData) {
            this.userData = userData;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired(long currentTime) {
            return (currentTime - timestamp) > TimeUnit.HOURS.toMillis(24); 
        }

        public JsonObject getUserData() {
            return userData;
        }

		public JsonElement get(String string) {
			// TODO Auto-generated method stub
			return null;
		}
    }

    private static class ValidationException extends Exception {
        private static final long serialVersionUID = 1L;

        public ValidationException(String message) {
            super(message);
        }
    }
}
