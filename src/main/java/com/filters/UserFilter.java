// Source code is decompiled from a .class file using FernFlower decompiler.
package com.filters;

import com.models.SessionUserData;
import com.session.SessionData;

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

   public UserFilter() {
   }

   public void destroy() {
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	   
	   System.out.println("inside user filter" + count++);
	   HttpServletRequest httpReq = (HttpServletRequest)request;
	   HttpServletResponse httpRes = (HttpServletResponse)response;
	   Cookie[] cookies = httpReq.getCookies();
	   boolean isValidUser = false;
	   String sid = null;
	   
	   if (cookies != null) {
	       for(int i = 0; i < cookies.length; i++) {
	           if(cookies[i].getName().equals("sid") && SessionData.checkSid(cookies[i].getValue())) {
	               isValidUser = true;
	               sid = cookies[i].getValue();
	               break;
	           }
	       }
	   }
	   
	   SessionUserData user = isValidUser ? SessionData.getUserwithId(sid) : null;
	   
	   
	   
	   // If the user is valid and is trying to access login/signup pages, redirect to index.jsp
	   if (user != null && (httpReq.getRequestURI().endsWith("login.jsp") || httpReq.getRequestURI().endsWith("login") || httpReq.getRequestURI().endsWith("signup.jsp") || httpReq.getRequestURI().endsWith("signup"))) {
	       httpRes.sendRedirect("index.jsp");
	       return;
	   } 
	   // If user is null and trying to access any page other than login/signup, redirect to login.jsp
	   else if (user == null && !httpReq.getRequestURI().endsWith("login.jsp") && !httpReq.getRequestURI().endsWith("login") && !httpReq.getRequestURI().endsWith("signup.jsp") && !httpReq.getRequestURI().endsWith("signup")) {
	       httpRes.sendRedirect("login.jsp");
	       return;
	   } 
	   // Continue with the filter chain
	   else {
		    if (user != null && user.getExpires_at() <= System.currentTimeMillis()) {
		        SessionData.removeUserFromSession(sid);
		        httpRes.sendRedirect("login.jsp");
		        return; // Prevent further processing after redirection
		    }
		    if (user != null) {
		        user.setLast_accessed_at(System.currentTimeMillis());
		        user.setExpires_at(System.currentTimeMillis() + 1800000);
		        request.setAttribute("su_data", user);
		        System.out.println(user);
		    }
		    chain.doFilter(request, response);
		}

	}


   public void init(FilterConfig fConfig) throws ServletException {
   }
}
