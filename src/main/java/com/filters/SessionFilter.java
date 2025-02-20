package com.filters;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

import org.apache.log4j.Logger;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dao.UserDetailsDao;
import com.dbObjects.SessionsObj;
import com.dbObjects.UserDetailsObj;
import com.loggers.ReqLogger;
import com.session.SessionDataManager;

@WebFilter("/*")
public class SessionFilter extends HttpFilter implements Filter {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(SessionFilter.class);
    private static final int SESSION_TIMEOUT_MS = 1000 * 60 * 30; // 30 minutes
    
    public static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    public static final ThreadLocal<String> SESSION_ID = new ThreadLocal<>();
    public static final ThreadLocal<String> ENDPOINT = new ThreadLocal<>();
    public static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    private static final Set<String> ENTRY_PAGES = new HashSet<>();

    static {
     
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        
        String requestUri = httpReq.getRequestURI();
        if (ENTRY_PAGES.contains(requestUri)) {
            chain.doFilter(httpReq, httpRes);
            return;
        }
        
        ENDPOINT.set(requestUri);
        String sessionId = getSessionIdFromCookies(httpReq);
        if (sessionId == null) {
            handleMissingSession(httpReq, httpRes, chain);
            return;
        }
        if (!SessionDataManager.sessionData.containsKey(sessionId)) {
            if (!validateAndStoreSession(sessionId, httpRes)) {
                return; 
            }
        }

        SessionsObj sessionData = SessionDataManager.sessionData.get(sessionId);
        if (sessionData != null) {
            SESSION_ID.set(sessionId);
            ROLE.set("user");
            handleValidSession(sessionData, sessionId, httpReq, httpRes, chain);
        }
        
        USER_ID.remove();
        SESSION_ID.remove();
        ENDPOINT.remove();
        ROLE.remove();
    }

    private String getSessionIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void handleMissingSession(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain)
            throws IOException, ServletException {
        String requestURI = httpReq.getRequestURI();
        String requestedPage = requestURI.substring(requestURI.lastIndexOf("/") + 1).toLowerCase();
        
        if (ENTRY_PAGES.contains(requestedPage)) {
            chain.doFilter(httpReq, httpRes);
        } else {
            httpRes.sendRedirect("/contacts/login.jsp");
        }
    }

    private boolean validateAndStoreSession(String sessionId, HttpServletResponse httpRes) throws IOException {
        SessionsDao dao = new SessionsDao();
        try {
            SessionsObj session = dao.getSessionWithId(sessionId);
            if (isValidSession(session)) {
                session.setLastAccessedTime(Instant.now().toEpochMilli());
                SessionDataManager.sessionData.put(sessionId, session);
                return true;
            } else {
                dao.deleteSession(sessionId);
                httpRes.sendRedirect("login.jsp");
                return false;
            }
        } catch (DaoException e) {
            logger.info(e);
            return false;
        }
    }
    
    private boolean isValidSession(SessionsObj session) {
        return session != null && 
               session.getUserId() != null &&
               System.currentTimeMillis() <= session.getLastAccessedTime() + SESSION_TIMEOUT_MS;
    }

    private void handleValidSession(SessionsObj sessionData, String sessionId, HttpServletRequest httpReq,
            HttpServletResponse httpRes, FilterChain chain) throws IOException, ServletException {
        if (System.currentTimeMillis() > sessionData.getLastAccessedTime() + SESSION_TIMEOUT_MS) {
            SessionDataManager.sessionData.remove(sessionId);
            try {
                new SessionsDao().deleteSession(sessionId);
            } catch (DaoException e) {
                logger.info(e);
            }
            httpRes.sendRedirect("/contacts/login.jsp");
            return;
        }
        
        sessionData.setLastAccessedTime(Instant.now().toEpochMilli());
        SessionDataManager.sessionData.put(sessionId, sessionData);
        Integer userId = sessionData.getUserId();
        UserDetailsObj user = getUserDetails(userId);
        
        if (user == null) {
        	httpRes.sendRedirect("/contacts/login.jsp");
        } 
        USER_ID.set(userId);
        httpReq.setAttribute("user_id", userId);
        logRequest(httpReq, userId, sessionId);
        chain.doFilter(httpReq, httpRes);
    }

    private UserDetailsObj getUserDetails(Integer userId) {
        UserDetailsObj user = SessionDataManager.usersData.get(userId);
        if (user == null) {
            user = fetchUserDetails(userId);
            if (user != null) {
                SessionDataManager.usersData.put(userId, user);
            }
        }
        return user;
    }

    private UserDetailsObj fetchUserDetails(Integer userId) {
        UserDetailsDao dao = new UserDetailsDao();
        try {
            return dao.getUserWithId(userId);
        } catch (DaoException e) {
            logger.info(e);
            return null;
        }
    }
    
    private void logRequest(HttpServletRequest request, Integer userId, String sessionId) {
        String logMessage = String.format(
                "Server: %s, Method: %s, URL: %s, Remote Address: %s, User ID: %s, Session ID: %s",
                "localhost:8280", request.getMethod(), request.getRequestURL().toString(), 
                request.getRemoteAddr(), userId, sessionId);
        logger.info(logMessage);
        ReqLogger.AccessLog(logMessage);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    	   ENTRY_PAGES.add("/contacts/login.jsp");
           ENTRY_PAGES.add("/contacts/login");
           ENTRY_PAGES.add("/contacts/signup.jsp");
           ENTRY_PAGES.add("/contacts/signup");
           ENTRY_PAGES.add("/contacts/api/v1/oauth2/login");
           ENTRY_PAGES.add("/contacts/api/v1/oauth2/token");
           ENTRY_PAGES.add("/contacts/api/v1/resource/profile");
           ENTRY_PAGES.add("/contacts/testoauthcallback");
           ENTRY_PAGES.add("/contacts/glogin");
           ENTRY_PAGES.add("/contacts/glogincallback");
           ENTRY_PAGES.add("/contacts/sru");
    }

    @Override
    public void destroy() {
    }
}