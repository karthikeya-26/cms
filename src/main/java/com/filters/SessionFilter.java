package com.filters;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.NewDao;
import com.dbObjects.UserDetailsObj;
import com.dto.SessionData;
import com.loggers.AppLogger;
import com.loggers.ReqLogger;
//import com.notifier.SessionmapUpdateNotifier;
import com.servlets.ResourceRemover;
import com.session.Session;
import com.session.SessionDataManager;
//import com.startup.RegServer;

@WebFilter("/*")
public class SessionFilter extends HttpFilter implements Filter {
	public static final ThreadLocal<Integer> user_id = new ThreadLocal<Integer>();
    public SessionFilter() {
        super();
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        
        if(httpReq.getRequestURI().contains("sru")) {
        	System.out.println("action :"+httpReq.getParameter("action"));
        	System.out.println("URL"+httpReq.getRequestURL());
        	System.out.println("URI"+httpReq.getRequestURI());
        	System.out.println("session update request");
        	chain.doFilter(request, response);
        	return;
        	
        }
        Cookie[] cookies = httpReq.getCookies();
        String sid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("session_id".equals(c.getName())) { 
                    sid = c.getValue();
                    break;
                }
            }
        }
        
        if(sid != null && !SessionDataManager.session_data.containsKey(sid)) {
        	//check in db
        	System.out.println("checking in db");
        	SessionData session = NewDao.fetchSessionFromDb(sid);
        	if (session != null && session.getUser_id()!= null && System.currentTimeMillis() <= session.getLast_accessed_time()+1000*60*30 ) {
        		
        		session.setLast_accessed_at(System.currentTimeMillis());
        		session.setExpires_at(System.currentTimeMillis()+30*60*1000);
        		SessionDataManager.session_data.put(sid, session);
        	}else {
        		NewDao.removeSessionfromDb(sid);
        		sid = null;
        	}
        }
        
        
        // Redirect to login if session_id is missing
        if (sid == null  ) {
        	String requestURI = httpReq.getRequestURI();
            // Bypass the filter for login page and login servlet
            if (requestURI.endsWith("login.jsp") || requestURI.endsWith("/login") || requestURI.endsWith("/signup.jsp") || requestURI.endsWith("/signup")) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Retrieve session data
        SessionData sessiondata = SessionDataManager.session_data.get(sid);
        if (sessiondata != null) {
        	
        	if (!(System.currentTimeMillis()<= sessiondata.getLast_accessed_time()+1000*60*30)) {
        		NewDao.removeSessionfromDb(sid);
        		SessionDataManager.session_data.remove(sid);
//        		SessionmapUpdateNotifier.removeSession(sid);
        	}
        	
            // Update session data with new access and expiration times
            sessiondata.setLast_accessed_at(System.currentTimeMillis());
            sessiondata.setExpires_at(System.currentTimeMillis() + 30 * 60000); // 30 minutes from now
            // Retrieve the user details associated with this session
            UserDetailsObj user = SessionDataManager.users_data.get(sessiondata.getUser_id());
            if (user == null) {
                // Load user details if not already in users_data
                user = NewDao.getUser(sessiondata.getUser_id());
                if (user != null) {
                    SessionDataManager.users_data.put(user.getUserId(), user);
                }
            }
            //thread local
            user_id.set(user.getUserId());
            //request attribute
            request.setAttribute("user_id", user.getUserId());
            //logging the request
            String logMessage = String.format("Server Name: %s, Request Method: %s, URL: %s, Remote Address: %s, User id: %s, Session id: %s",
            		"local host 8280",
	                httpReq.getMethod(),
	                httpReq.getRequestURL().toString(),
	                httpReq.getRemoteAddr(),
	                sessiondata.getUser_id(),
	                sid);
            ReqLogger.AccessLog(logMessage);
          
            chain.doFilter(request, response);
        } else {
            // If session data is invalid, redirect to login
            httpRes.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
