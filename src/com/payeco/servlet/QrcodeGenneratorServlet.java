package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.payeco.qrorderen.AliScanBarPlaceOrder;
import com.payeco.qrorderen.AliqrcodePlaceOrder;
import com.payeco.qrorderen.BeecloudWxNativePlaceOrder;
import com.payeco.qrorderen.PlaceOrder;
import com.payeco.qrorderen.WxJSAPIPlaceOrder;
import com.payeco.qrorderen.WxNativePlaceOrder;
import com.payeco.qrorderen.WxScanBarPlaceOrder;
import com.payeco.util.JsonUtil;
import com.payeco.util.LRUCache;

public class QrcodeGenneratorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	     doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String orderType = req.getParameter("orderType");
		String amount = req.getParameter("amount");
		String merchantNo = req.getParameter("merchantNo");
		String merchantPwd = req.getParameter("merchantPwd");
		if("TRIP_SCAN".equals(orderType)) {
			String orderenResult = new PlaceOrder().assemble(amount, merchantNo, merchantPwd);
			String sdkInitDate = orderenResult.substring(orderenResult.indexOf("<SdkInitData>")+13, 
					orderenResult.indexOf("</SdkInitData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(sdkInitDate);
			req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
			req.setAttribute("OrderNo", map.get("OrderNo"));
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("MS_ALI_QRCODE".equals(orderType)) {
			String orderenResult = new AliqrcodePlaceOrder().assemble(amount, merchantNo, merchantPwd);
			String apiExtData = orderenResult.substring(orderenResult.indexOf("<ApiExtData>")+12, 
					orderenResult.indexOf("</ApiExtData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(apiExtData);
			req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
			req.setAttribute("OrderNo", map.get("OrderNo"));
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("MS_WX_NATIVE".equals(orderType) || "PF_WX_NATIVE".equals(orderType)) {
			String orderenResult = new WxNativePlaceOrder().assemble(amount, merchantNo, merchantPwd);
			String apiExtData = orderenResult.substring(orderenResult.indexOf("<ApiExtData>")+12, 
					orderenResult.indexOf("</ApiExtData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(apiExtData);
			req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
			req.setAttribute("OrderNo", map.get("OrderNo"));
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("BC_WX_NATIVE".equals(orderType)) {
			String orderenResult = new BeecloudWxNativePlaceOrder().assemble(amount, merchantNo, merchantPwd);
			String apiExtData = orderenResult.substring(orderenResult.indexOf("<ApiExtData>")+12, 
					orderenResult.indexOf("</ApiExtData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(apiExtData);
			req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
			req.setAttribute("OrderNo", map.get("OrderNo"));
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("MS_WX_JSAPI".equals(orderType)) {
			String openid = req.getParameter("openid");
			String orderenResult = new WxJSAPIPlaceOrder().assemble(openid, amount, merchantNo, merchantPwd);
			String apiExtData = orderenResult.substring(orderenResult.indexOf("<ApiExtData>")+12, 
					orderenResult.indexOf("</ApiExtData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(apiExtData);
			String orderNo = map.get("OrderNo");
			req.setAttribute("OrderNo", orderNo);
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			String app_id = map.get("AppId");
			String timestamp = map.get("Timestamp");
			String nonce_str = map.get("NonceStr");
			String Package = map.get("Package");
			String sign_type = map.get("SignType");
			String pay_sign = map.get("PaySign");
			String initDate = "jsapiAppid="
					+app_id+"&timeStamp="+timestamp+"&nonceStr="+nonce_str+"&jsapipackage="
					+Package+"&signType="+sign_type+"&paySign="+pay_sign;
			//req.setAttribute("QrCodeUrl", "http://test.payeco.com:9080/payecodemo/wxjsapi?tk="+orderNo);
			//req.setAttribute("QrCodeUrl", "http://10.123.76.53:8080/payecodemo/wxjsapi?tk="+orderNo);
			req.setAttribute("QrCodeUrl", "http://10.123.1.56:9080/payecodemo/wxjsapi?tk="+orderNo);
			LRUCache.getInstance().put(orderNo, initDate);
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("PF_ALI_QRCODE".equals(orderType)) {	//浦发支付宝扫码
			String orderenResult = new AliqrcodePlaceOrder().assemble(amount, merchantNo, merchantPwd);
			String apiExtData = orderenResult.substring(orderenResult.indexOf("<ApiExtData>")+12, 
					orderenResult.indexOf("</ApiExtData>"));
			
			Map<String, String> map = (Map<String, String>) JsonUtil.jsonStr2Map(apiExtData);
			req.setAttribute("QrCodeUrl", map.get("QrCodeUrl"));
			req.setAttribute("OrderNo", map.get("OrderNo"));
			req.setAttribute("MerchantNo", map.get("MerchantNo"));
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("PF_ALI_SCAN_BAR".equals(orderType)) {	//浦发支付宝条码
			String authCode = req.getParameter("authCode");
			String orderenResult = new AliScanBarPlaceOrder().assemble(authCode, amount, merchantNo, merchantPwd);
			req.setAttribute("Remark", "支付结果：支付成功，请以后台订单状态为主。");
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} else if("PF_WX_SCAN_BAR".equals(orderType)) {	//浦发微信条码
			String authCode = req.getParameter("authCode");
			String orderenResult = new WxScanBarPlaceOrder().assemble(authCode, amount, merchantNo, merchantPwd);
			req.setAttribute("Remark", "支付结果：支付成功，请以后台订单状态为主。");
			
			RequestDispatcher rd = req.getRequestDispatcher("/qrcode.jsp");
			rd.forward(req,resp);
		} 
		
	}

}
