package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.SessionsDao;
import com.dao.UserDetailsDao;
import com.dao.UserMailsDao;
import com.dbObjects.SessionsObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.handlers.GoogleContactsSyncHandler;
import com.loggers.AppLogger;
import com.session.SessionDataManager;
import com.util.IdGenerator;

/**
 * Servlet implementation class GoogleLoginCallback
 */
@WebServlet("/glogincallback")
public class GoogleLoginCallback extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static AppLogger logger = new AppLogger(GoogleLoginCallback.class.getCanonicalName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoogleLoginCallback() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Hi from do get of glogincallback");
		String authCode = request.getParameter("code");
		JsonObject responseJson = new JsonObject();
		if (authCode == null) {
			responseJson.addProperty("error", "please provide an authorization code");
			response.setStatus(400);
			response.getWriter().print(responseJson);
			return;
		}
		String accessToken;

		HttpURLConnection tokenConnection;

		try {
			URL url = new URL("https://oauth2.googleapis.com/token");
			String params = "code=" + URLEncoder.encode(request.getParameter("code"), "UTF-8") + "&client_id="
					+ URLEncoder.encode(GoogleContactsSyncHandler.getClientid(), "UTF-8") + "&client_secret="
					+ URLEncoder.encode(GoogleContactsSyncHandler.getClientsecret(), "UTF-8")
					+ "&redirect_uri=http://localhost:8280/contacts/glogincallback" + "&grant_type=authorization_code";

			tokenConnection = (HttpURLConnection) url.openConnection();
			tokenConnection.setRequestMethod("POST");
			tokenConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			tokenConnection.setDoOutput(true);

			try (OutputStream os = tokenConnection.getOutputStream()) {
				os.write(params.getBytes(StandardCharsets.UTF_8));
				os.flush();
			}

			int responseCode = tokenConnection.getResponseCode();
			System.out.println(responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(tokenConnection.getInputStream(), StandardCharsets.UTF_8))) {
					StringBuilder res = new StringBuilder();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						res.append(inputLine);
					}
					responseJson = JsonParser.parseString(res.toString()).getAsJsonObject();

					accessToken = responseJson.get("access_token").getAsString();
					System.out.println("accessToken :"+accessToken);
				}
			} else {
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(tokenConnection.getInputStream(), StandardCharsets.UTF_8))) {
					StringBuffer errorResponse = new StringBuffer();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						errorResponse.append(inputLine);
					}
					
					System.out.println(errorResponse.toString());
					logger.log(Level.WARNING, errorResponse.toString());
					return;
				}
			}

		} catch (Exception e) {
			responseJson.addProperty("error", e.getMessage());
			response.getWriter().print(responseJson);
			return;
		}

		HttpURLConnection conn;

		try {
			
			System.out.println("fetching user data");
			URL url = new URL("https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			int resCode = conn.getResponseCode();
			JsonObject data = new JsonObject();
			System.out.println("Response code "+resCode);
			if (resCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
					StringBuilder res = new StringBuilder();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						res.append(inputLine);
					}
					data = JsonParser.parseString(res.toString()).getAsJsonObject();
					System.out.println(new Gson().toJsonTree(data));
					UserDetailsDao userDao = new UserDetailsDao();
					String accountId = data.get("resourceName").getAsString();
					JsonObject names = data.get("names").getAsJsonArray().get(0).getAsJsonObject();
					JsonObject emailAddresses = data.get("emailAddresses").getAsJsonArray().get(0).getAsJsonObject();
					UserDetailsObj user = userDao.getUserWithAccountId(accountId);
					IdGenerator generator = new IdGenerator();
					System.out.println("user :"+user);
					if (user != null) {
						
						System.out.println("User exists");
						Integer userId = user.getUserId();
						String sessionId = generator.generateSessionId();
						Long time = Instant.now().toEpochMilli();
						SessionsDao sessionDao = new SessionsDao();
						sessionDao.insertSession(sessionId, userId, time, time);

						Cookie c = new Cookie("session_id", sessionId);
						c.setMaxAge(3600);

						SessionsObj session = new SessionsObj(sessionId, user.getUserId(), time, time);
						SessionDataManager.session_data.put(sessionId, session);
						SessionDataManager.users_data.put(userId, user);
						response.addCookie(c);

						response.sendRedirect("profile.jsp");

					} else {
						
						System.out.println("user doesnt exist");
						int userId = userDao.createUser(names.get("displayName").getAsString(),
								names.get("givenName").getAsString(), names.get("familyName").getAsString(), "private",
								accountId);
						UserMailsDao dao = new UserMailsDao();
						dao.addMailForUser(userId, emailAddresses.get("value").getAsString());

						IdGenerator gen = new IdGenerator();
						String sessionId = gen.generateSessionId();
						Long time = Instant.now().toEpochMilli();
						SessionsObj session = new SessionsObj(sessionId, userId, time, time);
						SessionDataManager.session_data.put(sessionId, session);
						SessionDataManager.users_data.put(userId, userDao.getUserWithId(userId));
						SessionsDao sessionDao = new SessionsDao();
						sessionDao.insertSession(sessionId, userId, time, time);

						Cookie c = new Cookie("session_id", sessionId);
						c.setMaxAge(3600);
						response.addCookie(c);

						response.sendRedirect("profile.jsp");
					}

				}
			} else {

			}

		} catch (Exception e) {
			responseJson.addProperty("error", "Something went wrong , try again later");
			response.setStatus(500);
			response.getWriter().print(responseJson);
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
