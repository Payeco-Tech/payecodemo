package com.payeco.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;







public class Toolkit {
	  public static String base64Encode(byte[] data) throws IOException {
	        if(data == null)
	            return "";
	        return new sun.misc.BASE64Encoder().encode(data);
	    }
	  
	  public static byte[] base64Decode(String data) throws IOException {
	        if(isNullOrEmpty(data))
	            return null;
	        return new sun.misc.BASE64Decoder().decodeBuffer(data);
	    }
	  public synchronized static String getPropertyFromFile(String filename, String key)
	    {
	        ResourceBundle rb = ResourceBundle.getBundle(filename);
	        return rb.getString(key).trim();
	    }

	    public synchronized static String getPropertyFromFile(String key)
	    {
	        ResourceBundle rb = ResourceBundle.getBundle("systemsetting");
	        return rb.getString(key).trim();
	    }
	    
	    public static String getMerchantKey(String merchantKey,String certPfx,String certPfxPasswd){
			String key = "";
			try {
				
				if(isNullOrEmpty(merchantKey)){
					return "";
				}
				byte[] keyBt = RSA.decrypt(base64Decode(merchantKey), certPfx, certPfxPasswd);
		        return new String(keyBt, "UTF-8");
			} catch (Exception e) {
				return key;
			}
		}
	    
	    public static boolean isNullOrEmpty(Object o){
			return o == null || "".equals(o.toString().trim());
		}
		
		
		 public static String random(int len) {
		        String str = "";
		        java.util.Random rander = new java.util.Random(System.currentTimeMillis());
		        for(int i=0; i<len; i++) {
		            str+= HEXCHAR[rander.nextInt(16)];
		        }
		        return str;
		  }
		    
		 private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7',
		        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		 
		 
