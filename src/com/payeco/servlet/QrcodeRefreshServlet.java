package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.payeco.qrorderen.PlaceOrder;
import com.payeco.qrorderen.QrcodeRefresh;
import com.payeco.util.JsonUtil;

public class QrcodeRefreshServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	     doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		//resp.setContentType("text/html;charset=utf-8");
		System.out.println("req.getParameter(OrderNo): "+req.getParameter("OrderNo"));
		System.out.println("req.getParameter(MerchantNo): "+req.getParameter("MerchantNo"));
		String result = new QrcodeRefresh().assemble(req.getParameter("OrderNo"), req.getParameter("MerchantNo"));
		String sdkInitDate = result.substring(result.indexOf("<SdkInitData>")+13, 
				result.indexOf("</SdkInitData>"));
		
		Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(sdkInitDate);
		req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
		
		PrintWriter out = resp.getWriter();
		out.print(map.get("QrCodeUrl"));
		out.flush();
		out.close();
		
		//RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
		//rd.forward(req,resp);
	}

}
