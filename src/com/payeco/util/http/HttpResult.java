package com.payeco.util.http;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import com.payeco.util.Toolkit;


public class HttpResult {
	private int httpStatus = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
	private String httpMessage = "";
	private Object data;
	private String trace = "";
	
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getHttpMessage() {
		return httpMessage;
	}
	public void setHttpMessage(String httpMessage) {
		this.httpMessage = httpMessage;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public void addTrace(String trace) {
		if(Toolkit.isNotEmpty(trace))
			this.trace += trace;
	}
	
	@Override
	public String toString(){
		String str = httpStatus + " " + httpMessage + "\n";
		if(null != data){
			if(data instanceof byte[]){
				try {
					str += new String((byte[])data , "UTF-8") + "\n";
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				str += data + "\n";
			}
		}
		str += trace;
		return str;
	}
}
