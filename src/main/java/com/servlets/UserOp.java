package com.servlets;

import com.dao.Dao;
import com.models.SessionData;
import com.models.User;
import com.session.SessionDataManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/userOp"})
public class UserOp extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserOp() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String sid = null;
       
       
       Cookie[] cookies = request.getCookies();
       for(int i=0; i<cookies.length; i++) {
    	   if (cookies[i].getName().equals("sid")) {
    		   sid = cookies[i].getValue();
    	   }
       }
       System.out.println(sid);
       
       SessionData sessiondata =SessionDataManager.getUserwithId(sid);

        User user = SessionDataManager.users_data.get(sessiondata.getUser_id());
        String action = request.getParameter("action");

        if ("viewContacts".equals(action)) {
            try {
                ArrayList<?> contacts = Dao.getContacts(user);
                request.setAttribute("contacts", contacts);  // Assuming you want to set the contacts for the JSP
                request.getRequestDispatcher("viewContacts.jsp").forward(request, response); // Redirect to viewContacts page
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Unable to fetch contacts at the moment.");
                request.getRequestDispatcher("index.jsp?try_again").forward(request, response);
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String action = request.getParameter("action");
     
        User user = (User)request.getAttribute("user_data");
        switch (action) {
            case "addemail":
                handleAddEmail(request, user);
                break;
            case "deleteemail":
                Dao.deleteEmail(user, request.getParameter("emailtodelete"));
                break;
            case "createGroup":
                handleCreateGroup(request, user);
                break;
            case "addContacttoGroup":
                handleAddContactToGroup(request, user);
                break;
            case "deleteContactfromGroup":
                handleDeleteContactFromGroup(request, user);
                break;
            case "setprimaryemail" :
            	handleSetPrimaryEmail(request, user);
            	break;
            default:
                break;
        }

        updateUserSessionData(user);
        response.sendRedirect("index.jsp");
    }

//	private boolean isUserLoggedIn(HttpSession session, HttpServletResponse response) throws IOException {
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect("login.jsp");
//            return false;
//        }
//        return true;
//    }

    private void handleAddEmail(HttpServletRequest request, User user) {
        String email = request.getParameter("mail");
        if (!Dao.checkifMailExists(user, email)) {
            Dao.addEmail(user, email);
        } else {
            System.out.println("Email already exists for the user.");
        }
    }

    private void handleCreateGroup(HttpServletRequest request, User user) {
        String groupName = request.getParameter("group_name");
        if (!Dao.checkifGroupExists(user, groupName)) {
            Dao.addGroup(user, groupName);
        } else {
            System.out.println("Group already exists.");
        }
    }

    private void handleAddContactToGroup(HttpServletRequest request, User user) {
        String selectedGroup = request.getParameter("selectedgroup");
        int selectedContactId = Integer.parseInt(request.getParameter("selectedcontact"));
        Dao.addContactToGroup(user, selectedGroup, selectedContactId);
//        System.out.println(Dao.getGroupUserContacts(user));
    }

    private void handleDeleteContactFromGroup(HttpServletRequest request, User user) {
        String selectedGroup = request.getParameter("selectedgroup");
        int selectedContactId = Integer.parseInt(request.getParameter("selectedcontact"));
        Dao.deleteContactFromGroup(user, selectedGroup, selectedContactId);
//        System.out.println(Dao.getGroupUserContacts(user));
    }
    
    private void handleSetPrimaryEmail(HttpServletRequest request, User user) {
    	System.out.println("inside this set primary email");
    	String selected_mail = request.getParameter("primaryemail");
    	System.out.println(selected_mail);
    	Dao.setPrimaryEmail(selected_mail, user);
    }

    private void updateUserSessionData(User user) {
        user.setGroup_contacts(Dao.getGroupUserContacts(user));
        user.setUserGroups(Dao.getUserGroups(user));
        user.setmails(Dao.getEmails(user));
//        SessionDataManager.users_data.put(user.getUser_id(), user);
    }
}
