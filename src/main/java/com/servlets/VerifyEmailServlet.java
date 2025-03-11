package com.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.servlets.SignUpServlet.VerificationEntry;
import com.dao.*;
import com.dbconn.BCrypt;

@WebServlet("/verify-email")
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserDetailsDao userDetailsDao = new UserDetailsDao();
    private final PasswordsDao passwordsDao = new PasswordsDao();
    private final UserMailsDao mailsDao = new UserMailsDao();

    public VerifyEmailServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String message;
        String status;

        try {
            String token = request.getParameter("token");

            if (token == null || !SignUpServlet.verificationTokens.containsKey(token)) {
                throw new Exception("Invalid or expired verification link.");
            }

            // Retrieve user details and remove from temporary store
            VerificationEntry inputJson = SignUpServlet.verificationTokens.remove(token);

            // Register user in the database
            int userId = userDetailsDao.createUser(
                    inputJson.get("userName").getAsString(),
                    inputJson.get("firstName").getAsString(),
                    inputJson.get("lastName").getAsString(),
                    inputJson.get("profileType").getAsString());

            passwordsDao.createPassword(
                    userId,
                    BCrypt.hashpw(inputJson.get("password").getAsString(), BCrypt.gensalt()),
                    0);

            mailsDao.addMailForUser(
                    userId,
                    inputJson.get("email").getAsString());

            status = "success";
            message = "Email verified! User registered successfully.";

        } catch (Exception e) {
            status = "error";
            message = e.getMessage();
        }

        // Set attributes and forward request to verify.jsp
        request.setAttribute("status", status);
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("verify.jsp");
        dispatcher.forward(request, response);
    }
}
