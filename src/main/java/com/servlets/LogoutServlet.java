// Source code is decompiled from a .class file using FernFlower decompiler.
package com.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.Dao;
import com.session.SessionDataManager;

@WebServlet({"/logout"})
public class LogoutServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public LogoutServlet() {
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doPost(request,response);
   }

   @SuppressWarnings("unlikely-arg-type")
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   
	   Cookie[] cookies = request.getCookies();

	   for(int i=0; i<cookies.length; i++){
	   	if (cookies[i].getName().equals("sid")){
	   		SessionDataManager.users_data.remove(SessionDataManager.session_data.get(cookies[i].getValue()));
	   		System.out.println(SessionDataManager.session_data.get(cookies[i].getValue()));
	   		SessionDataManager.removeUserFromSession(cookies[i].getValue());
	   		
	   		Dao.inactivateSession(cookies[i].getValue());
	   	}
	   }

      response.sendRedirect("login.jsp");
   }
}
