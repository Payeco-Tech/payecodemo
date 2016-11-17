package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.payeco.qrorderen.PlaceOrder;
import com.payeco.util.HttpClientUtil;

public class ReckonBridgeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("request.getRequestURL(): "+ req.getRequestURL() + "?" + req.getQueryString());
		String defalut = "https://10.123.1.71:9444/third/OrderServerWeb?"+"httpOrg/create";
		//resp.sendRedirect(defalut+"?"+req.getQueryString());

		Map<String,String> createMap = new HashMap<String,String>(); 
		Map<String, String[]> params = req.getParameterMap(); 
        for (String key : params.keySet()) {  
            String[] values = params.get(key);  
            for (int i = 0; i < values.length; i++) {  
                String value = values[i];  
                createMap.put(key,value);
            }  
        }  
		String result = new HttpClientUtil().doPost(defalut, createMap,"utf-8");
		
		//String result = new HttpClientUtil().doGet("https://10.123.1.71:9444/third/OrderServerWeb?"+req.getQueryString());

		response(resp, result);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	public static void response(HttpServletResponse resp, String respData) {
		PrintWriter out = null;   
		try {
			// 设置回发内容编码，防止弹出的信息出现乱码
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			out = resp.getWriter(); 
			out.print(respData);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String generateDoGetUrl(HttpServletRequest request, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();  
	        writer.println("GET " + request.getRequestURL() + " "  
	                + request.getQueryString());  
	  
	        Map<String, String[]> params = request.getParameterMap();  
	        String queryString = "";  
	        for (String key : params.keySet()) {  
	            String[] values = params.get(key);  
	            for (int i = 0; i < values.length; i++) {  
	                String value = values[i];  
	                queryString += key + "=" + value + "&";  
	            }  
	        }  
	        // 去掉最后一个空格  
	        queryString = queryString.substring(0, queryString.length() - 1);  
	        writer.println("GET " + request.getRequestURL() + " " + queryString); 
	        return request.getRequestURL() + " " + request.getQueryString();  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static String generateDoPostUrl(HttpServletRequest request, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();  
		    Map<String, String[]> params = request.getParameterMap();  
		    String queryString = "";  
		    for (String key : params.keySet()) {  
		        String[] values = params.get(key);  
		        for (int i = 0; i < values.length; i++) {  
		            String value = values[i];  
		            queryString += key + "=" + value + "&";  
		        }  
		    }  
		    // 去掉最后一个空格  
		    queryString = queryString.substring(0, queryString.length() - 1);  
		    writer.println("POST " + request.getRequestURL() + " " + queryString); 
			return request.getRequestURL() + " " + queryString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
