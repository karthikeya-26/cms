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
import com.dao.DaoException;
import com.dao.GroupContactsDao;
import com.dao.UserDetailsDao;
import com.dao.UserGroupsDao;
import com.dao.UserMailsDao;
import com.dbObjects.ContactsObj;
import com.dbObjects.GroupContactsObj;
import com.filters.SessionFilter;
import com.handlers.GoogleContactsSyncHandler;
import com.loggers.AppLogger;
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
			UserMailsDao dao = new UserMailsDao();
			try {
				dao.setPrimaryMail(SessionFilter.user_id.get(), request.getParameter("mail_id"));
			} catch (DaoException e) {
				response.sendError(400, e.getMessage());
			}
			response.sendRedirect("usermails.jsp");
		}
		else if(action.equals("viewGroupContacts")) {
			GroupContactsDao dao = new GroupContactsDao();
			ContactsDao cdao = new ContactsDao();
			List<GroupContactsObj> group_contact_ids = null;
			try {
				group_contact_ids = dao.getGroupContactIds(Integer.parseInt(request.getParameter("group_id")));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<ContactsObj> contacts = new ArrayList<ContactsObj>();
			for(GroupContactsObj gc: group_contact_ids) {
				try {
					contacts.add(cdao.getContactWithId(gc.getContactId()));
				} catch (DaoException e) {
					e.printStackTrace();
					AppLogger.ApplicationLog(e.getMessage());
					AppLogger.ApplicationLog(e);
				}
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
			
			ContactsDao dao = new ContactsDao();
			try {
				dao.addContact(request.getParameter("firstName"), request.getParameter("lastName"), SessionFilter.user_id.get());
			} catch (DaoException e) {
				AppLogger.ApplicationLog(e.getMessage());
				e.printStackTrace();
			}
			
			response.sendRedirect("usercontacts.jsp");
		}
		else if("addEmail".equals(action)) {
			UserMailsDao dao = new UserMailsDao();
			try {
				dao.addMailForUser(SessionFilter.user_id.get(), request.getParameter("email"));
			} catch (DaoException e) {
				AppLogger.ApplicationLog(e.getMessage());
				e.printStackTrace();
			}
			response.sendRedirect("useremails.jsp");
		}
		else if(action.equals("profileUpdate")) {
			UserDetailsDao dao = new UserDetailsDao();
			try {
				dao.updateUser(SessionFilter.user_id.get(),
						request.getParameter("user_name"),
						request.getParameter("first_name"),
						request.getParameter("last_name"),
						request.getParameter("contactType"));
			} catch (DaoException e) {
				e.printStackTrace();
				AppLogger.ApplicationLog("Failed to update profile");
				AppLogger.ApplicationLog(e);
;			}
			SessionDataManager.users_data.remove(SessionFilter.user_id.get());
			response.sendRedirect("profile.jsp");
			Integer i = 2;
			int j = i;
			return;
		}
		else if("deleteGroup".equals(action)) {
			UserGroupsDao dao = new UserGroupsDao();
			Integer groupId = Integer.parseInt(request.getParameter("group_id"));
			try {
				dao.deleteGroupForUser(groupId);
			} catch (DaoException e) {	
				response.sendError(500, e.getMessage());
			}

			response.sendRedirect("usergroups.jsp");
		}
		else if("addGroup".equals(action)) {
			UserGroupsDao dao = new UserGroupsDao();
			try {
				dao.addGroupToUser(SessionFilter.user_id.get(), request.getParameter("group_name"));
			} catch (DaoException e) {
				response.sendError(404, e.getMessage());
			}
			response.sendRedirect("usergroups.jsp");
		}
		else if("syncContacts".equals(action)) {
			//auth token 
			GoogleContactsSyncHandler h = new GoogleContactsSyncHandler();
			response.sendRedirect(h.getAuthUrl());
		}
		else if("selSyncProvider".equals(action)) {
			RequestDispatcher rd = request.getRequestDispatcher("syncproviders.jsp");
			rd.forward(request, response);
			return;
		}
	
	}

}
