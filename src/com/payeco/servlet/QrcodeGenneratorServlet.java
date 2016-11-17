package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.payeco.qrorderen.PlaceOrder;
import com.payeco.util.JsonUtil;

public class QrcodeGenneratorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	     doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String orderenResult = new PlaceOrder().assemble();
		String sdkInitDate = orderenResult.substring(orderenResult.indexOf("<SdkInitData>")+13, 
				orderenResult.indexOf("</SdkInitData>"));
		
		Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(sdkInitDate);
		req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
		
		RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
		rd.forward(req,resp);
	}

}
