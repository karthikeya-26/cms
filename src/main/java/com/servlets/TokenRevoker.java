package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.RefreshTokensDao;
import com.dbObjects.RefreshTokensObj;
import com.google.gson.JsonObject;
import com.queryLayer.QueryException;

/**
 * Servlet implementation class TokenRevoker
 */
@WebServlet("/api/v1/oauth2/token/revoke")
public class TokenRevoker extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TokenRevoker() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();
		String refreshToken = request.getParameter("refreshToken");
		
		if(refreshToken == null) {
			response.setStatus(400);
			responseJson.addProperty("error","Please provide a token value");
			response.getWriter().print(responseJson);
			return;
		}
		RefreshTokensDao refreshDao = new RefreshTokensDao();
		try {
			RefreshTokensObj tokenObj = refreshDao.getRefTokenObj(refreshToken);
			
			if(tokenObj == null) {
				response.setStatus(400);
				responseJson.addProperty("error", "Not a valid refresh token");
				response.getWriter().print(responseJson);
				return;
			}
			
			boolean success =refreshDao.deleteRefreshToken(refreshToken);
			
			if(success) {
				response.setStatus(204);
				return;
			}else {
				response.setStatus(500);
				responseJson.addProperty("error", "Something went wrong try again later");
				response.getWriter().print(responseJson);
				return;
			}
			
			
		} catch (QueryException e) {
			response.setStatus(500);
			responseJson.addProperty("error", "Something went wrong try again later");
			response.getWriter().print(responseJson);
			return;	
		}
	}

}
