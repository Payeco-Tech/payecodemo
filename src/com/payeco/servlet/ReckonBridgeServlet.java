package com.payeco.servlet;

import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payeco.qrorderen.PlaceOrder;
import com.payeco.util.FileUtil;
import com.payeco.util.HttpClientUtil;
import com.payeco.util.Toolkit;
import com.payeco.util.http.HttpProxyClient;

public class ReckonBridgeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ReckonBridgeServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("request.getRequestURL(): "+ req.getRequestURL() + "?" + req.getQueryString());
		String defalut = "https://10.123.1.71:9444/third/OrderServerWeb?"+"httpOrg/create";
		//resp.sendRedirect(defalut+"?"+req.getQueryString());
		
		String config = FileUtil.readLine("/home/fmh/config/payecodemo_bridge_doGet_url.txt").replace("\n", "");
		//String config = FileUtil.read("c:\\payecodemo_bridge_target_url.txt");
		//String config = "http://10.123.76.51:8080/notify";
		if(!Toolkit.isNullOrEmpty(config)) {
			defalut = config;
		}
		
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
		
		response(resp, result);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String defalut = "";
		//String config = FileUtil.readLine("/home/fmh/config/payecodemo_bridge_target_url.txt").replace("\n", "");
		//String config = FileUtil.readLine("c:\\payecodemo_bridge_target_url.txt").replace("\n", "");
		String config = "http://10.123.76.51:8080/JavaDemo/notify";
		if(!Toolkit.isNullOrEmpty(config)) {
			defalut = config;
		}
		logger.info("中转地址："+defalut);
		String content = "";
		try {
			content = getContent(req);
			logger.info("recived content:"+content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String url = "|"+config+"||10000|30000|3|20000|60000";
		String result = HttpProxyClient.httpPost(url, content.getBytes("UTF-8"), null).getData().toString();
		response(resp, result);
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
	
	/**
	 * 获取request中的content内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getContent(HttpServletRequest request) throws Exception{
		int t_l = request.getContentLength();
		byte[] bts = new byte[t_l];
        int totalLen = 0, len = 0;
        while (totalLen < t_l && (len = request.getInputStream().read(bts, totalLen, t_l-totalLen)) != -1) {
            totalLen += len;
            logger.info("readLen " + len);
        }
        if(totalLen < t_l) {
        	logger.info("read invalid request,read:" + totalLen + ",total:" + t_l);
            return null;
        }
        return new String(bts,"UTF-8");
	} 
	
	
}
