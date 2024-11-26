 package com.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.startup.SetContextPropertiesRule;

import com.dao.ContactMailsDao;
import com.dao.ContactMobileNumbersDao;
import com.dao.ContactsDao;
import com.dao.NewDao;
import com.dbObjects.ContactMailsObj;
import com.dbObjects.ContactMobileNumbersObj;

/**
 * Servlet implementation class ContactOp
 */
@WebServlet("/contactOp")
public class ContactOp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactOp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if(action.equals("viewnumbers")) {
			ContactMobileNumbersDao dao = new ContactMobileNumbersDao();
			

			List<ContactMobileNumbersObj> numbers = dao.getContactMobileNumbers(Integer.parseInt(request.getParameter("contact_id")));
			request.setAttribute("numbers", numbers);
			request.getRequestDispatcher("numbers.jsp").forward(request, response);
			return;
		}
		if(action.equals("viewmails")) {
			ContactMailsDao dao = new ContactMailsDao();
			
			List<ContactMailsObj> mails = dao.getMailsWithContactId(Integer.parseInt(request.getParameter("contact_id")));
			request.setAttribute("mails", mails);
			request.getRequestDispatcher("mails.jsp").forward(request, response);
			return;
		}
		if(action.equals("viewGroups")) {
			
			List<String> groupnames = NewDao.getContactGroups(Integer.parseInt(request.getParameter("contact_id")));
			request.setAttribute("groupnames", groupnames);
			request.getRequestDispatcher("groups.jsp").forward(request, response);
			return;
		}
		if(action.equals("deleteContact")) {
			NewDao.deleteContact(Integer.parseInt(request.getParameter("contact_id")));
			response.sendRedirect("usercontacts.jsp");
		}
	}

}
