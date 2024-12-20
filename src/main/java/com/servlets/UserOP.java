package com.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ContactsDao;
import com.dao.GroupContactsDao;
import com.dao.NewDao;
import com.dao.UserDetailsDao;
import com.dbObjects.ContactsObj;
import com.dbObjects.GroupContactsObj;
import com.filters.SessionFilter;
import com.handlers.GoogleContactsSyncHandler;
import com.session.SessionDataManager;

/**
 * Servlet implementation class UserOP
 */
@WebServlet("/userOp")
public class UserOP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserOP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HI MOWA");
		
		String action = request.getParameter("action");
		System.out.println(action);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action == null) {
			return;
		}
		
		if(action.equals("setprimarymail")) {
			NewDao.setPrimaryMail(SessionFilter.user_id.get(), request.getParameter("mail_id"));
			response.sendRedirect("usermails.jsp");
		}
		else if(action.equals("viewGroupContacts")) {
			GroupContactsDao dao = new GroupContactsDao();
			ContactsDao cdao = new ContactsDao();
			List<GroupContactsObj> group_contact_ids = dao.getGroupContactIds(Integer.parseInt(request.getParameter("group_id")));
			List<ContactsObj> contacts = new ArrayList<ContactsObj>();
			for(GroupContactsObj gc: group_contact_ids) {
				contacts.add(cdao.getContactWithId(gc.getContactId()));
			}
			request.setAttribute("contacts", contacts);
			request.getRequestDispatcher("groupcontacts.jsp").forward(request, response);
			return;
		}
		else if(action.equals("addContact")) {
			ArrayList<Long> numbers = new ArrayList<Long>();
			numbers.add(Long.parseLong(request.getParameter("phoneNumber")));
			ArrayList<String> mails = new ArrayList<String>();
			mails.add(request.getParameter("email"));
			
			NewDao.addContact(SessionFilter.user_id.get(), request.getParameter("firstName"), request.getParameter("lastName"), request.getParameter("address"), numbers, mails);
			response.sendRedirect("usercontacts.jsp");
		}
		else if("addEmail".equals(action)) {
			NewDao.addEmail(SessionFilter.user_id.get(), request.getParameter("email"));
			response.sendRedirect("useremails.jsp");
		}
		else if(action.equals("profileUpdate")) {
			UserDetailsDao dao = new UserDetailsDao();
			dao.updateUser(SessionFilter.user_id.get(),
					request.getParameter("user_name"),
					request.getParameter("first_name"),
					request.getParameter("last_name"),
					request.getParameter("contactType"));
			SessionDataManager.users_data.remove(SessionFilter.user_id.get());
			response.sendRedirect("profile.jsp");
			return;
		}
		else if("deleteGroup".equals(action)) {
			NewDao.deleteGroupForUser(SessionFilter.user_id.get(),Integer.parseInt(request.getParameter("group_id")));
			response.sendRedirect("usergroups.jsp");
		}
		else if("addGroup".equals(action)) {
			NewDao.addGroupForUser(SessionFilter.user_id.get(), request.getParameter("group_name"));
			response.sendRedirect("usergroups.jsp");
		}
		else if("syncContacts".equals(action)) {
			//auth token 
			GoogleContactsSyncHandler h = new GoogleContactsSyncHandler();
			System.out.println(h.getAuthUrl());
			response.sendRedirect(h.getAuthUrl());
		}
		else if("selSyncProvider".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("syncproviders.jsp");
			rd.forward(request, response);
			return;
		}
		
		
	}

}
