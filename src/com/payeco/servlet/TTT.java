package com.payeco.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

//import org.apache.commons.lang.time.DateUtils;

//import dnapay.common.AES;

public class TTT {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	Connection conn = null;

	// 内部测试环境
	// String url = "jdbc:db2://10.123.1.12:50000/newdna";
	// 外部测试环境
	String url = "jdbc:db2://10.123.1.14:50000/dna:currentSchema=DB2INST1;";

	// 商户名称
	String MERCHANTNAME = "测试" + new Date().getMinutes();

	// 商户简称
	String SIMPLEMERNAME = MERCHANTNAME;

	// 0 事后验证
	// 1 事前验证
	String limitTimeFlag = "1";

	// merchantNetworkModeId.4001 即时收款
	// merchantNetworkModeId.4002 非即时收款
	String networkMode = "merchantNetworkModeId.4001";

	// OffSeaType=OffSeaType.Domestic 境内
	// OffSeaType=OffSeaType.Abroad 境外
	// String offSeaType = "All"; //包括境内与境外
	String offSeaType = "OffSeaType.Domestic"; // 境内
	// String offSeaType = "OffSeaType.Abroad"; //境外

	// 修改行业类型，列表如下
	// IndustryType.16 Apple_iTunes
	// IndustryType.17 微支付
	// IndustryType.18 实名充值
	// IndustryType.19 盒子支付
	// IndustryType.20 国际旅行社
	// IndustryType.21 话费充值(外呼)
	// IndustryType.22 信用卡还款
	// IndustryType.23 视频直播
	// IndustryType.24 金融理财
	// IndustryType.25 通用插件
	// IndustryType.26 线下交易
	// IndustryType.27 跨境支付
	// IndustryType.28 插件游戏充值
	// IndustryType.1 航空售票类
	// IndustryType.2 铁路售票类
	// IndustryType.3 旅游类
	// IndustryType.4 彩票类
	// IndustryType.5 礼品类
	// IndustryType.6 高尔夫类
	// IndustryType.7 数字电视缴费类
	// IndustryType.8 保留
	// IndustryType.9 缴费类
	// IndustryType.10 网上商城
	// IndustryType.11 渠道广告收费
	// IndustryType.12 保险类
	// IndustryType.13 充值类
	// IndustryType.14 手机商城
	// IndustryType.15 电影及普通票务
	String IndustryType = "IndustryType.15";

	public static void main(String[] args) throws SQLException, IOException {

		// System.out.println(new
		// // ConnectDB2().getIDByCardNo("6225882015431103"));
		// new ConnectDB2().changeCardLevel("6228480310499222118", "123", "123",
		// "user.0000001", "123", "123", "123", "");
		// new ConnectDB2().deleteCardAndPhoneInfo("622202360207605821");
		// new ConnectDB2().deleteCardAndPhoneInfo2("13934967036");
		// new
		// ConnectDB2().getOrderStateAndRespcodeByOrderId("0120120911091936");
		// new ConnectDB2().getSmsCode("6227000012435149");
		// new ConnectDB2().getSmsContentByOrderNo("202015080100105950");
		// new ConnectDB2().deleteCardAndPhoneInfo2("18102821128");
		// new ConnectDB2().updateDiction( "dna.task.FlushesTask",new
		// Date().getTime()+",true");
		// String orderState =
		// "ProcCode=0210&AccountNum=15220ED345EDE10F4F8E39FC44A995F02AB6654538526B7A75ED6A401406B94DDD0FC3AE16246A28AC3A3FD0C888E829&ProcessCode=190000&Amount=000000000001&CurCode=01&TransDatetime=0826115823&AcqSsn=115823&Ltime=175839&Ldate=0826&Reference=20140826115823&ReturnAddress=08http://10.123.18.3/returnUrl2.php&RespCode=X913&Remark=IVR2：外呼失败（用户未接听）&TerminalNo=05023141&MerchantNo=02202020000244&OrderNo=01202014082600094554&OrderState=05&Description=订单测试&ValidTime=20140902175840&OrderType=00&TransData=莫|440106196911211822||01|农业银行|||03570597810|广东广州||1
		// ! 06450||&Mac=E1B584191EDBC4B0A4C63F5C10F01B62";
		// new ConnectDB2()
		// .deleteCardAndPhoneInfo("6222023602012354906|6222023602012356001|6225882013417864|6225882013417865|6225882013417864|6222023602012356000|6222023602012356000|6222023602012356000|6228480083248380419|6228480083248380419|6228480083248380419|6228480083248380419|6228480083248380419|62220202000030203|62220202000030203|62220202000030203|62220202000030203|6222023602012355405|6222023602012355405|6222023602012355405|6222023602012355405|6222023602012355405|6222023602012355405|6222023602012355405|6222023602012356001|6222023602012356001|62220202000030001|62220202000030001|62220202000410001|62220202000030001|62220202000030001|6228480083248380518|6228480083248380518|6228480083248380518|6228480083248380518|6228480083248380518|6228480083248383418|6228480083248383418|6228480083248383418|6222023602012356071|6228480083248380519|6222023602012356071|6228480083248380519|6228480083248380519|6222023602012356071|6222023602012356071|6228480083248380519|6222023602012356301|6222023602012356301|6222023602012356301|6228480082236012650|6227003324126012230|6228480082236012650|6227003324126012230|6227003324126012230|6228480082236012651|6228480082236012652|6228480082236012651|6228480082236012651|6228480082236012651|6228480082236012652|6228480082236012653|6228480082236012651|6228480082236012651|6228480082236012653|6228480082236012652|6228480082236012651|");
		// new
		// ConnectDB2().getSmsCodeByMerchantOrder("60201000000520140828170003");
		// new
		// ConnectDB2().getOrderStateAndRespcodeByReference("20140903072055");
		// System.out.println(new
		// new ConnectDB2().getCardAmtByAccountNo("6222023602012354906");
		// System.out.println(System.currentTimeMillis());
		// System.out.println(toFixdLengthString(System.currentTimeMillis(),16));
		new TTT().genOrder();
		//new TTT().genMerchant();
		// new ConnectDB2().getItunesFastPayReturnFlag("18102821128",
		// "202015080600106141");
		// new ConnectDB2().setBossVerify("1", "1", "1", "1");
		// new ConnectDB2().getTestCaseByTestRule("5652");

	}

