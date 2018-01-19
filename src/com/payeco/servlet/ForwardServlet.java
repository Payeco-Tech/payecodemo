package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payeco.qrorderen.PlaceOrder;
import com.payeco.test.Transaction;
import com.payeco.util.MD5;
import com.payeco.util.Toolkit;

/**
 * ÖÐ×ªÆ÷
 * @author user
 */
public class ForwardServlet extends HttpServlet{
	
	private static Logger logger = Logger.getLogger(ForwardServlet.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	
}
