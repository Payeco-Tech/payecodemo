package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payeco.model.MerchantMessage;
import com.payeco.qrorderen.AliqrcodePlaceOrder;
import com.payeco.qrorderen.BeecloudWxNativePlaceOrder;
import com.payeco.qrorderen.PlaceOrder;
import com.payeco.qrorderen.WxNativePlaceOrder;
import com.payeco.test.TransactionClient;
import com.payeco.util.JsonUtil;
import com.payeco.util.MD5;
import com.payeco.util.Toolkit;

public class UnionQrcodeOrderSyncServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UnionQrcodeOrderSyncServlet.class.getName());
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	     doPost(req, resp);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String srcXml = new String(req.getParameter("srcXml").getBytes("ISO-8859-1"),"UTF-8");;
		String url = req.getParameter("url");
		
		String Amount = Toolkit.getValue(srcXml, "Amount");
		String MerchantNo = Toolkit.getValue(srcXml, "MerchantNo");
		String MerchantPwd = Toolkit.getValue(srcXml, "MerchantPwd");
		String MerchantOrderNo = Toolkit.getValue(srcXml, "MerchantOrderNo");
		String Description = Toolkit.getValue(srcXml, "Description");
		String TransDatetime = Toolkit.getValue(srcXml, "TransDatetime");
		String PayInfo = Toolkit.getValue(srcXml, "PayInfo");
		String Reference = Toolkit.getValue(srcXml, "Reference");
		String DisneyFrom = Toolkit.getValue(srcXml, "DisneyFrom");
		
		String xml = assemble(url, Amount, MerchantNo, MerchantPwd, 
			     MerchantOrderNo, Description, TransDatetime, 
				 PayInfo, Reference,  DisneyFrom);
		
		final String CharSet = "UTF-8";
		resp.setContentType("text/html");
		resp.setCharacterEncoding(CharSet);
		resp.setCharacterEncoding(CharSet);
		PrintWriter out = resp.getWriter();
		out.println("����ͬ�����ؽ����<br/>");
		out.println("<textarea rows=\"40\" cols=\"100\">" + xml + "</textarea>");