	public Connection conn() {
		Connection conn = null;
		String driver = "com.ibm.db2.jcc.DB2Driver";

		String userName = "db2scadm";
		String passWord = "farmer=celery!slid";

		// String url = "jdbc:db2://10.123.26.13:50000/NEWDNA";
		// String userName = "db2scadm";
		// String passWord = "moon!circle!oncemore";

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, userName, passWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public String genOrder() throws SQLException {
		Statement st = this.conn().createStatement();
		ResultSet rs = null;
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
		System.out.println(sql3);
		st.executeUpdate(sql3);

//		String sql4 = "INSERT INTO DNA_TRANSACTION(TRANSACTIONID, ORDERID, ORDERNO, SYSMERCHANTNO, MERCHANTNO, USERNO, TERMINALNO, MERTERMINALNO, SEQUENCENO, MERSEQUENCENO, SETTLEMENTDATE, TRANSTYPE, TRANSSTATE, TRANSCODE, BEGINTIME, TRANSTIME, ENDTIME, AMOUNT, REASON, REFERENCE, REMARK, CHECKSTATE, CHECKDATE, LIQUIDATIONSTATE, LIQUIDATIONDATE, COMMISIONFEE, SYSCOMMISIONFEE, TELFEE, SMSFEE, SERVICEFEE, LASTUPDATETIME)VALUES("+new BigInteger(orderNo)+", "+new BigInteger(orderNo)+", '"
//				+ orderNo + "', '" + merchantNo
//				+ "', '872754', '1214152609', '', NULL, '', NULL, '', 'TransType.pay', 'TransState.done', 'SystemError.0000', TO_DATE('"
//				+ insertTime + "','YYYY-MM-DD HH24:MI:SS'), TO_DATE('" + insertTime
//				+ "','YYYY-MM-DD HH24:MI:SS'), TO_DATE('" + insertTime
//				+ "','YYYY-MM-DD HH24:MI:SS'), 0.10, '交易成功', NULL, '交易成功', NULL, NULL, NULL, NULL, 0.00, 0.00, 0.00, 0.00, 0.00, TO_DATE('"
//				+ insertTime + "','YYYY-MM-DD HH24:MI:SS'))";
//		st.executeUpdate(sql4);

		return orderNo;
	}

	public String genMerchant() throws SQLException {
		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		try {

			long result = System.currentTimeMillis();
			String sysMerchantNo = String.valueOf(result);
			String merchantNo = toFixdLengthString(result, 15);
			String account = String.valueOf(result).substring(2);
			String account2 = String.valueOf(Long.valueOf(account) + 1);
			String account3 = String.valueOf(Long.valueOf(account2) + 1);
			String account4 = String.valueOf(Long.valueOf(account3) + 1);
			System.out.println(sysMerchantNo);
			System.out.println(merchantNo);
			System.out.println(account);
			System.out.println(account2);

			// 插入商户信息表
			String sql1 = "INSERT INTO dna_merchant (MERCHANTID,MERCHANTNO,SYSMERCHANTNO,MERCHANTNAME,SIMPLEMERNAME,"
					+ "EMAIL,MOBILE,TERMINALNO,MERCHANTTYPEID,STATEID,LINKMAN1,LINKMAN2,TELEPHONE1,TELEPHONE2,FAX,"
					+ "SERVICETEL1,SERVICETEL2,SERVICETEL3,NETWORKADDRESS,POSTALCODE,ADDRESS,AREA,LICENCENO, CORPORATION,"
					+ "ORGANIZATIONCODE,LICENCEEXPIRINGDATE,WORKINGITEM,TAXNO,BANKACCOUNTNAME,BANKNAME,BANKACCOUNT,"
					+ "APPLYPEOPLE, APPLYLOGINDATE,SYSLOGINDATE,APPLYLOGOUTDATE,SYSLOGOUTDATE,UNIONPAYAPPROVEPEOPLE,"
					+ "UNIONPAYAPPROVEDATE,UNIONPAYLOGOUTDATE, OPERATOR,OPERATEDATE,SYSAPPROVEPEOPLE,SYSAPPROVEDATE,"
					+ "REMARK,APPROVESTATE,APPROVEREMARK,NETWORKMODEID, MERCHANTLEVELID,LICENCESCANFILEPATH,COUNTRYID,"
					+ "PROVINCEID,CITYID,SMSNOTIFYFLAG,RECALLFLAG,WEBNEWORDERFLAG, WAPNEWORDERFLAG,IVRNEWORDERFLAG,"
					+ "SMSNEWORDERFLAG,THIRDPARTNOFLAG,MERCHANTPROPERTYID,MK,IDCARD,UNIONID, MERCHANTMK,CUSTOMERSMSNOTIFYFLAG,"
					+ "CUSTOMERRECALLFLAG,REFUNDFLAG,THIRDHTTPFLAG,THIRDSOCKETFLAG,IP,RETURNENCODING, RETURNURL,USERSTATEID,"
					+ "BANKCODE,BANKADDRESS,MERCHANTMKAES,SMSMOBILE,RECALLTIMES,SETTLEMENTDATE,MCC,FLOWTYPE, CURRENCYTYPE,"
					+ "BELONGMERCHANTNO,CHINAUMSMERCHANTNO,CHINAUMSTERMINALNO,ORGANIZATIONNO) VALUES " + "(default,'"
					+ merchantNo + "','" + sysMerchantNo + "','" + MERCHANTNAME + "','" + SIMPLEMERNAME
					+ "','02022154653@qq.com','" + account + "',"
					+ "'06014635', 'merchantTypeId.0120','UserState.Active','yan','','" + account
					+ "','','','','','','','','',null,"
					+ "'dfsadf','', '',null,'','','teste','ABC','622848008451258254','test',null,(current timestamp),null,null, "
					+ "'test',(current timestamp),null,'0502161700',(current timestamp),'0502161700', (current timestamp),'',null,"
					+ "'','" + networkMode
					+ "','merchantLevelId.2','', 'countryId.CN','','',1,1,1,1,1,1,1,'merchantPropertyId.1',"
					+ "'B7D004098F6AD830','','merchantUnionId.7020', '123456',1,1,1,1,1,null,'UTF-8','',null,'','',"
					+ "'7BEE4819182617105A555149DE37211E','',2,null,'','0', 'CurrencyType.CNY','0','','',null)";
			System.out.println("SQL1:" + sql1);
			st.executeUpdate(sql1);
			Thread.sleep(1000);

			// 插入商店
			String merchantid = "";
			String merchantTypeId = "";
			String sql2 = "select merchantid,merchantTypeId from dna_merchant where sysmerchantno='" + sysMerchantNo
					+ "'";
			rs = st.executeQuery(sql2);
			System.out.println("SQL2:" + sql2);
			if (rs.next()) {
				merchantid = rs.getString(1);
				merchantTypeId = rs.getString(2);
			}

			String sql3 = "INSERT INTO Dna_Shop (SHOPID,MERCHANTID,SHOPNO,SHOPNAME,MOBILENO,SYSMERCHANTNO,STATEID,SHOPLEVEL,SHOPTYPE,LOGINDATE,LOGOUTDATE,PRINCIPAL, "
					+ "ADDRESS,AREA,LINKMAN,TELEPHONE1,TELEPHONE2,EMAIL,FAX,OPERATOR,OPERATEDATE,REMARK) "
					+ "VALUES (default," + merchantid + ", TO_CHAR((current timestamp), 'YYYYMMDDHH24MISS'),'"
					+ MERCHANTNAME + "'," + "'" + account + "','" + sysMerchantNo
					+ "','UserState.Active','ShopLevel.1','ShopType.1', " + "(current timestamp),null,'','','','yan','"
					+ account + "','','02022154653@qq.com','','" + account + "',(current timestamp),'')";
			System.out.println("SQL3:" + sql3);
			st.executeUpdate(sql3);
			Thread.sleep(1000);

			// 插入用户表
			String sql4 = "INSERT INTO dna_user (USERID,USERGROUPID,USERNO,LOGINNAME,PASSWORD,USERNAME,EMAIL,USERTYPE,USERSTATE,LOGINDATE,LOGOUTDATE, "
					+ "IDENTITYCARD,SEX,BIRTHDAY,TELEPHONE,MOBILE,OPERATOR,OPERATEDATE,REMARK,POSITIONID,DEGREEID,DEPARTMENTID, "
					+ "CHECKCODEFLAG,USERSTATEID,USERTYPEID,PASSWORDEXPIREDDATE,PASSERRORTIMES,CASEQNO) "
					+ "VALUES (default,2,TO_CHAR((current timestamp), 'MMDDHH24MISS'),'" + account
					+ "','A906449D5769FA7361D7ECC6AA3F6D28','" + account + "','02022154653@qq.com', "
					+ "'UserType.Merchant','UserState.Active',(current timestamp),null,null,'Sex.Male',null,'" + account
					+ "', " + "'" + account + "','" + account
					+ "',(current timestamp),'','Position.7014','Degree.8001','Department.9001', "
					+ "0,null,null,(current timestamp),null,null)";
			System.out.println("SQL4:" + sql4);
			st.executeUpdate(sql4);
			Thread.sleep(1000);

			// 插入商户用户表
			String userid = "";
			String sql5 = "select userid from dna_user where LOGINNAME='" + account + "'";
			rs = st.executeQuery(sql5);
			System.out.println("SQL5:" + sql5);
			if (rs.next()) {
				userid = rs.getString(1);
			}

			String SHOPID = "";
			String sql6 = "select SHOPID from Dna_Shop where SYSMERCHANTNO='" + sysMerchantNo + "'";
			rs = st.executeQuery(sql6);
			System.out.println("SQL6:" + sql6);
			if (rs.next()) {
				SHOPID = rs.getString(1);
			}

			String sql7 = "INSERT INTO Dna_Merchantuser (USERID,SHOPID) VALUES (" + userid + "," + SHOPID + ")";
			System.out.println("SQL7:" + sql7);
			st.executeUpdate(sql7);
			Thread.sleep(1000);

			// 插入合同
			// 合同编号组成规则=银联商户编号前4+(一级:"A-";二级:"B-")+(dna_merchant字段merchantTypeId数字部分)+11位随机数
			String CONTRACTNO = "";
			CONTRACTNO = merchantNo.substring(0, 4) + "B-" + merchantTypeId.split("\\.")[1] + account;
			System.out.println(CONTRACTNO);

			String sql8 = "INSERT INTO dna_contract (CONTRACTID,MERCHANTID,CONTRACTNO,MERCHANTNO,FIRST,SECOND,"
					+ "THIRD,FOURTH,TERMINALQUANTITY,EACHDEPOSIT,TOTALDEPOSIT, MONTHEXPENSE,MAINTENANCEEXPENSE,"
					+ "DEVELOPPEOPLENAME,DEVELOPPEOPLENO,CONTRACTSTATEID,SIGNUPDATE,EXPIRINGDATE,OPERATOR,OPERATEDATE, "
					+ "SYSAPPROVEPEOPLE,SYSAPPROVEDATE,REMARK,APPROVEREMARK,APPROVESTATE,CONTRACTTYPEID,CONTRACTSCANFILEPATH,"
					+ "INDUSTRYTYPEID,LIQUIDATIONTYPE, LIQUIDATIONAMOUNT,MONTHBACKAMOUNT,MONTHBACKRATE,COMMISIONTYPEID,"
					+ "LEASTCOMMISION,COMMISIONRATE,COMMISIONFEE,SMSEXPENSE,TELEXPENSERATE, SERVEREXPENSE,LIMITAMT,"
					+ "LIMITAMTPERDAY,LIMITAMTNEWUSER,LIMITAMTBLACKUSER,LIMITCOUNTPERDAY,LIMITAMTPERMONTH,LIMITAMTFIRSTMONTH,"
					+ "LIMITAMTVIPFIRSTMONTH, LIMITCOUNTFIRSTMONTH,LIMITCOUNTPERMONTH,LIMITTIMEFLAG,LIMITTIME,LIMITSELFFLAG,"
					+ "LIMITAMTSELF,LIMITNONBINDFLAG,MERACCOUNTTYPE,REFUNDLIMITDAYS, ADJUSTFEE,ADJUSTTIPS,ALLOWFLAG,AMTBLACKUSER,"
					+ "AMTFIRSTMONTH,AMTNEWUSER,AMTPERMONTH,AMTSELFTRANS,AMTVIPFIRSTMONTH,BACKTIPS,CALLEXPENSE,CHECKFLAG, CHECKTIME,"
					+ "CONTRACTSTATE,COUNTFIRSTMONTH,COUNTPERMONTH,DAYAMOUNT,LIQUIDATIONFEE,ONCEAMOUNT,SERVERRATE,SUPPNONBIND,"
					+ "TELEXPENSE,TIMESPERCARD, UNIONTIPS,APPROVETYPE,ISSUPPORTDEBIT,ISSUPPORTCREDIT,COMMISIONRATE_PUB,SELFTRANSONLY,"
					+ "LIMITAMTVIPPERMONTH,SUPORTBANKS,RISKASSURER,AGENCYUNIONFEE, FASTPAYLEASTCOMMISION,LIMITAMTBINDINGPERDAY,"
					+ "LIMITAMTBINDINGPERMONTH,ISMONTHLYFEE,SETTLEMENTTYPE,IVREXPENSERATE,OffSeaType,limitCountVipPerDay,limitCountVipPerMonth) VALUES (default,"
					+ merchantid + ",'" + CONTRACTNO + "'," + "'" + sysMerchantNo + "','广州易联7','" + MERCHANTNAME
					+ "','','','',0.00,0.00,0.00,0.00,'abc','0807184906', 'UserState.Active',"
					+ "(current timestamp),(current timestamp+3 years),'0520173342',(current timestamp+1 years),'0520173342',"
					+ "(current timestamp+1 years), '','','APPROVED','ContractType.2201',null,'" + IndustryType
					+ "','LiquidationType.1',"
					+ "25.00,0.00,'001=0,0.0|', 'CommisionType.2101',0.00,'',0.00,0.00,'0.00,0.00',0.00,1000000.00,1000000.00,"
					+ "1000000.00,1000000.00,1000000,1000000.00,1000000.00,1000000.00,1000000, 1000000,1,'00:00:00-23:59:59',1,1000000.00,"
					+ "1,'MerAccountType.2',0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, 0.00,"
					+ "null,null,null,null,null,null,null,0,1,null,0,1000000.00,'','RiskAssurer.MERCHANT',null,0.00,1000000.00,1000000.00,0,"
					+ "'SettlementType.01',null,'OffSeaType.Domestic','100000','100000')";

			// 插入合同2
			CONTRACTNO = merchantNo.substring(0, 4) + "B-" + merchantTypeId.split("\\.")[1] + account2;
			System.out.println(CONTRACTNO);
			String sql9 = "INSERT INTO dna_contract (CONTRACTID,MERCHANTID,CONTRACTNO,MERCHANTNO,FIRST,SECOND,THIRD,FOURTH,"
					+ "TERMINALQUANTITY,EACHDEPOSIT,TOTALDEPOSIT, MONTHEXPENSE,MAINTENANCEEXPENSE,DEVELOPPEOPLENAME,DEVELOPPEOPLENO,"
					+ "CONTRACTSTATEID,SIGNUPDATE,EXPIRINGDATE,OPERATOR,OPERATEDATE, SYSAPPROVEPEOPLE,SYSAPPROVEDATE,REMARK,APPROVEREMARK,"
					+ "APPROVESTATE,CONTRACTTYPEID,CONTRACTSCANFILEPATH,INDUSTRYTYPEID,LIQUIDATIONTYPE, LIQUIDATIONAMOUNT,MONTHBACKAMOUNT,"
					+ "MONTHBACKRATE,COMMISIONTYPEID,LEASTCOMMISION,COMMISIONRATE,COMMISIONFEE,SMSEXPENSE,TELEXPENSERATE, SERVEREXPENSE,"
					+ "LIMITAMT,LIMITAMTPERDAY,LIMITAMTNEWUSER,LIMITAMTBLACKUSER,LIMITCOUNTPERDAY,LIMITAMTPERMONTH,LIMITAMTFIRSTMONTH,"
					+ "LIMITAMTVIPFIRSTMONTH, LIMITCOUNTFIRSTMONTH,LIMITCOUNTPERMONTH,LIMITTIMEFLAG,LIMITTIME,LIMITSELFFLAG,LIMITAMTSELF,"
					+ "LIMITNONBINDFLAG,MERACCOUNTTYPE,REFUNDLIMITDAYS, ADJUSTFEE,ADJUSTTIPS,ALLOWFLAG,AMTBLACKUSER,AMTFIRSTMONTH,AMTNEWUSER,"
					+ "AMTPERMONTH,AMTSELFTRANS,AMTVIPFIRSTMONTH,BACKTIPS,CALLEXPENSE,CHECKFLAG, CHECKTIME,CONTRACTSTATE,COUNTFIRSTMONTH,"
					+ "COUNTPERMONTH,DAYAMOUNT,LIQUIDATIONFEE,ONCEAMOUNT,SERVERRATE,SUPPNONBIND,TELEXPENSE,TIMESPERCARD, UNIONTIPS,APPROVETYPE,"
					+ "ISSUPPORTDEBIT,ISSUPPORTCREDIT,COMMISIONRATE_PUB,SELFTRANSONLY,LIMITAMTVIPPERMONTH,SUPORTBANKS,RISKASSURER,AGENCYUNIONFEE, "
					+ "FASTPAYLEASTCOMMISION,LIMITAMTBINDINGPERDAY,LIMITAMTBINDINGPERMONTH,ISMONTHLYFEE,SETTLEMENTTYPE,IVREXPENSERATE,OffSeaType,limitCountVipPerDay,limitCountVipPerMonth) VALUES "
					+ "(default," + merchantid + ",'" + CONTRACTNO + "','" + sysMerchantNo + "','广州易联7','"
					+ MERCHANTNAME + "','','','',0.00,0.00,0.00,0.00,'abc','0807184906', "
					+ "'UserState.Active',(current timestamp),(current timestamp+3 years),'1214152609',(current timestamp+1 years),'0000000000',"
					+ "(current timestamp+1 years), '','','APPROVED','ContractType.2201',null,'" + IndustryType
					+ "','LiquidationType.1',25.00,0.00,'001=0,0.0|', "
					+ "'CommisionType.2101',0.00,'',0.00,0.00,'0.00,0.00',0.00,1000000.00,1000000.00,10000000.00,10000000.00,10000000,10000000.00,"
					+ "10000000.00,10000000.00, 10000000,10000000," + limitTimeFlag
					+ ",'0:00:00-23:59:59',1,1000000.00,1,'MerAccountType.1',0,null,null,null,null,null,"
					+ "null,null,null,null,null,null,null,null,null,null, null,null,0.00,null,null,null,null,null,null,null,1,0,null,0,10000000.00, "
					+ "'工商银行,农业银行,建设银行,交通银行,邮政储蓄,招商银行,兴业银行,光大银行,中信银行,民生银行,华夏银行,平安银行,深发展,中国银行,广发银行,广州银行,广州农商银行,广东省农信社,',"
					+ "'RiskAssurer.MERCHANT', null,0.00,1000000.00,1000000.00,0,'SettlementType.01',null,'OffSeaType.Domestic','100000','100000')";

			CONTRACTNO = merchantNo.substring(0, 4) + "B-" + merchantTypeId.split("\\.")[1] + account3;
			String sql10 = "INSERT INTO dna_contract (CONTRACTID,MERCHANTID,CONTRACTNO,MERCHANTNO,FIRST,SECOND,"
					+ "THIRD,FOURTH,TERMINALQUANTITY,EACHDEPOSIT,TOTALDEPOSIT, MONTHEXPENSE,MAINTENANCEEXPENSE,"
					+ "DEVELOPPEOPLENAME,DEVELOPPEOPLENO,CONTRACTSTATEID,SIGNUPDATE,EXPIRINGDATE,OPERATOR,OPERATEDATE, "
					+ "SYSAPPROVEPEOPLE,SYSAPPROVEDATE,REMARK,APPROVEREMARK,APPROVESTATE,CONTRACTTYPEID,CONTRACTSCANFILEPATH,"
					+ "INDUSTRYTYPEID,LIQUIDATIONTYPE, LIQUIDATIONAMOUNT,MONTHBACKAMOUNT,MONTHBACKRATE,COMMISIONTYPEID,"
					+ "LEASTCOMMISION,COMMISIONRATE,COMMISIONFEE,SMSEXPENSE,TELEXPENSERATE, SERVEREXPENSE,LIMITAMT,"
					+ "LIMITAMTPERDAY,LIMITAMTNEWUSER,LIMITAMTBLACKUSER,LIMITCOUNTPERDAY,LIMITAMTPERMONTH,LIMITAMTFIRSTMONTH,"
					+ "LIMITAMTVIPFIRSTMONTH, LIMITCOUNTFIRSTMONTH,LIMITCOUNTPERMONTH,LIMITTIMEFLAG,LIMITTIME,LIMITSELFFLAG,"
					+ "LIMITAMTSELF,LIMITNONBINDFLAG,MERACCOUNTTYPE,REFUNDLIMITDAYS, ADJUSTFEE,ADJUSTTIPS,ALLOWFLAG,AMTBLACKUSER,"
					+ "AMTFIRSTMONTH,AMTNEWUSER,AMTPERMONTH,AMTSELFTRANS,AMTVIPFIRSTMONTH,BACKTIPS,CALLEXPENSE,CHECKFLAG, CHECKTIME,"
					+ "CONTRACTSTATE,COUNTFIRSTMONTH,COUNTPERMONTH,DAYAMOUNT,LIQUIDATIONFEE,ONCEAMOUNT,SERVERRATE,SUPPNONBIND,"
					+ "TELEXPENSE,TIMESPERCARD, UNIONTIPS,APPROVETYPE,ISSUPPORTDEBIT,ISSUPPORTCREDIT,COMMISIONRATE_PUB,SELFTRANSONLY,"
					+ "LIMITAMTVIPPERMONTH,SUPORTBANKS,RISKASSURER,AGENCYUNIONFEE, FASTPAYLEASTCOMMISION,LIMITAMTBINDINGPERDAY,"
					+ "LIMITAMTBINDINGPERMONTH,ISMONTHLYFEE,SETTLEMENTTYPE,IVREXPENSERATE,OffSeaType,limitCountVipPerDay,limitCountVipPerMonth) VALUES (default,"
					+ merchantid + ",'" + CONTRACTNO + "'," + "'" + sysMerchantNo + "','广州易联7','" + MERCHANTNAME
					+ "','','','',0.00,0.00,0.00,0.00,'abc','0807184906', 'UserState.Active',"
					+ "(current timestamp),(current timestamp+3 years),'0520173342',(current timestamp+1 years),'0520173342',"
					+ "(current timestamp+1 years), '','','APPROVED','ContractType.2201',null,'" + IndustryType
					+ "','LiquidationType.1',"
					+ "25.00,0.00,'001=0,0.0|', 'CommisionType.2101',0.00,'',0.00,0.00,'0.00,0.00',0.00,1000000.00,1000000.00,"
					+ "1000000.00,1000000.00,1000000,1000000.00,1000000.00,1000000.00,1000000, 1000000,1,'00:00:00-23:59:59',1,1000000.00,"
					+ "1,'MerAccountType.2',0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null, 0.00,"
					+ "null,null,null,null,null,null,null,0,1,null,0,1000000.00,'','RiskAssurer.MERCHANT',null,0.00,1000000.00,1000000.00,0,"
					+ "'SettlementType.01',null,'OffSeaType.Abroad','100000','100000')";

			// 插入合同2
			CONTRACTNO = merchantNo.substring(0, 4) + "B-" + merchantTypeId.split("\\.")[1] + account4;
			System.out.println(CONTRACTNO);
			String sql11 = "INSERT INTO dna_contract (CONTRACTID,MERCHANTID,CONTRACTNO,MERCHANTNO,FIRST,SECOND,THIRD,FOURTH,"
					+ "TERMINALQUANTITY,EACHDEPOSIT,TOTALDEPOSIT, MONTHEXPENSE,MAINTENANCEEXPENSE,DEVELOPPEOPLENAME,DEVELOPPEOPLENO,"
					+ "CONTRACTSTATEID,SIGNUPDATE,EXPIRINGDATE,OPERATOR,OPERATEDATE, SYSAPPROVEPEOPLE,SYSAPPROVEDATE,REMARK,APPROVEREMARK,"
					+ "APPROVESTATE,CONTRACTTYPEID,CONTRACTSCANFILEPATH,INDUSTRYTYPEID,LIQUIDATIONTYPE, LIQUIDATIONAMOUNT,MONTHBACKAMOUNT,"
					+ "MONTHBACKRATE,COMMISIONTYPEID,LEASTCOMMISION,COMMISIONRATE,COMMISIONFEE,SMSEXPENSE,TELEXPENSERATE, SERVEREXPENSE,"
					+ "LIMITAMT,LIMITAMTPERDAY,LIMITAMTNEWUSER,LIMITAMTBLACKUSER,LIMITCOUNTPERDAY,LIMITAMTPERMONTH,LIMITAMTFIRSTMONTH,"
					+ "LIMITAMTVIPFIRSTMONTH, LIMITCOUNTFIRSTMONTH,LIMITCOUNTPERMONTH,LIMITTIMEFLAG,LIMITTIME,LIMITSELFFLAG,LIMITAMTSELF,"
					+ "LIMITNONBINDFLAG,MERACCOUNTTYPE,REFUNDLIMITDAYS, ADJUSTFEE,ADJUSTTIPS,ALLOWFLAG,AMTBLACKUSER,AMTFIRSTMONTH,AMTNEWUSER,"
					+ "AMTPERMONTH,AMTSELFTRANS,AMTVIPFIRSTMONTH,BACKTIPS,CALLEXPENSE,CHECKFLAG, CHECKTIME,CONTRACTSTATE,COUNTFIRSTMONTH,"
					+ "COUNTPERMONTH,DAYAMOUNT,LIQUIDATIONFEE,ONCEAMOUNT,SERVERRATE,SUPPNONBIND,TELEXPENSE,TIMESPERCARD, UNIONTIPS,APPROVETYPE,"
					+ "ISSUPPORTDEBIT,ISSUPPORTCREDIT,COMMISIONRATE_PUB,SELFTRANSONLY,LIMITAMTVIPPERMONTH,SUPORTBANKS,RISKASSURER,AGENCYUNIONFEE, "
					+ "FASTPAYLEASTCOMMISION,LIMITAMTBINDINGPERDAY,LIMITAMTBINDINGPERMONTH,ISMONTHLYFEE,SETTLEMENTTYPE,IVREXPENSERATE,OffSeaType,limitCountVipPerDay,limitCountVipPerMonth) VALUES "
					+ "(default," + merchantid + ",'" + CONTRACTNO + "','" + sysMerchantNo + "','广州易联7','"
					+ MERCHANTNAME + "','','','',0.00,0.00,0.00,0.00,'abc','0807184906', "
					+ "'UserState.Active',(current timestamp),(current timestamp+3 years),'1214152609',(current timestamp+1 years),'0000000000',"
					+ "(current timestamp+1 years), '','','APPROVED','ContractType.2201',null,'" + IndustryType
					+ "','LiquidationType.1',25.00,0.00,'001=0,0.0|', "
					+ "'CommisionType.2101',0.00,'',0.00,0.00,'0.00,0.00',0.00,1000000.00,1000000.00,10000000.00,10000000.00,10000000,10000000.00,"
					+ "10000000.00,10000000.00, 10000000,10000000," + limitTimeFlag
					+ ",'0:00:00-23:59:59',1,1000000.00,1,'MerAccountType.1',0,null,null,null,null,null,"
					+ "null,null,null,null,null,null,null,null,null,null, null,null,0.00,null,null,null,null,null,null,null,1,0,null,0,10000000.00, "
					+ "'工商银行,农业银行,建设银行,交通银行,邮政储蓄,招商银行,兴业银行,光大银行,中信银行,民生银行,华夏银行,平安银行,深发展,中国银行,广发银行,广州银行,广州农商银行,广东省农信社,',"
					+ "'RiskAssurer.MERCHANT', null,0.00,1000000.00,1000000.00,0,'SettlementType.01',null,'OffSeaType.Abroad','100000','100000')";

			if (!offSeaType.equals("OffSeaType.Domestic") && !offSeaType.equals("OffSeaType.Abroad")) {
				System.out.println("SQL8:" + sql8);
				st.executeUpdate(sql8);
				Thread.sleep(1000);
				System.out.println("SQL9:" + sql9);
				st.executeUpdate(sql9);
				Thread.sleep(1000);
				System.out.println("SQL10:" + sql10);
				st.executeUpdate(sql10);
				Thread.sleep(1000);
				System.out.println("SQL11:" + sql11);
				st.executeUpdate(sql11);
			} else if (offSeaType.equals("OffSeaType.Domestic")) {
				System.out.println("SQL8:" + sql8);
				st.executeUpdate(sql8);
				Thread.sleep(1000);
				System.out.println("SQL9:" + sql9);
				st.executeUpdate(sql9);
				Thread.sleep(1000);
			} else {
				System.out.println("SQL10:" + sql10);
				st.executeUpdate(sql10);
				Thread.sleep(1000);
				System.out.println("SQL11:" + sql11);
				st.executeUpdate(sql11);
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	/**
	 * 生成一个定长的纯0字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuffer sb = new StringBuffer();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

	

	

	

	public void deleteInfo() {
		try {
			// idCard = AES.encrypt(idCard);
			Statement st = this.conn().createStatement();
			ResultSet rs = null;

			String sql3 = "delete from dna_customer where name = " + "'李海林'";
			// System.out.println("2删除" + cardNo2 + " " + sql3);
			st.executeUpdate(sql3);

			String sql4 = "delete from dna_account  where name = " + "'李海林'";
			// System.out.println("3删除" + cardNo2 + " " + sql4);
			st.executeUpdate(sql4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

	

	public int updateDiction(String code, String value) throws SQLException {
		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		int flag = 0;
		try {
			String sql3 = "update dna_diction set value='" + value + "' where code='" + code + "'";
			System.out.println("6删除" + sql3);
			flag = st.executeUpdate(sql3);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return flag;
	}

	
	/*
	 * 成功支付返回格式为:" 01|0000 " orderstate|respcode
	 */
	public String getOrderStateAndRespcodeByMerOrderId(String merorderid) throws SQLException {
		String OrderStateAndRespcode = "";

		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		if (merorderid.subSequence(0, 2).equals("02"))
			merorderid = "01" + merorderid.substring(2, merorderid.length());
		try {
			String sql2 = "select * from dna_transactionlog where  merorderid='" + merorderid
					+ "' order by transtime desc";
			rs = st.executeQuery(sql2);
			String orderState = "";
			if (rs.next()) {
				orderState = rs.getObject("params").toString();
				orderState = orderState.substring(orderState.indexOf("OrderState=") + 11,
						orderState.indexOf("OrderState=") + 13);
				OrderStateAndRespcode = orderState + "|" + rs.getObject("result").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		// System.out.println(OrderStateAndRespcode);
		return OrderStateAndRespcode;
	}

	public String getTransChannelByOrderNo(String orderNo) throws SQLException {

		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		String transchannel = "";
		try {
			String sql2 = "select * from dna_order_new where  orderno='" + orderNo + "'";
			rs = st.executeQuery(sql2);
			String orderState = "";
			if (rs.next()) {
				transchannel = rs.getString("transchannel") + "|" + rs.getString("customMsg");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		System.out.println(orderNo);
		System.out.println(transchannel);
		return transchannel;
	}

	
	

	public String getSmsCodeByMerchantOrder(String merchantNo) throws SQLException {
		String smscode = "";
		String orderno = "";

		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		try {
			String sql2 = "select * from dna_order_new where merchantorderno='" + merchantNo + "'";
			// + "' and paystate = 'SystemError.T451' order by smscode desc";
			rs = st.executeQuery(sql2);
			if (rs.next()) {
				if (rs.getString("smscode").indexOf(",") > -1) {
					smscode = rs.getString("smscode").split(",")[1];
				}
				orderno = rs.getString("orderno");
				System.out.println("smscode:" + smscode);
				System.out.println("orderno:" + orderno);
				if (smscode.equals("") || smscode == null)
					orderno = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return smscode + orderno;
	}


	public String getSmsContentByOrderNo(String orderNo) throws SQLException {
		String smsContent = "";

		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		try {
			// String sql2 =
			// "select messagecontent from dna_shortmessage a ,dna_order_new b
			// where a.orderno=b.orderno and b.merchantorderno='"
			// + orderNo + "'";

			String sql2 = "select  messagecontent from dna_shortmessage where  orderno='" + orderNo + "'";

			rs = st.executeQuery(sql2);
			while (rs.next()) {
				smsContent += "【" + rs.getString("messagecontent") + "】 ";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		System.out.println(orderNo);
		System.out.println(smsContent);
		return smsContent;
	}

	public String getMessageIDByOrderNo(String orderNo) throws SQLException {
		String smsContent3 = "";
		String smsContent2 = "";
		String smsContent = "";

		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		try {
			// String sql2 =
			// "select messageId,messagecontent,a.remark from dna_shortmessage a
			// ,dna_order_new b where a.orderno=b.orderno and
			// messagetype='iTunes_SMS.0000_3' and b.merchantorderno='"
			String sql2 = "select  messageId,messagecontent,a.remark from dna_shortmessage a where  (messagetype='iTunes_SMS.0000_3' or messagetype='iTunes_SMS.INFORM') and orderno='"

					+ orderNo + "'";

			rs = st.executeQuery(sql2);
			if (rs.next()) {
				smsContent = rs.getString("messageId");
				smsContent2 = rs.getString("messagecontent");
				smsContent3 = rs.getString("remark");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		// System.out.println(orderNo);
		// System.out.println(smsContent);
		return smsContent + "|" + smsContent2 + "|" + smsContent3;
	}

	/*
	 * 成功支付返回格式为:" 01|0000 " orderstate|respcode
	 */
	public String getOrderStateAndRespcodeByReference(String reference) throws SQLException {
		String OrderStateAndRespcode = "";
		Statement st = this.conn().createStatement();
		ResultSet rs = null;
		try {
			String sql2 = "select params from dna_transactionlog where  orderid='" + reference + "'";
			rs = st.executeQuery(sql2);
			String orderState = "";
			String params = "";
			String respCode = "";
			String orderNo = "";
			if (rs.next()) {
				params = rs.getObject("params").toString();
				System.out.println(orderState);
				orderState = params.substring(params.indexOf("OrderState=") + 11, params.indexOf("OrderState=") + 13);
				respCode = params.substring(params.indexOf("RespCode=") + 9, params.indexOf("RespCode=") + 13);
				orderNo = params.substring(params.indexOf("OrderNo=") + 10, params.indexOf("&OrderState"));
				OrderStateAndRespcode = orderState + "|" + respCode + "|" + orderNo;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st == null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		System.out.println(reference);
		System.out.println(OrderStateAndRespcode);
		return OrderStateAndRespcode;
	}


	
	public void runUpdateDate(String sql) {
		try {
			Statement st = this.conn().createStatement();
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getOccur(String src, String find) {
		int o = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++o;
		}
		return o;
	}

	public int NextInt(final int min, final int max) {
		Random rand;
		rand = new Random();
		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}

	// //select * from dna_riskrule where ruleid='5652'
	// public DnaRiskrule getTestCaseByTestRule (String ruleId){
	// DnaRiskrule dnaRiskrule = new DnaRiskrule();
	// try{
	// Statement st = this.conn().createStatement();
	// ResultSet rs = null;
	// String sql2 = "select * from dna_riskrule where ruleid='"
	// + ruleId + "'";
	// rs = st.executeQuery(sql2);
	// while (rs.next()) {
	// dnaRiskrule.setAccounttype(rs.getString("accounttype"));
	// dnaRiskrule.setAction(rs.getString("action"));
	// dnaRiskrule.setAmtcountrange(rs.getString("amtcountrange"));
	// dnaRiskrule.setAmtdecidemode(rs.getString("amtdecidemode"));
	// dnaRiskrule.setAmtlowerbound(rs.getDouble("amtlowerbound"));
	// dnaRiskrule.setAmtperiodunit(rs.getString("amtperiodunit"));
	// dnaRiskrule.setAmtupperbound(rs.getDouble("amtupperbound"));
	// dnaRiskrule.setAreaaccuracy(rs.getString("areaaccuracy"));
	// dnaRiskrule.setAreajudgeobj(rs.getString("areajudgeobj"));
	// dnaRiskrule.setBank(rs.getString("bank"));
	// dnaRiskrule.setBelongto(rs.getString("belongto"));
	// dnaRiskrule.setBindingtype(rs.getString("bindingtype"));
	// dnaRiskrule.setCheckmode(rs.getString("checkmode"));
	// dnaRiskrule.setClassname(rs.getString("classname"));
	// dnaRiskrule.setCreatedby(rs.getString("createdby"));
	// dnaRiskrule.setCreateddt(rs.getTimestamp("createddt"));
	// dnaRiskrule.setCustomriskgroup(rs.getString("customriskgroup"));
	// dnaRiskrule.setErrorcode(rs.getString("errorcode"));
	// dnaRiskrule.setExcludedmerchants(rs.getString("excludedmerchants"));
	// dnaRiskrule.setForchannelnopwd(rs.getString("forchannelnopwd"));
	// dnaRiskrule.setfortrustedmobile(rs.getString("fortrustedmobile"));
	// dnaRiskrule.setFordifferentcity(rs.getString("fordifferentcity"));
	// dnaRiskrule.setFordifferentregion(rs.getString("fordifferentregion"));
	// dnaRiskrule.setFordiffmunicipality(rs.getString("fordiffmunicipality"));
	// dnaRiskrule.setForforeignorder(rs.getString("forforeignorder"));
	// dnaRiskrule.setForinitialorder(rs.getString("forinitialorder"));
	// dnaRiskrule.setForintegratedcall(rs.getString("forintegratedcall"));
	// dnaRiskrule.setForinternational(rs.getString("forinternational"));
	// dnaRiskrule.setForischinamobileboss(rs.getString("forischinamobileboss"));
	// dnaRiskrule.setFormerchantbindonly(rs.getString("formerchantbindonly"));
	// dnaRiskrule.setFormerchantolduser(rs.getString("formerchantolduser"));
	// dnaRiskrule.setFormobileplugin(rs.getString("formobileplugin"));
	// dnaRiskrule.setFororderstate2(rs.getString("fororderstate2"));
	// dnaRiskrule.setForpayimmediately(rs.getString("forpayimmediately"));
	// dnaRiskrule.setForregisteredmobile(rs.getString("forregisteredmobile"));
	// dnaRiskrule.setForrelativemobile(rs.getString("forrelativemobile"));
	// dnaRiskrule.setForriskassurerbydna(rs.getString("forriskassurerbydna"));
	// dnaRiskrule.setForselftrans(rs.getString("forselftrans"));
	// dnaRiskrule.setForsignedbyuser(rs.getString("forsignedbyuser"));
	// dnaRiskrule.setGroupname(rs.getString("groupname"));
	// dnaRiskrule.setGrouppriority(rs.getInt("grouppriority"));
	// dnaRiskrule.setGuide(rs.getString("guide"));
	// dnaRiskrule.setIndustry(rs.getString("industry"));
	// dnaRiskrule.setIssameregion(rs.getString("issameregion"));
	// dnaRiskrule.setManuallyverifytype(rs.getString("manuallyverifytype"));
	// dnaRiskrule.setMobileoperator(rs.getString("mobileoperator"));
	// dnaRiskrule.setModifiedby(rs.getString("modifiedby"));
	// dnaRiskrule.setModifieddt(rs.getTimestamp("modifieddt"));
	// dnaRiskrule.setPastdays(rs.getInt("pastdays"));
	// dnaRiskrule.setPastdaystype(rs.getString("pastdaystype"));
	// dnaRiskrule.setPattern(rs.getString("pattern"));
	// dnaRiskrule.setPriority(rs.getInt("priority"));
	// dnaRiskrule.setProductno(rs.getString("productno"));
	// dnaRiskrule.setRiskno(rs.getString("riskno"));
	// dnaRiskrule.setRuledemands(rs.getString("ruledemands"));
	// dnaRiskrule.setRuleid(rs.getInt("ruleid"));
	// dnaRiskrule.setRuleversion(rs.getInt("ruleversion"));
	// dnaRiskrule.setState(rs.getString("state"));
	// dnaRiskrule.setSysmerchantno(rs.getString("sysmerchantno"));
	// dnaRiskrule.setTitle(rs.getString("title"));
	// dnaRiskrule.setUserlevel(rs.getString("userlevel"));
	// dnaRiskrule.setValiddays(rs.getInt("validdays"));
	// dnaRiskrule.setValiddays2(rs.getInt("validdays2"));
	// dnaRiskrule.setValue(rs.getString("value"));
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return dnaRiskrule;
	// }

}
