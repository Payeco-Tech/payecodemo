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
 * 接收异步通知demo
 * @author user
 */
public class AsynServlet extends HttpServlet{

	public static void main(String[] args) {
		//String src = "0210 190011 0.01 000052 502017041050224656 102700000025|channel.wechat 0000 502040000155 052017041000000098 02 SHA256WITHRSA";
		String src = "0210 190011 0.1 20170614111543 111515 502017061409373834 0000 502040000098 14974101131350 02";
		String ext = " WX_NATIVE {\"tradeNo\":\"4001442001201706145660585748\",\"buyerId\":\"\",\"buyerPayAmount\":\"0.10\",\"totalAmount\":\"0.10\",\"discount\":\"\"} 71DFB993673B4556";
		String MAC = new MD5().getMD5ofStr(src+ext);	//getMD5ofStr()方法将src转换成大写
		System.out.println("\nMAC:\n"+MAC);
	}
	
	private static Logger logger = Logger.getLogger(AsynServlet.class.getName());
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		final String CharSet = "UTF-8";
	
		response.setContentType("text/html");
		request.setCharacterEncoding(CharSet);
        response.setCharacterEncoding(CharSet);
		PrintWriter out = response.getWriter();
        String response_text = request.getParameter("response_text");
        
        //转发到我本机
//        try {
//			PlaceOrder.sendAndGet("http://10.123.65.35:8080/payecodemo/servlet/AsynServlet", response_text.getBytes("utf-8"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        
		String xml = "";
		if(response_text != null && !"".equals(response_text)){
			
            //如果解出来的xml是乱码的，请将这个步骤给注释掉，有的服务器程序会自动做UrlDecode
			//String urlText = URLDecoder.decode(response_text, CharSet);
			xml = new String(new sun.misc.BASE64Decoder().decodeBuffer(response_text),CharSet);
			logger.info("异步报文"+xml);
		    String merchantPwd = "123456";
			String src = getValue(xml,"ProcCode")
					+ getString(getValue(xml,"AccountNo"))
					+ getString(getValue(xml,"ProcessCode"))
					+ getString(getValue(xml,"Amount"))
					+ getString(getValue(xml,"TransDatetime"))
					+ getString(getValue(xml,"AcqSsn"))
					+ getString(getValue(xml,"OrderNo"))
					+ getString(getValue(xml,"TransData"))
					+ getString(getValue(xml,"Reference"))
					+ getString(getValue(xml,"RespCode"))
					+ getString(getValue(xml,"TerminalNo"))
					+ getString(getValue(xml,"MerchantNo"))
					+ getString(getValue(xml,"MerchantOrderNo"))
					+ getString(getValue(xml,"OrderState"))
					+ getString(getValue(xml,"Channel"))		//微信支付宝异步通知增加Channel和Detail字段
					+ getString(getValue(xml,"Detail")) + " " + merchantPwd;
			
			System.out.println("src:\n"+src);
			String MAC = new MD5().getMD5ofStr(src);	//getMD5ofStr()方法将src转换成大写
			System.out.println("\nMAC:\n"+MAC);
			String orderState = getValue(xml,"OrderState").trim();
			if(MAC.equals(getValue(xml,"MAC"))){
				logger.info("mac 校验成功");
				//mac verify success
				if("02".equals(orderState)){
					//payment success
					
					
				}else if("05".equals(orderState)){
					//payment fail
					String errorCode = getValue(xml,"RespCode"); 
					
				}else{
					//payment in-process
					String errorCode = getValue(xml,"RespCode");
				}
			}

			//通知易联接收成功
			//notify payeco
			out.print("0000");
			out.flush();
			out.close();
		} 
	}
	private String getValue(String xml, String name){
		if(xml==null || "".equals(xml.trim()) 
				|| name == null || "".equals(name.trim())){
			return "";
		}
		String tag = "<" + name + ">";
		String endTag = "</" + name + ">";
		if(!xml.contains(tag) || !xml.contains(endTag)){
			return "";
		}
		String value = xml.substring(xml.indexOf(tag) + tag.length(), xml.indexOf(endTag));
		if(value != null && !"".equals(value)){
			return value;
		}
		return "";
	}
	
	private String getString(String src) {
	    return (Toolkit.isNullOrEmpty(src) ? "" : (" " + src.trim()));
	}
	
}
