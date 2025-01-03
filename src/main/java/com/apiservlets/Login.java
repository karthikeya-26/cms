package com.apiservlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apihandlers.LoginHandler;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/api/v1/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "error");
        jsonResponse.addProperty("message", "PUT method not allowed on this resource");

        response.getWriter().write(jsonResponse.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("application/json");

        // Create a JsonObject for the response
        JsonObject jsonResponse = new JsonObject();

        // Validate user credentials (hardcoded validation for example)
        if ("admin".equals(username) && "password123".equals(password)) {
            // Generate a session token (hardcoded for example)
            String sessionId = "abc123";

            // Set a cookie
            Cookie sessionCookie = new Cookie("session_id", sessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(60 * 60 * 24); // 1-day expiration
            response.addCookie(sessionCookie);

            // Prepare success response
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Login successful");
            jsonResponse.addProperty("sessionId", sessionId);
        } else {
            // Prepare failure response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Invalid username or password");
        }

        // Write JSON response
        response.getWriter().write(jsonResponse.toString());
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Return 405 Method Not Allowed
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "error");
        jsonResponse.addProperty("message", "PUT method not allowed on this resource");

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Return 405 Method Not Allowed
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", "error");
        jsonResponse.addProperty("message", "DELETE method not allowed on this resource");

        response.getWriter().write(jsonResponse.toString());
    }

}
