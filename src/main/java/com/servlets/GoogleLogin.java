package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.handlers.GoogleContactsSyncHandler;

/**
 * Servlet implementation class GoogleLogin
 */
@WebServlet("/glogin")
public class GoogleLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String clientId = GoogleContactsSyncHandler.getClientid();
		String redirecturi = GoogleContactsSyncHandler.getAccountsEndpoint()+"?client_id="+clientId+"&redirect_uri=http://localhost:8280/contacts/glogincallback"
				+"&scope=openid "+GoogleContactsSyncHandler.getProfileScope()+"&response_type=code";

		response.sendRedirect(redirecturi);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return ;
		
	}

}
