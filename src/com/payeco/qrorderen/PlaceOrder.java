package com.payeco.qrorderen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;


import com.payeco.model.MerchantMessage;
import com.payeco.test.SslConnection;
import com.payeco.util.Toolkit;


/** 
 * @author fengmuhai
 * @date 2016-10-9 上午10:56:52 
 * @version 1.0  
 */
public class PlaceOrder {
	
	public static void main(String[] args) {
		new PlaceOrder().assemble("","","");
		try {
			int a = (int)(Math.random()*10000);
			Thread.sleep(a);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void placeAnOrder() {
		
	}
	
	/**
	 * 扫码下单demo，返回格式为xml
	 */
	public String assemble(String amount, String merchantNo, String merchantPwd){
		
		try {
			String url = "https://dnapay.payeco.com/services/ApiV2ServerRSA";
			//String url = "https://dnaserver.payeco.com/services/ApiV2ServerRSA";
			//String url = "https://songshan.payeco.com/services/ApiV2ServerRSA";
			//String url = "http://test.payeco.com:9080/QR/services/ApiV2ServerRSA";
			//String url = "http://test.payeco.com:9080/PayEcoChannel/services/ApiV2ServerRSA";	//下单地址
			//String url = "http://test.payeco.com:9080/services/ApiV2ServerRSA";	//下单地址
			//String url = "http://test.payeco.com:9080/pay/services/ApiV2ServerRSA";	//下单地址
			String asynAddress = "http://test.payeco.com:9080/payecodemo/servlet/AsynServlet";	//异步地址
			//String asynAddress = "http://54.223.255.108:48091/servlet/AsynServlet";
			//String asynAddress = "http://10.123.65.35:8080/payecodemo/servlet/AsynServlet";

			String GDYILIAN_CERT_PUB_64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
			
			String request_text = "";
			String srcXml="";
			//String amount = "0.01";
			if(Toolkit.isNullOrEmpty(amount)) {
				amount = "0.01";
			}
			String curcode = Toolkit.getCurrency("01");
			//String desc = "Divide order test.";
			String desc = "Test Description.";
			String remark = "";
			
			if(Toolkit.isNullOrEmpty(merchantPwd)) merchantPwd = "23CE7A67BA2B4975";//密钥
			String merchantOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String acqSsn = new SimpleDateFormat("HHmmss").format(new Date());
			String transDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//商户号
			//String merchantNo = "1472181543236";
			//String merchantNo = "1472181649022";
			//String merchantNo = "702040000002";	//测试环境三码合一商户号：702040000002
			//String merchantNo = "502020002898";  //7EABCA2C8FFB4BF5
			//String merchantNo = "502020002975";  //DCB9F4B6F62B4D25
			//String merchantNo = "702040000004";
			//String merchantNo = "502020002845";	//83555B0B5D744798
			//String merchantNo = "502020002899";  //8DEAA6A6B0E14290
			//String merchantNo = "502020002989";	//FC560A2CCA944F60
			if(Toolkit.isNullOrEmpty(merchantNo)) merchantNo = "502040000089";	//23CE7A67BA2B4975 湖北亿咖通科技有限公司(代理商-插件) 
			MerchantMessage msg = new MerchantMessage();
			msg.setVersion("2.1.0");
			msg.setProcCode("0200");
			msg.setProcessCode("190011");
			msg.setMerchantNo(merchantNo);
			msg.setMerchantOrderNo(merchantOrderNo);	//1462847066331  merchantOrderNo
			msg.setAcqSsn(acqSsn);
			msg.setTransDatetime(transDatetime);
			msg.setAmount(amount);
			msg.setCurrency(curcode);
			msg.setDescription(desc);
			msg.setAsynAddress(asynAddress);
			msg.setIpAddress("10.123.1.7");
			//新增订单来源
			msg.setOrderFrom("30");		//30扫码
			//msg.setTransData("|o-nenw7WQKegTvjESDIRZW3BUS3c|wx80b28d18140a5615|");	//市立创新，奉慕海 
			//msg.setTransData("|ohRS8wSK1UIEVsfTm7xxAreqywEo|wx80b28d18140a5615|");	//市立创新,wenhaihao
			//msg.setIpAddress("10.123.1.72");
			//msg.setTransData("|00|00|281622382196661562|");
			//msg.setReference("cartNo|MerTerminalNo");
			//msg.setValidTime("20170524163000");
			//msg.setSdkExtData("{\"geelyUserId\":\"001221\", \"walletUserId\":\"10234\"}");
			String mac = msg.computeMac(merchantPwd);
			msg.setMac(mac);
		
		    srcXml = msg.toXml();
		    System.out.println("srcXml:"+srcXml);
			String encryptKey = Toolkit.random(24);
			String pubKey =  GDYILIAN_CERT_PUB_64;
			
			String tmp = Toolkit.signWithMD5(encryptKey, srcXml, pubKey);
			request_text = Toolkit.bytePadLeft(tmp.length()+"", '0', 6) + tmp;
			String resp = sendAndGet(url, request_text.getBytes("utf-8"));
			resp = new String(new sun.misc.BASE64Decoder().decodeBuffer(resp), "utf-8");
			System.out.println("返回内容：\n"+resp);   
			return resp;
		} catch (Exception e) {
		
		}
		return "";
	}
	
	public static String sendAndGet(String url, byte[] req) throws Exception {
    	HttpURLConnection connect = null;
        if (!url.toLowerCase().startsWith("https:")) {
          URL urlConnect = new URL(url);
          connect = (HttpURLConnection)urlConnect.openConnection();
        } else {
          SslConnection urlConnect = new SslConnection();
          connect = urlConnect.openConnection(url);
        };
        connect.addRequestProperty("content-type", "application/text;charset=UTF-8");
        connect.setReadTimeout(1000 * 40);
        connect.setConnectTimeout(10000);
        connect.setRequestMethod("POST");
        connect.setDoInput(true);
        connect.setDoOutput(true);
        connect.connect();
        BufferedOutputStream out = new BufferedOutputStream(connect.getOutputStream());
        out.write(req);
        out.flush();
        out.close();
        byte[] resp = readResponse(new BufferedInputStream(connect.getInputStream()));
        connect.getInputStream().close();
        connect.disconnect();
        return new String(resp, "utf-8");
	}

	
	
	public static byte[] readResponse(InputStream is) throws Exception {

		BufferedInputStream in = new BufferedInputStream(is);

		LinkedList<Httpbuf> bufList = new LinkedList<Httpbuf>();
		int size = 0;
		byte buf[];
		
		do {
			buf = new byte[128];
			int num = in.read(buf);
			if (num == -1)
				break;
			size += num;
			bufList.add(new Httpbuf(buf, num));
		} while (true);
		
		buf = new byte[size];
		int pos = 0;
		for (ListIterator<Httpbuf> p = bufList.listIterator(); p.hasNext();) {
			
			Httpbuf b = p.next();
			for (int i = 0; i < b.size;) {
				buf[pos] = b.buf[i];
				i++;
				pos++;
			}

		}
		return buf;
	}
	
}
class Httpbuf
{

	public byte buf[];
	public int size;

	public Httpbuf(byte b[], int s)
	{
		buf = b;
		size = s;
	}
}
