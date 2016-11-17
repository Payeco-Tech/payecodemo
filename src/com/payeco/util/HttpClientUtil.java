package com.payeco.util;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;  

public class HttpClientUtil {
	
	private HttpClient getNewHttpClient() {  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore  
                    .getDefaultType());  
            trustStore.load(null, null);  
            SSLSocketFactory sf = new SSLSocketFactory(trustStore);  
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            HttpParams params = new BasicHttpParams();  
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
            SchemeRegistry registry = new SchemeRegistry();  
            registry.register(new Scheme("http", PlainSocketFactory  
                    .getSocketFactory(), 80));  
            registry.register(new Scheme("https", sf, 443));  
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(  
                    params, registry);  
            return new DefaultHttpClient(ccm, params);  
        } catch (Exception e) {  
            return new DefaultHttpClient();  
        }  
    }  
	
	public String doGet(String httpGetUrl) {
		DefaultHttpClient httpclient = (DefaultHttpClient) getNewHttpClient();  
		HttpContext localContext = new BasicHttpContext();
        try {  
            //Secure Protocol implementation.    
            SSLContext ctx = SSLContext.getInstance("SSL");  
            //Implementation of a trust manager for X509 certificates    
            X509TrustManager tm = new X509TrustManager() {  
            	
            	public void checkClientTrusted(X509Certificate[] xcs,  
                        String string) throws CertificateException {  
  
                }  
  
                public void checkServerTrusted(X509Certificate[] xcs,  
                        String string) throws CertificateException {  
                }  
  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
            };  
            ctx.init(null, new TrustManager[] { tm }, null);  
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);  
            ClientConnectionManager ccm = httpclient.getConnectionManager();  
            //register https protocol in httpclient's scheme registry    
            SchemeRegistry sr = ccm.getSchemeRegistry();  
            sr.register(new Scheme("https", ssf, 444)); 

            HttpGet httpGet = new HttpGet(httpGetUrl);  
            HttpResponse response = httpclient.execute(httpGet, localContext); 
            return response.toString();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;
	}

	 public String doPost(String url,Map<String,String> map,String charset){  
	        HttpClient httpClient = null;  
	        HttpPost httpPost = null;  
	        String result = null;  
	        try{  
	            httpClient = HttpsUtil.newHttpsClient(); //new SSLClient();  
	            httpPost = new HttpPost(url);  
	            //…Ë÷√≤Œ ˝  
	            List<NameValuePair> list = new ArrayList<NameValuePair>();  
	            Iterator iterator = map.entrySet().iterator();  
	            while(iterator.hasNext()){  
	                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	            }  
	            if(list.size() > 0){  
	                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
	                httpPost.setEntity(entity);  
	            }  
	            HttpResponse response = httpClient.execute(httpPost);  
	            if(response != null){  
	                HttpEntity resEntity = response.getEntity();  
	                if(resEntity != null){  
	                    result = EntityUtils.toString(resEntity,charset);  
	                }  
	            }  
	        }catch(Exception ex){  
	            ex.printStackTrace();  
	        }  
	        return result;  
	    }  
	 
	 
	 public static void main(String[] args){  
	        TestMain main = new TestMain();  
	        main.test();  
	    }  
	 
}

class TestMain {  
   // private String url = "http://test.payeco.com:9080/payecodemo/third/OrderServerWeb";  
    private String url = "https://10.123.1.71:9444/third/OrderServerWeb?";  
    private String charset = "utf-8";  
    private HttpClientUtil httpClientUtil = null;  
      
    public TestMain(){  
        httpClientUtil = new HttpClientUtil();  
    }  
      
    public void test(){  
        String httpOrgCreateTest = url + "httpOrg/create";  
        Map<String,String> createMap = new HashMap<String,String>();  
        createMap.put("ProcCode","0120");  
        createMap.put("Version","2.0.0");  
        createMap.put("ProcessCode","310000");  
        createMap.put("TransDatetime","0603105129");  
        createMap.put("AcqSsn","105129");  
        createMap.put("SettleDate","0603");  
        createMap.put("MerchantNo","021462847066331");  
        createMap.put("Mac","C8AE480BB2F96BFA4881DB17E6796ECF");
        String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,createMap,charset);  
        System.out.println("result:"+httpOrgCreateTestRtn); 
    }  
}  