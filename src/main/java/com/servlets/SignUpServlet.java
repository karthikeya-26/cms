// Source code is decompiled from a .class file using FernFlower decompiler.
package com.servlets;

import com.dao.Dao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet({"/signup"})
public class SignUpServlet extends HttpServlet {
   public SignUpServlet() {
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      if (Dao.signUpUser(request.getParameter("user_name"), request.getParameter("first_name"), request.getParameter("last_name"), request.getParameter("email"), request.getParameter("password"), request.getParameter("account_type"))) {
         response.sendRedirect("login.jsp");
      } else {
         response.sendRedirect("signup.jsp?some_error_occured_try_again");
      }

   }
}
