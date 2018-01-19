package com.payeco.qrorderen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payeco.model.MerchantMessage;
import com.payeco.test.SslConnection;
import com.payeco.test.Transaction;
import com.payeco.test.TransactionClient;
import com.payeco.util.MD5;
import com.payeco.util.Toolkit;


/** 
 * @author fengmuhai
 * @date 2016-10-9 上午10:56:52 
 * @version 1.0  
 */
public class QrcodeRefresh {
	
	private static Logger logger = Logger.getLogger(Transaction.class.getName());
	
	public static void main(String[] args) {
		new QrcodeRefresh().assemble("","123456");
	}
	
	/**
	 * 二维码刷新接口demo，返回格式为xml
	 */
	public String assemble(String orderNo, String merchantNo){
		
		try {
			String url = "http://test.payeco.com:9080/pay/services/ApiPayecoServerRSA";
			//String url = "http://test.payeco.com:9080/pay/services/ApiPayecoServerRSA";
			//String url = "http://test.payeco.com:9080/QR/payeco/ApiPayecoServerRSA";
			//String url = "http://10.123.74.102:8082/payeco/ApiPayecoServerRSA";
			String GDYILIAN_CERT_PUB_64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
			
			String srcXml="";
			String merchantPwd = "123456";//"123456"; 61518FF122B647CE
			String merchantOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String acqSsn = new SimpleDateFormat("HHmmss").format(new Date());
			String transDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//商户号
			//String merchantNo = "1472181543236";	// 302020000058   502040000005  502050000326 
			/*if(null==orderNo || "".equals(orderNo.trim())) {
				orderNo = "702016120700131835";
			}*/
			MerchantMessage msg = new MerchantMessage();
			msg.setVersion("2.1.0");
			msg.setProcCode("3100");
			msg.setProcessCode("310010");
			msg.setMerchantNo(merchantNo);
			msg.setOrderNo(orderNo);	//1462847066331  merchantOrderNo 
			msg.setAcqSsn(acqSsn);
			msg.setTransDatetime(transDatetime);
			String mac = msg.computeMac(merchantPwd);
			msg.setMac(mac);
	
		    srcXml = msg.toXml();
		    
			String encryptKey = Toolkit.random(24);
			String pubKey =  GDYILIAN_CERT_PUB_64;
			String tmp = Toolkit.signWithMD5(encryptKey, srcXml, pubKey);
			
			TransactionClient  tc = new TransactionClient(url);
			String resp = tc.transact(tmp);
			
			resp = Toolkit.verify(encryptKey, resp);
			logger.info("解密后：\n"+resp);  
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
