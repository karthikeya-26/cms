package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.handlers.GoogleContactsSyncHandler;

 
@WebServlet("/syncContacts")
public class SyncContacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public SyncContacts() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String provider = request.getParameter("provider");
		if(provider == null) {
			response.sendRedirect("usercontacts.jsp?error=invalid_provider");
		}
		
		if(provider.toLowerCase().equals("google")) {
			GoogleContactsSyncHandler info = new GoogleContactsSyncHandler();
			System.out.println(info.getAuthUrl());
			response.sendRedirect(info.getAuthUrl());
		}
		//redirect to google and then to goauth
	}

}
