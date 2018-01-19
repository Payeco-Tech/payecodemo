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

public class WxJSAPIPlaceOrder {
	public static void main(String[] args) {
		new WxJSAPIPlaceOrder().assemble("ohRS8wbm5MU835ZFUKa3NF7CJGNA", "","","");
	}
	
	public void placeAnOrder() {
		
	}
	
	/**
	 * 扫码下单demo，返回格式为xml
	 */
	public String assemble(String openid, String amount, String merchantNo, String merchantPwd){
		
		try {
			//String url = "http://test.payeco.com:9080/QR/services/ApiV2ServerRSA";
			//String url = "http://test.payeco.com:9080/PayEcoChannel/services/ApiV2ServerRSA";	//下单地址
			//String url = "http://test.payeco.com:9080/pay/services/ApiV2ServerRSA";	//下单地址
			String url = "https://web.payeco.com/services/ApiV2ServerRSA";	//下单地址
			//String url = "https://dnaserver.payeco.com/services/ApiV2ServerRSA";	//下单地址
			String asynAddress = "http://test.payeco.com:9080/payecodemo/servlet/AsynServlet";	//异步地址
			//String asynAddress = "http://54.223.255.108:48091/servlet/AsynServlet";
			//String asynAddress = "http://10.123.65.35:8080/payecodemo/servlet/AsynServlet";
			//String url = "https://192.168.161.28:8443/services/ApiV2ServerRSA";	//PM下单地址

			String GDYILIAN_CERT_PUB_64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
			
			String request_text = "";
			String srcXml="";
			//String amount = "0.01";
			if(Toolkit.isNullOrEmpty(amount)) {
				amount = "0.01";
			}
			String curcode = Toolkit.getCurrency("01");
			String desc = "Test Description";
			String remark = "";
			
			if(Toolkit.isNullOrEmpty(merchantPwd)) merchantPwd = "7EABCA2C8FFB4BF5";//密钥
			String merchantOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String acqSsn = new SimpleDateFormat("HHmmss").format(new Date());
			String transDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//商户号
			//String merchantNo = "1472181543236";
			//String merchantNo = "1472181649022";
			//String merchantNo = "702040000002";
			if(Toolkit.isNullOrEmpty(merchantNo)) merchantNo = "502020002898";
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
			//新增订单来源
			msg.setOrderFrom("34");		//30扫码
			msg.setIpAddress("10.123.1.72");
			//msg.setTransData("|"+openid+"|wx9614bff351a28228|");	//民生，易联国际公众号
			msg.setTransData("|"+openid+"|wx80b28d18140a5615|");	//浦发，市立创新：wx80b28d18140a5615
			//msg.setSdkExtData("{\"geelyUserId\":\"001221\", \"walletUserId\":\"10234\"}");
			String mac = msg.computeMac(merchantPwd);
			msg.setMac(mac);
		
		    srcXml = msg.toXml();
		    
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
