package com.payeco.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.payeco.model.MerchantMessage;
import com.payeco.util.Toolkit;



public class PayecoServletNew extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	     doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		assemble(req, resp);
		
	}
	

	public String getString(String src) {
	    return (Toolkit.isNullOrEmpty(src) ? "" : (" " + src.trim()));
	}
	
	
	public void  assemble(HttpServletRequest request, HttpServletResponse response){
		
		try {
			//String url = "http://test.payeco.com:9080/services/ApiV2ServerRSA";		//测试环境地址
			String synAddress = "http://test.payeco.com:9080/payecodemo/servlet/CallBackServlet";	//同步地址
			//String asynAddress = "http://10.123.65.20:9080/payecodemo/servlet/AsynServlet";			//异步地址
			//String asynAddress = "http://10.123.76.53:8080/payecodemo/servlet/AsynServlet";	
			//String synAddress = "http://10.123.76.53:8080/payecodemo/servlet/CallBackServlet";	//同步地址
			//String synAddress = "http://10.123.76.42:8080/payecodemo/servlet/CallBackServlet";	//同步地址
			
			String asynAddress = "http://test.payeco.com:9080/payecodemo/servlet/AsynServlet";
			String GDYILIAN_CERT_PUB_64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
			
			String request_text = "";
			String srcXml="";
			String amount = request.getParameter("money");
			String curcode = Toolkit.getCurrency("01");
			String desc = new String(request.getParameter("desc").getBytes("ISO-8859-1"),"UTF-8");
			String remark = new String(request.getParameter("remark").getBytes("ISO-8859-1"),"UTF-8");
			String currency = new String(request.getParameter("currency"));
			String orderfrom = request.getParameter("orderfrom");
			
			//String merchantPwd = "123456";
			String merchantOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String acqSsn = new SimpleDateFormat("HHmmss").format(new Date());
			String transDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			//商户号
			String merchantNo = (String)request.getParameter("merchantNo");
			//url
			String url = (String)request.getParameter("url");
			String merchantPwd = request.getParameter("password");
				
			MerchantMessage msg = new MerchantMessage();
			msg.setVersion("2.1.0");
			msg.setProcCode("0200");
			msg.setProcessCode("190011");
			msg.setMerchantNo(merchantNo);	//1472181543236
			msg.setMerchantOrderNo(merchantOrderNo);
			msg.setAcqSsn(acqSsn);
			msg.setTransDatetime(transDatetime);
			msg.setAmount(amount);
			msg.setCurrency(currency);
			msg.setDescription(desc);
			msg.setSynAddress(synAddress);
			msg.setAsynAddress(asynAddress);
			msg.setOrderFrom("28");		//PC收银台
			msg.setOrderFrom(orderfrom);
			msg.setSupportPayType("|1|2|");	//值：|1|2|3|4|5|  //|银联快捷支付|微信扫码|支付宝扫码|三码合一|银联在线支付|
			//msg.setIDCardNo("4524281991111611121221");
			//msg.setIDCardName("李春光");
			String mac = msg.computeMac(merchantPwd);
			msg.setMac(mac);
		
		    srcXml = msg.toXml();
		    System.out.println("srcXml: \n"+srcXml);
			String encryptKey = Toolkit.random(24);
			String pubKey =  GDYILIAN_CERT_PUB_64;
			
			String tmp = Toolkit.signWithMD5(encryptKey, srcXml, pubKey);
			request_text = Toolkit.bytePadLeft(tmp.length()+"", '0', 6) + tmp;
		    redirect(url, response, "UTF-8",request_text);
			    
		} catch (Exception e) {
		
		}
		
	}
	
	public  void redirect(String url, HttpServletResponse response, String charSet,String request_text) {
		String html = generateAutoSubmitForm(url, request_text);
		response.setContentType("text/html;charset=" + charSet);
		response.setCharacterEncoding(charSet);
		try {
			PrintWriter out = response.getWriter();
			response.setStatus(HttpServletResponse.SC_OK);
			out.print(html);
			out.flush();
			out.close();
		} catch (IOException e) {
			
		}
	}

	public static String generateAutoSubmitForm(String actionUrl,
			String  request_text) {
		
	    String method = "POST";
		StringBuilder html = new StringBuilder();
		html.append("<html><head></head><body>")
		.append("<form id=\"pay_form\" name=\"pay_form\" action=\"")
		.append(actionUrl).append("\" method=\"" + method + "\">\n")
		.append("<input type=\"hidden\" name=\"" + request_text + "\">\n")
		.append("</form>\n")
		.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n")
		.append("</body></html>");
		return html.toString();
	}
	
	
	
}
