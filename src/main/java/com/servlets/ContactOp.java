// Source code is decompiled from a .class file using FernFlower decompiler.
package com.servlets;

import com.dao.Dao;
import com.models.SessionData;
import com.models.User;
import com.session.SessionDataManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet({"/ContactOp"})
public class ContactOp extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public ContactOp() {
   }

   

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String action = request.getParameter("action");
      User user = (User) request.getAttribute("user_data");
      System.out.println("contactOp "+user);
      if (action.equals("deletecontact")) {
         int contact_id = Integer.parseInt(request.getParameter("contact_id"));

         try {
            Dao.deleteContact(contact_id);
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }

      if (action.equals("addcontact")) {
         System.out.println("came here");
         String[] mobiles = request.getParameter("mobile_numbers").split(",");
         String[] emails = request.getParameter("email_ids").split(",");
         Dao.addContact(user.getUser_id(), request.getParameter("first_name"), request.getParameter("last_name"), request.getParameter("address"), new ArrayList<String>(Arrays.asList(mobiles)), new ArrayList<String>(Arrays.asList(emails)));
         System.out.println("completed adding");
      }

      if (action.equals("addemail")) {
         String email = request.getParameter("mail");
         Dao.addEmail(user, email);
      }
      
      if(action.equals("updateContact"))
      try {
         user.setUser_contacts(Dao.getContacts(user));
         user.setGroup_contacts(Dao.getGroupUserContacts(user));
         user.setmails(Dao.getEmails(user));
//         SessionDataManager.users_data.put(user.getUser_id(), user);
      } catch (SQLException var8) {
    	  System.out.println("inside catch");
         var8.printStackTrace();
      }
      response.sendRedirect("index.jsp");
   }
}