//	    <Amount>0.10</Amount>
//	    <MerchantNo>502020002683</MerchantNo>
//	    <MerchantPwd>110723</MerchantPwd>
//	    <MerchantOrderNo>20170622231904</MerchantOrderNo>
//	    <Description>Test Description</Description>
//	    <TransDatetime>20170623110723</TransDatetime>
//	    <PayInfo>{"PayReqType":"03","PayTime":"20170623110723","PayQrNo":"","PaySettleKey":"4365436","PaySettleDate":"0623","PayVoucherNum":"","PayPayerInfo":"","PayComInfo":"79348679435341234","TerminalNo":"1235443623144567","PayState":"0000","PayRemark":"�˻��ɹ�","AdjustAmount":"0.05","RefundUnionSeqNo":"14123445335674571123","RefundSettleDate":"20170622123411"}</PayInfo>
//	    <Reference>2103451|</Reference>
//	    <DisneyFrom>ODVUNIONPAY</DisneyFrom>
		
	}
	
	/**
	 * ͬ�������ӿڣ��������󶩵���Ϣ��֧��/����״̬����̨�µ���ֱ�Ӹ��¶�����Ϣ������
	 */
	public String assemble(String url, String Amount, String MerchantNo, String MerchantPwd, 
			String MerchantOrderNo, String Description, String TransDatetime, 
			String PayInfo, String Reference, String DisneyFrom){
		
		try {
			//String url = "http://10.123.65.35:8082/services/ApiV2ServerRSA";
			//String url = "http://test.payeco.com:9080/pay/services/ApiV2ServerRSA";	//���Ե�ַ
			//String url = "http://10.123.107.236:8081/pay/services/ApiV2ServerRSA";	//disney�ڲ����Ե�ַ
			//String url = "https://testshdr.payeco.com:8443/services/ApiV2ServerRSA";	//disney�ⲿ���Ե�ַ
		   // String url = "https://shdr.payeco.com/services/ApiV2ServerRSA"; 	//disney �������Ե�ַ
		    //String url = "https://192.168.156.6:8443/services/ApiV2ServerRSA"; 	//disney PM���Ե�ַ
			//String url = "https://192.168.161.28:8443/services/ApiV2ServerRSA";	//dna PM���Ե�ַ
			String asynAddress = "http://test.payeco.com:9080/payecodemo/servlet/AsynServlet";	//�����첽֪ͨ��ַ
			String GDYILIAN_CERT_PUB_64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
			
			String request_text = "";
			String srcXml="";
			String amount = Amount;
			String curcode = Toolkit.getCurrency("01");
			String desc = Description;
			String remark = "";
			
			String merchantPwd = MerchantPwd;//��Կ
			//String merchantOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String acqSsn = new SimpleDateFormat("HHmmss").format(new Date());
			String transDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//�����̻��ţ�002050000031(΢��ɨ��֧��) ,502050000073(֧����ɨ��֧��)
			//�����̻��ţ�702020000048�����ں�֧������502050000073��֧��������֧����
			//String merchantNo = "702050000045";
			//String merchantNo = "502050000073";
			//String merchantNo = "702050000044";
			//String merchantNo = "702020000048";
			//String merchantNo = "302020000027";	//Disney ���ԣ���Ʊ��
			String merchantNo = MerchantNo;	//Disney PM�̻��� 21D55699FF554EF9
			//String merchantNo = "502050002415";	//����̨�������  EAABDEC50F144414
			
			MerchantMessage msg = new MerchantMessage();
			msg.setVersion("2.1.0");
			msg.setProcCode("0200");
			msg.setProcessCode("310011");
			msg.setMerchantNo(merchantNo);
			msg.setMerchantOrderNo(MerchantOrderNo);	//merchantOrderNo
			msg.setAcqSsn(acqSsn);
			msg.setTransDatetime(TransDatetime);
			msg.setAmount(amount);
			msg.setCurrency(curcode);
			msg.setDescription(desc);
			msg.setAsynAddress(asynAddress);
			//����������Դ
			msg.setOrderFrom("40");		//30�����һɨ��,32΢��ɨ��,33֧����ɨ��,34΢�Ź��ںţ�39֧��������֧��,40ͬ������
			msg.setDisneyFrom(DisneyFrom); //����Disney�������ͣ� ODVUNIONPAY ODVALIPAY
			msg.setReference(Reference);	//С�Ƴ����
			
			Map<String, Object> payInfo = new LinkedHashMap<String, Object>();
			String payDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String settleDate = new SimpleDateFormat("MMdd").format(new Date());
			payInfo.put("PayReqType", "03");		//��������:01:���� 02:�˿� 03:�˻�
			payInfo.put("PayTime", payDateTime);	//�������ض���֧��ʱ�䣨yyyyMMddHHmmss��
			payInfo.put("PayQrNo", "");				//�������ص�C2B��
			payInfo.put("PaySettleKey", "4365436");		//�������ص���������
			payInfo.put("PaySettleDate", settleDate);	//�������صĸ���ƾ֤�� 
			payInfo.put("PayVoucherNum", "");		//�������صĸ���ƾ֤�� 
			payInfo.put("PayPayerInfo", "");		//�������صĸ����Ϣ
			payInfo.put("PayComInfo", "79348679435341234");		//�������ص����ж�����ˮ��Ϣ
			payInfo.put("TerminalNo", "1235443623144567");		//�������ط����ն˺�
			payInfo.put("PayState", "0000");					//֧�����:0000��ʾ�ɹ�
			payInfo.put("PayRemark", "�˻��ɹ�");				//֧�������ע
			payInfo.put("AdjustAmount", "0.05");		//���˽��
			payInfo.put("RefundUnionSeqNo", "14123445335674571123");		//�����˿���ˮ
			payInfo.put("RefundSettleDate", "20170622123411");		//�˿���������
			String info = JsonUtil.map2JsonStr(payInfo);		
			msg.setPayInfo(PayInfo);
			
			//msg.setTransData("|00|00|285214336788020767|");	//��|�ָ�ֱ�Ϊ��|openid|wxappid|authCode|(00��ʾռλ�������ֶ�Ϊ��)
			//msg.setTransData("|openid|");	//΢�Ź��ں�֧������޸ĳɶ�Ӧ��openid
			//msg.setTransData("|ohRS8wbm5MU835ZFUKa3NF7CJGNA|");			
			//msg.setTransData("|ohRS8wX7qVI4MFM2l1Aocfbq_w3A|");
			//msg.setSdkExtData("{\"geelyUserId\":\"001221\", \"walletUserId\":\"10234\"}");
			
			
			String mac = msg.computeMac(merchantPwd);
			msg.setMac(mac);
		
		    srcXml = msg.toXml();
		    logger.info("srcXml:\n"+srcXml);
		    
			String encryptKey = Toolkit.random(24);
			String pubKey =  GDYILIAN_CERT_PUB_64;
			
			String tmp = Toolkit.signWithMD5(encryptKey, srcXml, pubKey);
			TransactionClient  tc = new TransactionClient(url);
			String result = tc.transact(tmp);
			logger.info(result);
			
			String xml = Toolkit.verify(encryptKey,result);
			logger.info(xml);
			String src = Toolkit.getValue(xml,"ProcCode")
					+ Toolkit.getString(Toolkit.getValue(xml,"AccountNo"))
					+ Toolkit.getString(Toolkit.getValue(xml,"ProcessCode"))
					+ Toolkit.getString(Toolkit.getValue(xml,"Amount"))
					+ Toolkit.getString(Toolkit.getValue(xml,"TransDatetime"))
					+ Toolkit.getString(Toolkit.getValue(xml,"AcqSsn"))
					+ Toolkit.getString(Toolkit.getValue(xml,"OrderNo"))
					+ Toolkit.getString(Toolkit.getValue(xml,"TransData"))
					+ Toolkit.getString(Toolkit.getValue(xml,"Reference"))
					+ Toolkit.getString(Toolkit.getValue(xml,"RespCode"))
					+ Toolkit.getString(Toolkit.getValue(xml,"TerminalNo"))
					+ Toolkit.getString(Toolkit.getValue(xml,"MerchantNo"))
					+ Toolkit.getString(Toolkit.getValue(xml,"MerchantOrderNo"))
					+ Toolkit.getString(Toolkit.getValue(xml,"OrderState")) + " " + "123456";
		
			String MAC = new MD5().getMD5ofStr(src);
			String orderState = Toolkit.getValue(xml,"OrderState").trim();
			if(MAC.equals(Toolkit.getValue(xml,"MAC"))){
				logger.info("OrderState="+orderState);
			}
			return xml;
		} catch (Exception e) {
			logger.info("assemble(): ", e);
		}
		return "";
	}

}
