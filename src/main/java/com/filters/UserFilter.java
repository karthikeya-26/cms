// Source code is decompiled from a .class file using FernFlower decompiler.
package com.filters;

import com.dao.Dao;
import com.loggers.ReqLogger;
import com.models.SessionData;
import com.models.User;
import com.session.SessionDataManager;

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

@SuppressWarnings("serial")
@WebFilter({"/*"})
public class UserFilter extends HttpFilter implements Filter {
   static int count = 1;
   private static final ThreadLocal<SessionData> userObj = new ThreadLocal<>();

   public UserFilter() {
   }

   public void destroy() {
	   ReqLogger.close();
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	   
	 //  System.out.println("servlet request -> "+request);
	  // System.out.println("inside user filter" + count++);
	   HttpServletRequest httpReq = (HttpServletRequest)request;
	   HttpServletResponse httpRes = (HttpServletResponse)response;
	 //  System.out.println("http request -> "+httpReq);
	 //  System.out.println(httpReq.getMethod()      );
	   Cookie[] cookies = httpReq.getCookies();
	   boolean isValidUser = false;
	   String sid = null;
	   
	   if (cookies != null) {
	       for(int i = 0; i < cookies.length; i++) {
	           if(cookies[i].getName().equals("sid") && SessionDataManager.checkSid(cookies[i].getValue())) {
	               isValidUser = true;
	               sid = cookies[i].getValue();
	               break;
	           }
	       }
	   }
	   
	   SessionData sessiondata = isValidUser ? SessionDataManager.getUserwithId(sid) : null;
	   
	   
	   
	   // If the user is valid and is trying to access login/signup pages, redirect to index.jsp
	   if (sessiondata != null && (httpReq.getRequestURI().endsWith("login.jsp") || httpReq.getRequestURI().endsWith("login") || httpReq.getRequestURI().endsWith("signup.jsp") || httpReq.getRequestURI().endsWith("signup"))) {
	       httpRes.sendRedirect("index.jsp");
	       return;
	   } 
	   // If user is null and trying to access any page other than login/signup, redirect to login.jsp
	   else if (sessiondata == null && !httpReq.getRequestURI().endsWith("login.jsp") && !httpReq.getRequestURI().endsWith("login") && !httpReq.getRequestURI().endsWith("signup.jsp") && !httpReq.getRequestURI().endsWith("signup")) {
	       httpRes.sendRedirect("login.jsp");
	       return ;
	   } 
	   // Continue with the filter chain
	   else {
		    if (sessiondata != null && sessiondata.getExpires_at() <= System.currentTimeMillis()) {
		    	
		        SessionDataManager.removeUserFromSession(sid);
		        Dao.removeUserfromSessions(sid);
		        httpRes.sendRedirect("login.jsp");
		        return; // Prevent further processing after redirection
		    }
		    if (sessiondata != null) {
		    	System.out.println("session data object"+sessiondata);
		    	System.out.println(SessionDataManager.users_data.get(sessiondata.getUser_id()));
		        sessiondata.setLast_accessed_at(System.currentTimeMillis());
		        sessiondata.setExpires_at(System.currentTimeMillis() + 180000);
		        User user = SessionDataManager.users_data.get(sessiondata.getUser_id());
		        System.out.println("user object "+user);
		        
		        if (user == null) {
		        	System.out.println("user is null getting use from db");
		        	user = Dao.getUser(sessiondata.getUser_id());
		        }
		        //System.out.println(user);
		        request.setAttribute("user_data", user);
		        
		        //logging the request
		        
		        String logMessage = String.format("Request Method: %s, URL: %s, Remote Address: %s",
		                httpReq.getMethod(),
		                httpReq.getRequestURL().toString(),
		                httpReq.getRemoteAddr());

		        ReqLogger.logRequest(logMessage);
		       
		        
		    }
		    chain.doFilter(request, response);
		}
	   
	}


   public void init(FilterConfig fConfig) throws ServletException {
   }
   
   public static SessionData getUserObj() {
       return userObj.get();
   }
}
