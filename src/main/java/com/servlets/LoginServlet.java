// Source code is decompiled from a .class file using FernFlower decompiler.
package com.servlets;

import com.dao.Dao;
import com.models.Contact;
import com.models.User;
import com.session.Session;
import com.session.SessionData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({"/login"})
public class LoginServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public LoginServlet() {
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getRequestDispatcher("login.jsp").forward(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      System.out.println(request.getSession(false));
      User u = null;

      try {
         u = Dao.loginUser(request.getParameter("username"), request.getParameter("password"));
      } catch (NamingException ne) {
         ne.printStackTrace();
      } catch (SQLException e) {
         e.printStackTrace();
      }

      ArrayList<Contact> user_contacts = null;
      ArrayList<Contact> group_contacts = null;
      ArrayList<String> othermails = null;
      ArrayList<String> user_groups = null;

      try {
         user_contacts = Dao.getContacts(u);
         group_contacts = Dao.getGroupUserContacts(u);
         othermails = Dao.getUserEmails(u, request.getParameter("username"));
         user_groups = Dao.getUserGroups(u);
      } catch (SQLException var11) {
         var11.printStackTrace();
      }

      
      u.setUser_contacts(user_contacts);
      u.setGroup_contacts(group_contacts);
      u.setOthermails(othermails);
      u.setUserGroups(user_groups);
      if (u.getUser_name() != null) {
         String s_id = Session.getSession();
         Cookie cookie = new Cookie("sid", s_id);
         response.addCookie(cookie);
         SessionData.addUsertoSession(s_id, u);
         response.sendRedirect("index.jsp");
      } else {
         response.sendRedirect("login.jsp?error=invalid_details");
      }

   }
}
