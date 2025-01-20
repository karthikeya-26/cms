package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.AccessTokensDao;
import com.dao.AuthorizationCodesDao;
import com.dao.ClientDetailsDao;
import com.dao.RefreshTokensDao;
import com.dbObjects.AuthorizationCodesObj;
import com.dbObjects.ClientDetailsObj;
import com.dbObjects.RefreshTokensObj;
import com.google.gson.JsonObject;
import com.queryLayer.QueryException;
import com.util.IdGenerator;

/**
 * Servlet implementation class TokenIssuer
 */
@WebServlet("/api/v1/oauth2/token")
public class TokenIssuer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ClientDetailsDao clientDao = new ClientDetailsDao();
	private final AuthorizationCodesDao authDao = new AuthorizationCodesDao();
	private final RefreshTokensDao refreshDao = new RefreshTokensDao();
	private final AccessTokensDao accessDao = new AccessTokensDao();
	private final IdGenerator tokenGenerator = new IdGenerator();

	/**
	 * Handles POST requests to issue tokens.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		JsonObject responseJson = new JsonObject();

		// Extract parameters
		String includeGrantedScopes = request.getParameter("include_granted_scopes");
		String clientId = request.getParameter("client_id");
		String code = request.getParameter("code");
		String clientSecret = request.getParameter("client_secret");
		String grantType = request.getParameter("grant_type");
		String scopes = request.getParameter("scopes");

		System.out.println("client id : " + clientId + " client secret :" + clientSecret + " code :" + code);

		if (clientId == null || code == null || clientSecret == null) {
			sendError(response, 400, "Invalid details");
			return;
		}
		
		
		
		try {
			// Validate authorization code
			AuthorizationCodesObj codeObj = authDao.getAuthorizationCodesObj(code);
			if (codeObj == null) {
				sendError(response, 400, "Invalid code");
				return;
			}

			if (codeObj.getCreated_at() + 60 * 60 * 1000 < Instant.now().toEpochMilli()) {
				authDao.deleteAuthCode(code);
				sendError(response, 400, "Authorization code expired");
				return;
			}

			// Validate client details
			ClientDetailsObj clientObject = clientDao.getClient(clientId);
			if (clientObject == null || !clientSecret.equals(clientObject.getClientSecret())) {
				sendError(response, 403, "Invalid client credentials");
				return;
			}

			// Validate client ID with authorization code
			if (!clientId.equals(codeObj.getClient_id())) {
				sendError(response, 403, "Client and code mismatch");
				return;
			}

			// Generate tokens
			if (grantType == null) {
				response.sendError(400, "Please provide a valid grant type");
				return;
			}

			if (grantType.equals("code")) {
				Integer refTokenId = null;
				if ("offline".equals(codeObj.getAccess_type())) {
					String refreshToken = tokenGenerator.generateRefreshToken();
					refTokenId = refreshDao.addRefreshToken(refreshToken, codeObj.getClient_id(), codeObj.getUser_id(),
							codeObj.getScopes());
					responseJson.addProperty("refresh_token", refreshToken);
				}

				String accessToken = tokenGenerator.generateAccessToken();
				accessDao.addAccessToken(accessToken, codeObj.getScopes(), codeObj.getUser_id(), clientId, refTokenId);
				responseJson.addProperty("access_token", accessToken);
				responseJson.addProperty("token_type", "Bearer");

				// Include optional data
				responseJson.addProperty("expires_in", 3600);
				responseJson.addProperty("scope", codeObj.getScopes());

				authDao.deleteAuthCode(code);

				// Send response
				try (PrintWriter out = response.getWriter()) {
					out.print(responseJson);
				}
			} else if (grantType.equals("refresh_token")) {
				if (request.getParameter("refresh_token") == null) {
					response.sendError(400, "please provide a refresh token");
				}

				RefreshTokensObj refreshToken = refreshDao.getRefTokenObj(request.getParameter("refresh_token"));

				if (refreshToken == null) {
					response.sendError(400, "Invalid refresh token");
				}

			}

		} catch (QueryException e) {
			sendError(response, 500, "Something went wrong, please try again later");
		}
	}

	/**
	 * Utility method to send an error response.
	 */
	private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
		response.setStatus(statusCode);
		JsonObject errorJson = new JsonObject();
		errorJson.addProperty("error", message);
		try (PrintWriter out = response.getWriter()) {
			out.print(errorJson);
		}
	}
}
