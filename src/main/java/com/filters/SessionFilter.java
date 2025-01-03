package com.filters;

import java.io.IOException;
import java.time.Instant;

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

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dao.UserDetailsDao;
import com.dbObjects.SessionsObj;
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
        	SessionsDao dao = new SessionsDao();
        	SessionsObj session = null;
			try {
				session = dao.getSessionWithId(sid);
			} catch (DaoException e) {
				httpRes.sendError(400,"");
			}
        	if (session != null && session.getUserId()!= null && System.currentTimeMillis() <= session.getLastAccessedTime()+1000*60*30 ) {
        		
        		session.setLastAccessedTime(Instant.now().toEpochMilli());
        		SessionDataManager.session_data.put(sid, session);
        	}else {
        		try {
					dao.deleteSession(sid);
				} catch (DaoException e) {
					AppLogger.ApplicationLog("Failed to delete session :"+sid);
					AppLogger.ApplicationLog(e);
				}
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
        SessionsObj sessiondata = SessionDataManager.session_data.get(sid);
        if (sessiondata != null) {
        	
        	if (!(System.currentTimeMillis()<= sessiondata.getLastAccessedTime()+1000*60*30)) {
        		SessionsDao dao = new SessionsDao();
        		try {
					dao.deleteSession(sid);
				} catch (DaoException e) {
					AppLogger.ApplicationLog(e);
				}
        		SessionDataManager.session_data.remove(sid);
        	}
        	
            // Update session data with new access and expiration times
            sessiondata.setLastAccessedTime(Instant.now().toEpochMilli());
            // Retrieve the user details associated with this session
            UserDetailsObj user = SessionDataManager.users_data.get(sessiondata.getUserId());
            if (user == null) {
                // Load user details if not already in users_data
            	UserDetailsDao dao = new UserDetailsDao();
            	try {
					user = dao.getUserWithId(sessiondata.getUserId());
				} catch (DaoException e) {
					AppLogger.ApplicationLog("Failed to get user");
					AppLogger.ApplicationLog(e);
				}
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
	                sessiondata.getUserId(),
	                sid);
            ReqLogger.AccessLog(logMessage);
          
            chain.doFilter(request, response);
        } else {
            httpRes.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
