package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegClient
 */
@WebServlet("/api/v1/regclient")
public class RegClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegClient() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String appName  = request.getParameter("appName");
		String[] redirectUris = request.getParameterValues("redirectUris[]");
		String appType = request.getParameter("appType");
		System.out.println("appType :"+appType);
		System.out.println("App name :"+appName);
		System.out.println(" RedirectUri :"+redirectUris);
		for (String string : redirectUris) {
			System.out.println(string);
		}
		response.sendRedirect("/contacts/regclient.jsp");
	}

}
