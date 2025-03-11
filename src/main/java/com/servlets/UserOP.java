package com.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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
import com.session.SessionDataManager;

@WebServlet("/userOp")
public class UserOP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserOP.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			return;
		}

		try {
			switch (action) {
			case "setPrimaryEmail":
				handleSetPrimaryEmail(request, response);
				break;
			case "deleteEmail":
				handleDeleteEmail(request, response);
				break;
			case "viewGroupContacts":
				handleViewGroupContacts(request, response);
				break;
			case "addContact":
				handleAddContact(request, response);
				break;
			case "addEmail":
				handleAddEmail(request, response);
				break;
			case "profileUpdate":
				handleProfileUpdate(request, response);
				break;
			case "deleteGroup":
				handleDeleteGroup(request, response);
				break;
			case "addGroup":
				handleAddGroup(request, response);
				break;
			case "syncContacts":
				handleSyncContacts(request, response);
				break;
			case "selSyncProvider":
				handleSelectSyncProvider(request, response);
				break;
			case "mergeContacts":
				handleMergeContacts(request, response);
				break;
			case "addContactToGroup":
				handleAddContactToGroup(request, response);
				break;
			default:
				logger.warn("Unknown action requested: " + action);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"An error occurred processing your request");
		}
	}

	private void handleAddContactToGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GroupContactsDao dao = new GroupContactsDao();
		try {
			dao.addContactToGroup(Integer.valueOf(request.getParameter("groupId")), Integer.valueOf(request.getParameter("contactId")));
			response.sendRedirect("usergroups.jsp");
		} catch (NumberFormatException | DaoException e) {
			e.printStackTrace();
			logger.warn(e.getMessage(),e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"An error occurred processing your request");
		}
	}

	private void handleSetPrimaryEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserMailsDao dao = new UserMailsDao();
		try {
			dao.setPrimaryMail(SessionFilter.USER_ID.get(), request.getParameter("emailId"));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DaoException e) {
			logger.warn("Failed to set primary email", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	private void handleDeleteEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserMailsDao dao = new UserMailsDao();
		try {
			dao.deleteMailForUser(SessionFilter.USER_ID.get(), request.getParameter("emailId"));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (DaoException e) {
			logger.warn("Failed to delete email", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	private void handleViewGroupContacts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		GroupContactsDao groupDao = new GroupContactsDao();
		ContactsDao contactsDao = new ContactsDao();
		List<ContactsObj> contacts = new ArrayList<>();

		try {
			int groupId = Integer.parseInt(request.getParameter("group_id"));
			List<GroupContactsObj> groupContacts = groupDao.getGroupContactIds(groupId);

			for (GroupContactsObj gc : groupContacts) {
				try {
					contacts.add(contactsDao.getContactWithId(gc.getContactId()));
				} catch (DaoException e) {
					logger.info("Failed to get contact with ID: " + gc.getContactId(), e);
				}
			}

			request.setAttribute("contacts", contacts);
			request.getRequestDispatcher("groupcontacts.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			logger.warn("Invalid group ID format", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid group ID");
		} catch (DaoException e) {
			logger.warn("Database error while fetching group contacts", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleAddContact(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ContactsDao dao = new ContactsDao();
		try {
			dao.addContact(request.getParameter("firstName"), request.getParameter("lastName"),
					SessionFilter.USER_ID.get());
			response.sendRedirect("usercontacts.jsp");
		} catch (DaoException e) {
			logger.warn("Failed to add contact", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleAddEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserMailsDao dao = new UserMailsDao();
		try {
			dao.addMailForUser(SessionFilter.USER_ID.get(), request.getParameter("email"));
			response.sendRedirect("usermails.jsp");
		} catch (DaoException e) {
			logger.warn("Failed to add email", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserDetailsDao dao = new UserDetailsDao();
		try {
			dao.updateUser(SessionFilter.USER_ID.get(), request.getParameter("user_name"),
					request.getParameter("first_name"), request.getParameter("last_name"),
					request.getParameter("contactType"));

			// Clear cached user data to force refresh
			SessionDataManager.usersData.remove(SessionFilter.USER_ID.get());
			response.sendRedirect("profile.jsp");
		} catch (DaoException e) {
			logger.warn("Failed to update profile", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleDeleteGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserGroupsDao dao = new UserGroupsDao();
		try {
			Integer groupId = Integer.parseInt(request.getParameter("group_id"));
			dao.deleteGroupForUser(SessionFilter.USER_ID.get(), groupId);
			response.sendRedirect("usergroups.jsp");
		} catch (NumberFormatException e) {
			logger.warn("Invalid group ID format", e);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid group ID");
		} catch (DaoException e) {
			logger.info("Failed to delete group", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleAddGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserGroupsDao dao = new UserGroupsDao();
		try {
			dao.addGroupToUser(SessionFilter.USER_ID.get(), request.getParameter("group_name"));
			response.sendRedirect("usergroups.jsp");
		} catch (DaoException e) {
			logger.warn("Failed to add group", e);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		}
	}

	private void handleSyncContacts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GoogleContactsSyncHandler handler = new GoogleContactsSyncHandler();
		response.sendRedirect(handler.getAuthUrl());
	}

	private void handleSelectSyncProvider(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("syncproviders.jsp").forward(request, response);
	}

	private void handleMergeContacts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] contactIds = request.getParameterValues("contactIds");
		if (contactIds != null) {
			for (String contactId : contactIds) {
				System.out.println(contactId);
			}
		}
		response.sendRedirect("usercontacts.jsp");
	}
}