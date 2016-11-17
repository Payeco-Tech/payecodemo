package com.payeco.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.payeco.util.Toolkit;

public class TransactionClient {
	
	
    public TransactionClient(String url) {
        this.url = url;
    }
    private String url;
    private int timeout = 300000;

	 /**
     * @param 发送报文方法
     * */
    public String transact(String request) throws Exception {
    	
        try {
            HttpURLConnection connect = null;
            if (!url.contains("https:")) {
                URL urlConnect = new URL(url);
                connect = (HttpURLConnection) urlConnect.openConnection();
            } else {
                SslConnection urlConnect = new SslConnection();
                connect = (HttpURLConnection) urlConnect.openConnection(url);
            }

            connect.setReadTimeout(timeout);
            connect.setConnectTimeout(timeout);
            connect.setRequestMethod("POST");
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestProperty("content-type", "text/html;charset=utf-8");

            connect.connect();
            BufferedOutputStream out = new BufferedOutputStream(connect.getOutputStream());
            String content = Toolkit.padLeft(request.length() + "", 6) + request;
            out.write(content.getBytes("UTF-8"));
            out.flush();
            out.close();

            BufferedInputStream in = new BufferedInputStream(connect.getInputStream());
            byte[] l_b = new byte[6];
            int l_l = 0,l_r = 0;
            while(l_l < 6 && (l_r = in.read(l_b,l_l,6-l_l)) != -1)
            	l_l += l_r;
            if(l_l < 6) {
                return "";
            }
            int t_l = Integer.parseInt(new String(l_b));
            
            byte[] bts = new byte[t_l];
            int totalLen = 0, len = 0;
            while (totalLen < t_l && (len = in.read(bts, totalLen, t_l-totalLen)) != -1) {
                totalLen += len;
            }
            if(totalLen < t_l) {
                return "";
            }
            String result = Toolkit.toString(new String(bts, "UTF-8"));
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}
