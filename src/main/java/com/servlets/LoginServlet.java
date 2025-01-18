package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.handlers.LoginHandler;
import com.loggers.AppLogger;
import com.session.Session;
import com.session.SessionDataManager;
import com.session.SessionFetcher;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static AppLogger logger = new AppLogger(LoginServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Parse JSON input
        StringBuilder jsonInput = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }
        }

        // Extract email and password from JSON
        JsonObject input = JsonParser.parseString(jsonInput.toString()).getAsJsonObject();
        String email = input.get("email").getAsString();
        String password = input.get("password").getAsString();
        
        
        UserDetailsObj user = null;
        
        try {
			user = LoginHandler.validateUser(email, password);
		} catch (DaoException e) {
			response.sendError(400, "Invalid Login details");
			return;
		}
        JsonObject responseJson = new JsonObject();
        if(user== null) {
        	
        	responseJson.addProperty("status", "failed");
        	responseJson.addProperty("message", "User not found");
        	out.print(responseJson);
        	out.flush();
        	return ;
        }
        
        String sessionId = Session.getSessionId();
        SessionsDao dao = new SessionsDao();
        Long time = Instant.now().toEpochMilli();
        SessionsObj session = new SessionsObj(sessionId, user.getUserId(), time, time);
        
        try {
			dao.insertSession(sessionId, user.getUserId(), time, time);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage(),e);
		}
        SessionDataManager.session_data.put(sessionId, session);
        SessionDataManager.users_data.put(user.getUserId(), user);
        
        Cookie c = new Cookie("session_id", sessionId);
        response.addCookie(c);
        responseJson.addProperty("status", "success");
        responseJson.addProperty("message", "Login successfull");
        out.print(responseJson);
        out.flush();
         

	}

}
