package com.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbObjects.SessionsObj;
import com.session.SessionDataManager;

/**
 * Servlet implementation class SessionUpdater
 */
@WebServlet("/sru")
public class ResourceRemover extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResourceRemover() {
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
//		
//		if(action.equals("SessionResourceUpdate")) {
//			String session_id = request.getParameter("session_id");
//			Long last_accessed_time = Long.parseLong(request.getParameter("last_accessed_time"));
//			System.out.println("session id :"+session_id);
//			System.out.println("last_accessed_time :"+last_accessed_time);
//			System.out.println("request auth type "+request.getAuthType());
//			System.out.println("request local address "+request.getLocalAddr());
//			System.out.println("request local port "+request.getLocalPort());
//			System.out.println("request remote addr "+request.getRemoteAddr());
//			System.out.println("request remote host "+request.getRemoteHost());
//			System.out.println("request remote port "+request.getRemotePort());
//			System.out.println("request remote user "+request.getRemoteUser());
//			System.out.println("request server name "+request.getServerName());
//			System.out.println("request server port "+request.getServerPort());
//			System.out.println("request "+request.getHeader("user-agent"));
//			
//		}
		if("SessionResourceUpdate".equals(action)) {
			System.out.println(action); 
	        String session_id = request.getParameter("session_id");
	        String lastAccessedTimeStr = request.getParameter("last_accessed_time");
	        Long last_accessed_time;
	        
	        if (session_id == null || session_id.isEmpty()) {
//	            response.getWriter().write("Error: session_id is required.");
	            return;
	        }

	        if (lastAccessedTimeStr == null || lastAccessedTimeStr.isEmpty()) {
//	            response.getWriter().write("Error: last_accessed_time is required.");
	            return;
	        }

	        try {
	            last_accessed_time = Long.parseLong(lastAccessedTimeStr);
//	            System.out.println("session id :" + session_id);
//	            System.out.println("last_accessed_time :" + last_accessed_time);
	        } catch (NumberFormatException e) {
	            response.getWriter().write("Error: last_accessed_time must be a valid number.");
	            return;
	        }
	        SessionsObj sessiondata = SessionDataManager.session_data.get(session_id);
	        Long last_accessed_time_from_network = Long.parseLong(lastAccessedTimeStr);
	        
	        System.out.println("Before Updating :"+SessionDataManager.session_data);
	        
	        if (sessiondata!=null &&  sessiondata.getLastAccessedTime() < last_accessed_time)  {
	        	System.out.println("setting last accessed time");
	        	sessiondata.setLastAccessedTime(last_accessed_time_from_network);
	        }
	        System.out.println("AFter Updating :"+SessionDataManager.session_data);
	        return ;
	    }
		if("SessionResourceDelete".equals(action)) {
			System.out.println(SessionDataManager.session_data);
			SessionDataManager.session_data.remove(request.getParameter("session_id"));
			System.out.println(SessionDataManager.session_data);
			return;
		}
	
		
	}

}
