package com.servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.AccessTokensDao;
import com.dao.ClientDetailsDao;
import com.dao.DaoException;
import com.dao.UserDetailsDao;
import com.dao.UserMailsDao;
import com.dbObjects.AccessTokensObj;
import com.dbObjects.UserDetailsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;

/**
 * Servlet implementation class ResourceProfile
 */
@WebServlet("/api/v1/resource/profile")
public class ResourceProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static AppLogger logger = new AppLogger(ResourceProfile.class.getName());
	private static final HashMap<String, Integer> SCOPES = new HashMap<>();

    static {
        SCOPES.put("profile", 1);
        SCOPES.put("profile.read", 2);
        SCOPES.put("contacts.read", 3);
        SCOPES.put("contacts", 4);
    }
	AccessTokensDao accessDao = new AccessTokensDao();
	UserDetailsDao userDao = new UserDetailsDao();
	UserMailsDao userMailDao = new UserMailsDao();
	ClientDetailsDao clientDao = new ClientDetailsDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourceProfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();
		String accessToken = request.getHeader("Bearer");
		System.out.println(accessToken);
		if(accessToken == null) {
			response.sendError(400,"Provide an access token in the request header.");
		}
		AccessTokensObj accessTokenObject= null;
		try {
			  accessTokenObject = accessDao.getAccessTokenObject(accessToken);
			  System.out.println(accessTokenObject);
			if(accessTokenObject == null) {
				response.sendError(400, "Invalid authorization code");
				return;
			}
			
			if(accessTokenObject.getCreated_at() +60*60*1000 < Instant.now().toEpochMilli() ) {
				response.sendError(400, "Expired access token");
				accessDao.deleteAccessToken(accessToken);
			}
			
			boolean isScoped = (accessTokenObject.getScopes().contains("1")||accessTokenObject.getScopes().contains("2"));
			
			if(!isScoped) {
				response.sendError(403, "Not allowed to this operation.");
				return;
			}
			
		} catch (QueryException e) {
			response.sendError(500, "Some error occured, try again later.");
			return;
		}
		
		
		Map<String,String[]> reqParams = request.getParameterMap();
		
		if(reqParams.isEmpty()) {
			Integer userId = accessTokenObject.getUser_id();
			try {
				UserDetailsObj user = userDao.getUserWithId(userId);
				response.getWriter().print(new Gson().toJsonTree(user));
				return;
			} catch (DaoException e) {
				response.sendError(500,"Something went wrong try again later");
				return;
			}
		}else {
			return;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accessToken = request.getParameter("Bearer");
		
		if(accessToken == null) {
			response.sendError(404, "Provide an access token.");
			return;
		}
		
		try {
			AccessTokensObj tokenObj = accessDao.getAccessTokenObject(accessToken);
		} catch (QueryException e) {
        	logger.log(Level.SEVERE, e.getMessage(),e);
			response.sendError(500,"Something went wrong, trry again later.");
			return;
		}
		
		
	}

}
