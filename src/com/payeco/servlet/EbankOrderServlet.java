package com.payeco.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.payeco.test.Transaction;
import com.payeco.util.MD5;
import com.payeco.util.Toolkit;

/**
 * 接收异步通知demo
 * @author user
 */
public class EbankOrderServlet extends HttpServlet{

	
	private static Logger logger = Logger.getLogger(EbankOrderServlet.class.getName());
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				// 获取当前时间
				String insertTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				java.util.Date dt = new Date();
				String orderNo = "5020" + String.valueOf(dt.getTime());
				String merchantNo = "1489060885289";
				String merchantOrder = merchantNo + orderNo;

				String sql3 = "INSERT INTO DNA_ORDER_NEW (ORDERID, ORDERNO, MERCHANTORDERNO, ORDERTIME, PAYIMMEDIATELY, VALIDTIME, PRODUCTDES, AMOUNT, REMARK, ORDERFROM, ORDERSTATE, CURRENCYTYPE, MOBILENO, ACCOUNTNO, PAYTIME, PAYSTATE, PAYSETTLEDATE, PAYUNIONSEQNO, TERSEQNO, REFUNDTIME, REFUNDSTATE, REFUNDSETTLEDATE, REFUNDUNIONSEQNO, USERNO, MERCHANTNO, TERMINALNO, SYSMERCHANTNO, CONTRACTNO, ACCOUNTBANKNAME, ACCOUNTIDCARDTYPE, ACCOUNTIDCARDNO, ACCOUNTNAME, ACCOUNTADDRESS, PRODUCTUSER, ACCOUNTLEVEL, CUSTOMERIP, ICAINPUT, PRODUCTNO, PRODUCTQUAN, DELLQUOTENO, DELLCUSTOMERNAME, DELLDELIVERADDRESS, DELLRESIDENCEADDRESS, DELLLANDPHONE, PRODUCTADDRESS, RETURNURL, IVRLANGUAGE, USERPHONENUMBER, ACCOUNTNOAES, MOBILENOAES, ACCOUNTIDCARDNOAES, USERPHONENUMBERAES, CUSTOMMSG, SMSCODE, CARDTYPE, TIMEZONE, BINDINGID, LASTUPDATETIME, RISKNO, TRANSCHANNEL, PAYMERSEQNO, REFUNDMERSEQNO, DNATB, CURRENCYRATE)VALUES("
						+ new BigInteger(orderNo) + ", '" + orderNo + "', '" + merchantOrder + "', TO_DATE('" + insertTime
						+ "','YYYY-MM-DD HH24:MI:SS'), 0, TO_DATE('" + insertTime
						+ "','YYYY-MM-DD HH24:MI:SS'), 'Test Decription', 0.10, '', 'OrderFrom.14', 'OrderState.2', 'CurrencyType.CNY', '6E4533344339C998FC96E9860261F569', '6E4533344339C998FC96E9860261F569', TO_DATE('"
						+ insertTime
						+ "','YYYY-MM-DD HH24:MI:SS'), 'SystemError.0000', '', '', NULL, NULL, 'SystemError.T451', NULL, NULL, '0309195521', '872754', '02023625', '"
						+ merchantNo
						+ "', '3020B-20150604003', '农业银行', 'IdcardType.01', '', '', '', '', NULL, '10.123.65.9', '', '', 1, '', '', NULL, 'http://10.123.26.23:8080/services/ApiV2ServerRSA', '', '', 'http://10.123.26.23:8080/services/ApiV2ServerRSA,reference,false,http://10.123.26.23:8080/services/ApiV2ServerRSA', '00', '', '6E4533344339C998FC96E9860261F569', '6E4533344339C998FC96E9860261F569', '', '', '', '', 'AccountType.2', '14:37:34.646|14:37:34.688|0|0|0|0|0', '', TO_DATE('"
						+ insertTime + "','YYYY-MM-DD HH24:MI:SS'), NULL, 'TRANSCHANNEL.CYBER_BANK', '" + merchantOrder
						+ "', NULL, 'dna_order_new', 100.00)";
				
	
	}
	
	
}
