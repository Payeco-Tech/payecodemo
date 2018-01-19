package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.payeco.util.LRUCache;

/** 
 * @author fengmuhai
 * @date 2016-11-3 ÉÏÎç11:14:51 
 * @version 1.0  
 */
public class WXJSAPIServlet extends HttpServlet {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_agent = request.getHeader("user-agent");
		if(user_agent.toLowerCase().contains("micromessenger")) {
			String tk = request.getParameter("tk");
			if(null != tk && !"".equals(tk.trim())) {
				String initDate = LRUCache.getInstance().get(tk);
				String from = "https://web.payeco.com/views/jsp/bcjsapi.jsp?" + initDate;
				response.sendRedirect(from);
			} else {
				response.sendRedirect("https://www.baidu.com/");
			}
			
		} else {
			response.sendRedirect("https://www.baidu.com/");
		}
		
	}
}
