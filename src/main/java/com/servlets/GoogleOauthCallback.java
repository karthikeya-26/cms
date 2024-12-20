package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ContactsSyncDao;
import com.filters.SessionFilter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.handlers.GoogleContactsSyncHandler;
import com.loggers.AppLogger;

/**
 * Servlet implementation class OauthCallback
 */
@WebServlet("/goauth")
public class GoogleOauthCallback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleOauthCallback() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GoogleContactsSyncHandler h = new GoogleContactsSyncHandler();
		String accessTokenEndpoint = "https://oauth2.googleapis.com/token";
		String params = "code=" + URLEncoder.encode(request.getParameter("code"), "UTF-8")
        + "&client_id=" + URLEncoder.encode(GoogleContactsSyncHandler.getClientid(), "UTF-8")
        + "&client_secret=" + URLEncoder.encode(GoogleContactsSyncHandler.getClientsecret(), "UTF-8")
        + "&redirect_uri=" + URLEncoder.encode(GoogleContactsSyncHandler.getRedirecturi(), "UTF-8")
        + "&grant_type=authorization_code";

		JsonObject jsonResponse = new JsonObject();
		
		try {
			URL url = new URL(accessTokenEndpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			
			try (OutputStream os = conn.getOutputStream()){
				os.write(params.getBytes(StandardCharsets.UTF_8));
				os.flush();
			}
			
			int responseCode = conn.getResponseCode();
			if( responseCode == HttpURLConnection.HTTP_OK) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8))){
					StringBuilder res = new StringBuilder();
					String inputLine;
					while((inputLine = in.readLine()) != null) {
						res.append(inputLine);
					}
					jsonResponse =  JsonParser.parseString(res.toString()).getAsJsonObject();
				}
			}else {
				try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8))){
					StringBuffer errorResponse = new StringBuffer();
					String inputLine ;
					while((inputLine = in.readLine())!= null) {
						errorResponse.append(inputLine);
					}
					AppLogger.ApplicationLog(errorResponse.toString());
				}
			}
			
			
			if(conn.getResponseCode()== HttpURLConnection.HTTP_OK) {
				System.out.println(jsonResponse.toString());
				String accessToken = jsonResponse.get("access_token").getAsString();
				String refreshToken = jsonResponse.get("refresh_token").getAsString();
				System.out.println("access token from google:"+accessToken);
				System.out.println("refresh token from google:"+refreshToken);
				
				String accountId = getAccountId(accessToken);
				ContactsSyncDao dao = new ContactsSyncDao();
//				dao.addRefreshTokenToUser(SessionFilter.user_id.get(), refreshToken, "GOOGLE");
//				h.getAndCreateContacts(refreshToken, accessToken, null);
				response.sendRedirect("usercontacts.jsp");
			}else {
				response.sendRedirect("usercontacts.jsp?error=something_went_wrong_please_try_again_later");
			}
		} catch (Exception e) {
			e.printStackTrace();
			AppLogger.ApplicationLog(e);
		}	

		
	}

	private String getAccountId(String accessToken) {
		GoogleContactsSyncHandler h = new GoogleContactsSyncHandler();
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
