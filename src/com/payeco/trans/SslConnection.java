package com.payeco.trans;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author Administrator
 */
public class SslConnection {

	
    public static byte[] readResponse(HttpURLConnection connect) throws Exception {

        BufferedInputStream in = new BufferedInputStream(connect.getInputStream());

        LinkedList<Httpbuf> bufList = new LinkedList<Httpbuf>();
        int size = 0;
        byte buf[];

        do {
            buf = new byte[128];
            int num = in.read(buf);
            if (num == -1) {
                break;
            }
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
    
    public static void respsmsTest() throws Exception{
//    	String xml = "<?xml version='1.0' encoding='GBK'?>" + "<reports>" +"<report><corp_id>11111</corp_id><mobile>13500000000</mobile><seq>1</seq><msg_id>45952</msg_id><result>0</result></report>" + "<report><corp_id>22222</corp_id><mobile>15811111111</mobile><seq>0</seq><msg_id>45951</msg_id><result>200</result></report>"+"</reports>";
    	String xml = "<?xml version='1.0' encoding='GBK'?>" +"<delivers>" +"<deliver><corp_id>test</corp_id><mobile>13570597810</mobile><ext>41</ext><time>2013-03-29 12:10:00</time><content>测试短信</content></deliver>" +"</delivers>";
    	HttpURLConnection connect = null;
//    	String rurl = "https://58.248.38.252:9443/sms/Receive";
    	String rurl = "https://127.0.0.1:8443/sms/Receive?type=deliver";
    	connect = new SslConnection().openConnection(rurl);
        connect.setReadTimeout(30000);
        connect.setConnectTimeout(10000);
        connect.setRequestMethod("POST");
        connect.setDoInput(true);
        connect.setDoOutput(true);
        connect.connect();
        BufferedOutputStream out = new BufferedOutputStream(connect.getOutputStream());
        out.write(("" + xml).getBytes("gbk"));
        out.flush();
        out.close();
        BufferedInputStream in = new BufferedInputStream(connect.getInputStream());
        byte[] bts = new byte[10000];
        int totalLen = 0, len = 0;
        while ((len = in.read(bts, totalLen, 1000)) != -1) {
            totalLen += len;
            System.out.println("Response readLen: " + len);
        }
        String result = new String(bts, "GBK");
        System.out.println("Response result: " + result);
        connect.getInputStream().close();
        connect.disconnect();
    }
    

     public HttpURLConnection openConnection(String strUrl) throws Exception {

        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        URL url = new URL(strUrl);
        return (HttpURLConnection) url.openConnection();
    }

    public String connect(String strUrl) throws Exception {

        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        URL url = new URL(strUrl);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        byte[] bts = new byte[100];
        urlConn.getInputStream().read(bts);
        String result = new String(bts).trim();
        return result;

    }
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
            return true;
        }
    };

    private static void trustAllHttpsCertificates() throws Exception {

        //  Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts =
                new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new miTM();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc =
                javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());

    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) throws
                java.security.cert.CertificateException {
            return;
        }
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