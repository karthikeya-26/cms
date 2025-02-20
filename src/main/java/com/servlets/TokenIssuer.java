package com.servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.*;
import com.dbObjects.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loggers.AppLogger;
import com.queryLayer.QueryException;
import com.util.IdGenerator;

@WebServlet("/api/v1/oauth2/token")
public class TokenIssuer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final AppLogger logger = new AppLogger(TokenIssuer.class.getName());
	private static final Gson gson = new Gson();

	private static final String GRANT_TYPE_CODE = "code";
	private static final String GRANT_TYPE_REFRESH = "refresh_token";
	private static final String ACCESS_TYPE_OFFLINE = "offline";
	private static final long AUTH_CODE_EXPIRY = 60 * 60 * 1000;
	private static final int ACCESS_TOKEN_EXPIRY = 3600;

	private final ClientDetailsDao clientDao;
	private final AuthorizationCodesDao authDao;
	private final RefreshTokensDao refreshDao;
	private final AccessTokensDao accessDao;
	private final IdGenerator tokenGenerator;

	public TokenIssuer() {
		this.clientDao = new ClientDetailsDao();
		this.authDao = new AuthorizationCodesDao();
		this.refreshDao = new RefreshTokensDao();
		this.accessDao = new AccessTokensDao();
		this.tokenGenerator = new IdGenerator();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String grantType = request.getParameter("grant_type");

			if (grantType == null) {
				sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Grant type is required");
				return;
			}

			switch (grantType) {
			case GRANT_TYPE_CODE:
				handleAuthorizationCodeGrant(request, response);
				break;
			case GRANT_TYPE_REFRESH:
				handleRefreshTokenGrant(request, response);
				break;
			default:
				sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Unsupported grant type");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error processing token request", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
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

	private void handleAuthorizationCodeGrant(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			TokenRequest tokenRequest = validateTokenRequest(request, response);
			if (tokenRequest == null) {
				return;
			}

			AuthorizationCodesObj codeObj = validateAuthorizationCode(tokenRequest.getCode(), response);
			if (codeObj == null) {
				return;
			}

			if (!validateClient(tokenRequest.getClientId(), tokenRequest.getClientSecret(), codeObj, response)) {
				return;
			}

			JsonObject tokenResponse = generateTokens(codeObj, tokenRequest.getClientId());

			sendResponse(response, HttpServletResponse.SC_OK, tokenResponse);

			authDao.deleteAuthCode(tokenRequest.getCode());

		} catch (QueryException e) {
			logger.log(Level.SEVERE, "Database error during authorization code flow", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
		}
	}

	private void handleRefreshTokenGrant(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String refreshToken = request.getParameter("refresh_token");
			if (refreshToken == null) {
				sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Refresh token is required");
				return;
			}

			RefreshTokensObj refreshTokenObj = refreshDao.getRefTokenObj(refreshToken);
			if (refreshTokenObj == null) {
				sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid refresh token");
				return;
			}

			// Generate new access token
			String accessToken = tokenGenerator.generateAccessToken();
			accessDao.addAccessToken(accessToken, refreshTokenObj.getScopes(), refreshTokenObj.getUserId(),
					refreshTokenObj.getClientId(), refreshTokenObj.getRefTokenId());

			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("access_token", accessToken);
			responseJson.addProperty("token_type", "Bearer");
			responseJson.addProperty("expires_in", ACCESS_TOKEN_EXPIRY);
			responseJson.addProperty("scope", refreshTokenObj.getScopes());

			sendResponse(response, HttpServletResponse.SC_OK, responseJson);

		} catch (QueryException e) {
			logger.log(Level.SEVERE, "Database error during refresh token flow", e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
		}
	}

	private TokenRequest validateTokenRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String clientId = request.getParameter("client_id");
		String code = request.getParameter("code");
		String clientSecret = request.getParameter("client_secret");

		if (clientId == null || code == null || clientSecret == null) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
			return null;
		}

		return new TokenRequest(clientId, code, clientSecret);
	}

	private AuthorizationCodesObj validateAuthorizationCode(String code, HttpServletResponse response)
			throws IOException, QueryException {
		AuthorizationCodesObj codeObj = authDao.getAuthorizationCodesObj(code);

		if (codeObj == null) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid authorization code");
			return null;
		}

		if (isCodeExpired(codeObj)) {
			authDao.deleteAuthCode(code);
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Authorization code expired");
			return null;
		}

		return codeObj;
	}

	private boolean validateClient(String clientId, String clientSecret, AuthorizationCodesObj codeObj,
			HttpServletResponse response) throws IOException, QueryException {
		ClientDetailsObj clientObj = clientDao.getClient(clientId);

		if (clientObj == null || !clientSecret.equals(clientObj.getClientSecret())) {
			sendError(response, HttpServletResponse.SC_FORBIDDEN, "Invalid client credentials");
			return false;
		}

		if (!clientId.equals(codeObj.getClientId())) {
			sendError(response, HttpServletResponse.SC_FORBIDDEN, "Client ID mismatch");
			return false;
		}

		return true;
	}

	private JsonObject generateTokens(AuthorizationCodesObj codeObj, String clientId) throws QueryException {
		JsonObject tokenResponse = new JsonObject();
		Integer refreshTokenId = null;

		if (ACCESS_TYPE_OFFLINE.equals(codeObj.getAccessType())) {
			String refreshToken = tokenGenerator.generateRefreshToken();
			refreshTokenId = refreshDao.addRefreshToken(refreshToken, codeObj.getClientId(), codeObj.getUserId(),
					codeObj.getScopes());
			tokenResponse.addProperty("refresh_token", refreshToken);
		}

		String accessToken = tokenGenerator.generateAccessToken();
		accessDao.addAccessToken(accessToken, codeObj.getScopes(), codeObj.getUserId(), clientId, refreshTokenId);

		tokenResponse.addProperty("access_token", accessToken);
		tokenResponse.addProperty("token_type", "Bearer");
		tokenResponse.addProperty("expires_in", ACCESS_TOKEN_EXPIRY);
		tokenResponse.addProperty("scope", codeObj.getScopes());

		return tokenResponse;
	}

	private boolean isCodeExpired(AuthorizationCodesObj codeObj) {
		return codeObj.getCreatedAt() + AUTH_CODE_EXPIRY < Instant.now().toEpochMilli();
	}

	private void sendResponse(HttpServletResponse response, int status, Object content) throws IOException {
		response.setContentType("application/json");
		response.setStatus(status);
		response.getWriter().print(gson.toJson(content));
	}

	private void sendError(HttpServletResponse response, int status, String message) throws IOException {
		JsonObject error = new JsonObject();
		error.addProperty("error", message);
		sendResponse(response, status, error);
	}

	private static class TokenRequest {
		private final String clientId;
		private final String code;
		private final String clientSecret;

		public TokenRequest(String clientId, String code, String clientSecret) {
			this.clientId = clientId;
			this.code = code;
			this.clientSecret = clientSecret;
		}

		public String getClientId() {
			return clientId;
		}

		public String getCode() {
			return code;
		}

		public String getClientSecret() {
			return clientSecret;
		}
	}
}