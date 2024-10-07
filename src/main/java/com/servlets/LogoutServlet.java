// Source code is decompiled from a .class file using FernFlower decompiler.
package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.User;
import com.session.SessionData;

@WebServlet({"/logout"})
public class LogoutServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public LogoutServlet() {
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getSession().invalidate();
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.sendRedirect("login.jsp");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   Cookie[] cookies = request.getCookies();

	   for(int i=0; i<cookies.length; i++){
	   	if (cookies[i].getName().equals("sid")){
	   		SessionData.removeUserFromSession(cookies[i].getValue());
	   	}
	   }

      response.sendRedirect("login.jsp");
   }
}
