package com.filters;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

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
import com.loggers.AppLogger;
import com.loggers.ReqLogger;
import com.session.SessionDataManager;

@WebFilter("/*")
public class SessionFilter extends HttpFilter implements Filter {
	private static AppLogger logger =new  AppLogger(SessionFilter.class.getName());
    private static final long serialVersionUID = 1L;

	public static final ThreadLocal<Integer> user_id = new ThreadLocal<>();
    
    private static final Set<String> entryPages = new HashSet<>();
    
    static {
        entryPages.add("/contacts/login.jsp");
        entryPages.add("/contacts/login");
        entryPages.add("/contacts/signup.jsp");
        entryPages.add("/contacts/signup");
        entryPages.add("/contacts/api/v1/oauth2/login");
        entryPages.add("/contacts/api/v1/oauth2/token");
        entryPages.add("/contacts/api/v1/resource/profile");
        entryPages.add("/contacts/testoauthcallback");
        entryPages.add("/contacts/glogin");
        entryPages.add("/contacts/glogincallback");
    }

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
        
        if(entryPages.contains(httpReq.getRequestURI())) {
        	chain.doFilter(httpReq, httpRes);
        	return;
        }
        // Handle session update requests
        if (httpReq.getRequestURI().contains("sru")) {
            System.out.println("Action: " + httpReq.getParameter("action"));
            chain.doFilter(request, response);
            return;
        }
        // Retrieve session_id from cookies
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
        
        // Check and handle missing or invalid session_id
        if (sid == null) {
            handleMissingSession(httpReq, httpRes, chain);
            return;
        }
        
        // Validate session
        if (!SessionDataManager.session_data.containsKey(sid)) {
            validateAndStoreSession(sid, httpRes);
        }
        
        // Retrieve session data
        SessionsObj sessionData = SessionDataManager.session_data.get(sid);
        if (sessionData != null) {
            handleValidSession(sessionData, sid, httpReq, httpRes, chain);
        } else {
           chain.doFilter(httpReq, httpRes);
        }
    }

    private void handleMissingSession(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain) 
            throws IOException, ServletException {
        String requestURI = httpReq.getRequestURI();
        String requestedPage = requestURI.substring(requestURI.lastIndexOf("/") + 1).toLowerCase();
        System.out.println(requestURI);
        System.out.println(requestedPage);
        // Handle query strings (e.g., login.jsp?param=value)
//        if (requestedPage.contains("?")) {
//            requestedPage = requestedPage.substring(0, requestedPage.indexOf("?"));
//        }

        // Allow entry pages to bypass the filter
        if (entryPages.contains(requestedPage)) {
            chain.doFilter(httpReq, httpRes);
        } else {
            httpRes.sendRedirect("/contacts/login.jsp");
        }
    }

    private void validateAndStoreSession(String sid, HttpServletResponse httpRes) {
        SessionsDao dao = new SessionsDao();
        try {
            SessionsObj session = dao.getSessionWithId(sid);
            if (session != null && session.getUserId() != null &&
                System.currentTimeMillis() <= session.getLastAccessedTime() + 1000 * 60 * 30) {

                session.setLastAccessedTime(Instant.now().toEpochMilli());
                SessionDataManager.session_data.put(sid, session);
            } else {
                dao.deleteSession(sid);
            }
        } catch (DaoException e) {
            logger.log(Level.INFO, "Failed to validate session: " + sid,e);
        }
    }

    private void handleValidSession(SessionsObj sessionData, String sid, HttpServletRequest httpReq, 
                                    HttpServletResponse httpRes, FilterChain chain) 
            throws IOException, ServletException {
        if (System.currentTimeMillis() > sessionData.getLastAccessedTime() + 1000 * 60 * 30) {
            // Session expired
            SessionDataManager.session_data.remove(sid);
            SessionsDao dao = new SessionsDao();
            try {
                dao.deleteSession(sid);
            } catch (DaoException e) {
               logger.log(Level.SEVERE, e.getMessage(),e);
            }
            httpRes.sendRedirect("/contacts/login.jsp");
            return;
        }

        // Update session timestamp
        sessionData.setLastAccessedTime(Instant.now().toEpochMilli());

        // Retrieve user details
        Integer userId = sessionData.getUserId();
        UserDetailsObj user = SessionDataManager.users_data.get(userId);
        if (user == null) {
            user = fetchUserDetails(userId);
        }

        if (user != null) {
            SessionDataManager.users_data.put(userId, user);
            user_id.set(userId);
            httpReq.setAttribute("user_id", userId);

            // Log request
            String logMessage = String.format("Server: %s, Method: %s, URL: %s, Remote Address: %s, User ID: %s, Session ID: %s",
                    "localhost:8280",
                    httpReq.getMethod(),
                    httpReq.getRequestURL().toString(),
                    httpReq.getRemoteAddr(),
                    userId,
                    sid);
            ReqLogger.AccessLog(logMessage);

            chain.doFilter(httpReq, httpRes);
        } else {
            httpRes.sendRedirect("/contacts/login.jsp");
        }
    }

    private UserDetailsObj fetchUserDetails(Integer userId) {
        UserDetailsDao dao = new UserDetailsDao();
        try {
            return dao.getUserWithId(userId);
        } catch (DaoException e) {
            logger.log(Level.SEVERE, e.getMessage(),e);
            return null;
        }
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
