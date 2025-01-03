package com.servlets;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.loggers.AppLogger;
//import com.notifier.SessionmapUpdateNotifier;
import com.session.SessionDataManager;

import javax.servlet.http.Cookie;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cookie[] cookies = request.getCookies();
		String session_id=null;
		for(Cookie c : cookies) {
			if(c.getName().equals("session_id")) session_id = c.getValue();
		}
		
		Integer user_id = SessionDataManager.session_data.get(session_id).getUserId();
		SessionDataManager.session_data.remove(session_id);
		
		SessionDataManager.users_data.remove(user_id);
		SessionsDao dao = new SessionsDao();
		try {
			dao.deleteSession(session_id);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AppLogger.ApplicationLog(SessionDataManager.session_data.toString());
		Cookie invalidate = new Cookie("session_id", "");
		invalidate.setMaxAge(0);
		response.addCookie(invalidate);
		response.sendRedirect("login.jsp");
		
	}

}