		  public static String bytePadLeft(String input, char c, int length) {
		        String output = input;
		        while (output.getBytes().length < length) {
		            output = c + output;
		        }
		        return output;
		    }
		  
		  
		  public static String sign(String keyPara, String srcPara, String cert,String certPfx,String certPfxPasswd) throws Exception {
		        if (Toolkit.isNullOrEmpty(keyPara)) {
		            keyPara = Toolkit.random(24);
		        }
		        String key = keyPara;
		        String src = srcPara;
                String keyEncrypt = "";
		        if (!Toolkit.isNullOrEmpty(cert)) { //公钥为空，不加密密钥
		            byte[] keyEnc = RSA.encrypt(key.getBytes("UTF-8"), cert);
		            keyEncrypt= Toolkit.base64Encode(keyEnc);
		        }
		        
		        byte[] srcEnc = TripleDes.encrypt(key.getBytes("UTF-8"), src.getBytes("UTF-8"));
		        String srcEncrypt = Toolkit.base64Encode(srcEnc);
		       
		        byte[] srcSigned = RSA.sign(src.getBytes("UTF-8"), certPfx,certPfxPasswd);
		        String srcSign = Toolkit.base64Encode(srcSigned);
		        byte[] pub_key = RSA.getPublicKey(certPfx,certPfxPasswd).getEncoded();
		        String pubKeyStr = Toolkit.base64Encode(pub_key);
		       
                String version = "RSA.3DES.MD5withRSA";
		        String tData = Toolkit.base64Encode(version.getBytes("UTF-8"));
		        tData += "|" + pubKeyStr;
		        tData += "|" + keyEncrypt;
		        tData += "|" + srcEncrypt;
		        tData += "|" + srcSign;
		        return tData;
		    }
		  
		  
		  public static String signWithMD5(String keyPara, String srcPara, String pubkey) throws Exception {
		        if (Toolkit.isNullOrEmpty(keyPara)) {
		            keyPara = Toolkit.random(24);
		        }
		        String key = keyPara;
		        String src = srcPara;
                 String keyEncrypt = "";
		        if (!Toolkit.isNullOrEmpty(pubkey)) { //公钥为空，不加密密钥
		            byte[] keyEnc = RSA.encrypt(key.getBytes("UTF-8"), pubkey);
		            keyEncrypt= Toolkit.base64Encode(keyEnc);
		        }
		        
		        byte[] srcEnc = TripleDes.encrypt(key.getBytes("UTF-8"), src.getBytes("UTF-8"));
		        String srcEncrypt = Toolkit.base64Encode(srcEnc);
		       
		 
		        MD5 md5 = new MD5();
		        //src = "<?xml version=\"1.0\" encoding=\"utf-8\"?><x:NetworkRequest xmlns:x=\"http://www.payeco.com\" xmlns:xsi=\"http://www.w3.org\"><Version>2.1.0</Version><ProcCode>0200</ProcCode><ProcessCode>190011</ProcessCode><Amount>0.3</Amount><Currency>HKD</Currency><MerchantNo>1472181543236</MerchantNo><MerchantOrderNo>20161222165756</MerchantOrderNo><Description>Test Decription</Description><AcqSsn>165756</AcqSsn><TransDatetime>20161222165756</TransDatetime><SynAddress>http://test.payeco.com:9080/payecodemo/servlet/CallBackServlet</SynAddress><AsynAddress>http://10.123.65.35:8080/payecodemo/servlet/AsynServlet</AsynAddress><OrderFrom>28</OrderFrom><Mac>F8FEAE8B8CE1D000EAF30312194B50EC</Mac></x:NetworkRequest>";
	            //src = "<?xml version=\"1.0\" encoding=\"utf-8\"?><x:NetworkRequest xmlns:x=\"http://www.payeco.com\" xmlns:xsi=\"http://www.w3.org\"><Version>2.1.0</Version><ProcCode>0200</ProcCode><ProcessCode>190011</ProcessCode><Amount>0.3</Amount><Currency>HKD</Currency><MerchantNo>1472181543236</MerchantNo><MerchantOrderNo>20161222165756</MerchantOrderNo><Description>Test Decription</Description><AcqSsn>165756</AcqSsn><TransDatetime>20161222165756</TransDatetime><SynAddress>http://test.payeco.com:9080/payecodemo/servlet/CallBackServlet</SynAddress><AsynAddress>http://10.123.65.35:8080/payecodemo/servlet/AsynServlet</AsynAddress><OrderFrom>28</OrderFrom><Mac>F8FEAE8B8CE1D000EAF30312194B50EC</Mac></x:NetworkRequest>";
		        String strSign = md5.getMD5ofByte(src.getBytes("UTF-8"));
	            String srcSign = Toolkit.base64Encode(strSign.getBytes("UTF-8"));
	            //System.out.println("strSign:"+strSign+"\nsrcSign:"+srcSign);
	            
                String version = "MD5";
		        String tData = Toolkit.base64Encode(version.getBytes("UTF-8"));
		        tData += "|" + "";
		        tData += "|" + keyEncrypt;
		        tData += "|" + srcEncrypt;
		        tData += "|" + srcSign;
		        return tData;
		    }
		  
		  public static String getCurrency(String currencyType){
//				人民币 港币 澳门币 台币 马元 朝鲜 泰铢 美元 日元 韩币 新元 欧元 英镑
//				CNY HKD MOP TWD MYR KPW THB USD JPY KRW SGD EUR GBP
				if(currencyType.equals("01")){
					return "CNY";
				}
				if(currencyType.equals("02")){
					return "HKD";
				}
				if(currencyType.equals("03")){
					return "MOP";
				}
				if(currencyType.equals("04")){
					return "TWD";
				}
				if(currencyType.equals("05")){
					return "MYR";
				}
				if(currencyType.equals("06")){
					return "KPW";
				}
				if(currencyType.equals("07")){
					return "THB";
				}
				if(currencyType.equals("08")){
					return "USD";
				}
				if(currencyType.equals("09")){
					return "JPY";
				}
				if(currencyType.equals("10")){
					return "KRW";
				}
				if(currencyType.equals("11")){
					return "SGD";
				}
				if(currencyType.equals("12")){
					return "EUR";
				}
				if(currencyType.equals("13")){
					return "GBP";
				}
				return "CNY";
			}

		  public static String getElementValue(String elemName, Document doc) {
		        String elemValue = "";
		        if (null != doc) {
		            Element elem = null;
		            elem = (Element) doc.getElementsByTagName(elemName).item(0);
		            if (null != elem && null != elem.getFirstChild()) {
		                elemValue = elem.getFirstChild().getNodeValue();
		            }
		        }
		        return elemValue;
		    }
		  
		  
		  public static Document getDocument(String xml) {
		        StringBuffer sb = new StringBuffer(xml);
		        Document doc = null;
		        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = null;
		        try {
		            db = dbf.newDocumentBuilder();
		            doc = db.parse(new InputSource(new StringReader(sb.toString())));
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return doc;
		    }
		  
		  public static String getStringFromDocument(Document doc) {
		        String result = null;

		        if (doc != null) {
		            StringWriter strWtr = new StringWriter();
		            StreamResult strResult = new StreamResult(strWtr);
		            TransformerFactory tfac = TransformerFactory.newInstance();
		            try {
		                Transformer t = tfac.newTransformer();
		                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		                t.setOutputProperty(OutputKeys.INDENT, "yes");
		                t.setOutputProperty(OutputKeys.STANDALONE, "yes");
		                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,// text
		                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		                t.transform(new DOMSource(doc.getDocumentElement()), strResult);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            result = strResult.getWriter().toString();
		        }

		        return result;
		    }
		  
		  
		  public static String padLeft(String input, char c, int length) {
	          String output = input;
	          while (output.length() < length) {
	              output = c + output;
	          }
	          return output;
	      }
		  
		  public static String padLeft(String input, int length) {
	          return padLeft(input, '0', length);
	      }
		  
		  public static String toString(Object o) {
	          if (o == null) {
	              return "";
	          }
	          return o.toString().trim();
	      }
		  
		  public static String verify(String keyPara, String sign) throws Exception {

	        String[] values = sign.split("\\|");
	        String version = new String(Toolkit.base64Decode(values[0]), "UTF-8");
	        
	        String srcEncrypt = values[3];
	        String srcSign = values[4];

	        byte[] srcEnc = Toolkit.base64Decode(srcEncrypt);
	        byte[] srcSigned = Toolkit.base64Decode(srcSign);
	        byte[] keyBt = null;

	        keyBt = keyPara.getBytes("UTF-8");
	        byte[] srcBt = TripleDes.decrypt(keyBt, srcEnc);
	        String src = new String(srcBt, "UTF-8");
	        System.out.println("\nsrc:"+src.replace("\n    ", "")+"\n");
	      
            MD5 md5 = new MD5();
            String strSign = md5.getMD5ofByte(srcBt);
            if (!strSign.equals(new String(srcSigned, "UTF-8"))) {
                throw new Exception("fail to verifySignedData MD5");
            }
            
	        return src;
	    }
		  
		public static String getValue(String xml, String name){
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
			
		public static String getString(String src) {
			   return (Toolkit.isNullOrEmpty(src) ? "" : (" " + src.trim()));
		}

		public static String trims(String str) {
			// TODO Auto-generated method stub
			if(null == str) {
				return "";
			}
			return str.trim();
		}

		public static boolean isNotBlank(String str) {
			// TODO Auto-generated method stub
			return !isNullOrEmpty(str);
		}

		public static boolean isNumeric(String str){  
			for (int i = str.length();--i>=0;){    
				if (!Character.isDigit(str.charAt(i))){  
					return false;  
				}  
			}  
			return true;  
		}

		public static boolean isNotEmpty(String str) {
			// TODO Auto-generated method stub
			return !isNullOrEmpty(str);
		}  

}
