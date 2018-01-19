package com.payeco.util.http;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.payeco.util.Toolkit;

/**
 * http����֧�ֶ�·�ɺ��Զ�����
 * @author luoyunqiu
 * @date 2016-09-12 12:27
 *
 */
public class HttpProxyRouter {
	private static Logger logger = Logger.getLogger(HttpProxyRouter.class);
	private Map<String, HttpAddressQueue> proxyAddressMap = new HashMap<String, HttpAddressQueue>();
	private long lastUserTime = System.currentTimeMillis();
	private String proxyType = "";
	
	public long getLastUserTime() {
		return lastUserTime;
	}

	public void setLastUserTime(long lastUserTime) {
		this.lastUserTime = lastUserTime;
	}

	private void clearQueue(){
		Iterator<Map.Entry<String, HttpAddressQueue>> iterator = proxyAddressMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, HttpAddressQueue> entity = iterator.next();
			long max = 1000L * 60 * 60 * 24;
			long dis = System.currentTimeMillis() - entity.getValue().getLastUseTime();
			if(dis > max){
				logger.info("Clear Expire HttpAddressQueue " + entity.getKey());
				iterator.remove();
			}
		}
	}
	
	private HttpAddressQueue fillAddress(String addresses){
		clearQueue();
		List<HttpAddress> addressList = new ArrayList<HttpAddress>();
		String[] infos = addresses.split("\\|");
		String[] adds = Toolkit.trims(infos[0]).split(";");
		String[] taradds = Toolkit.trims(infos[1]).split(";");
		String[] weights = Toolkit.trims(infos[2]).split(";");
		if(weights.length < (adds.length + taradds.length)){
			String[] newweights = new String[adds.length + taradds.length];
			for(int i=0; i<newweights.length; i++){
				if(i >= weights.length){
					newweights[i] = "1";
				} else {
					if(Toolkit.isNotBlank(weights[i].trim())&&Toolkit.isNumeric(weights[i].trim())){
						newweights[i] = weights[i];
					}
					else 
						newweights[i] = "1";
				}
			}
			weights = newweights;
		}
		//���ӳ�ʱĬ��10��
		int connectTimeout = (infos.length > 3 && Toolkit.isNotEmpty(infos[3])) ? Integer.parseInt(infos[3]) : 10*1000;
		//�ȴ���ʱĬ��30��
		int readTimeout = (infos.length > 4 && Toolkit.isNotEmpty(infos[4])) ? Integer.parseInt(infos[4]) : 30*1000;
		//���ȴ���ʱ����(���������ͣN������)��Ĭ��30
		int maxerrortimes = (infos.length > 5 && Toolkit.isNotEmpty(infos[5])) ? Integer.parseInt(infos[5]) : 30;
		//����ȴ���ʱʱ������, �����errorCycle�����ڵȴ���ʱ��������maxerrortimes�Σ�����ͣ��ַʹ��
		long errorCycle = (infos.length > 6 && Toolkit.isNotEmpty(infos[6])) ? Long.parseLong(infos[6]) : 30;
		long stopCycle = (infos.length > 7 && Toolkit.isNotEmpty(infos[7])) ? Long.parseLong(infos[7]) : 30;
		
		int index = 0;
		int totalWeight = 0;
		for(String ad: adds){
			for(String taradd: taradds){
				HttpAddress pd = new HttpAddress();
				pd.setAddress(ad);
				pd.setConnectTimeout(connectTimeout);
				pd.setDistributeWeight(Integer.parseInt(weights[index]));
				pd.setErrorCycle(errorCycle);
				pd.setMaxerrortimes(maxerrortimes);
				pd.setReadTimeout(readTimeout);
				pd.setStopCycle(stopCycle);
				pd.setTargetAddress(taradd);
				addressList.add(pd);
				totalWeight += pd.getDistributeWeight();
				index++;
			}
		}
		HttpAddressQueue queue = new HttpAddressQueue(addressList);
		queue.setTotalWeight(totalWeight);
		proxyAddressMap.put(addresses, queue);
		return queue;
		
	}
	
	protected HttpResult httpPost(String addresses, byte[] data, Map<String, String> headers){
		return this.httpSendRouter("POST", addresses, "", data, headers);
	}
	protected HttpResult httpPost(String addresses, String extraparams, byte[] data, Map<String, String> headers){
		return this.httpSendRouter("POST", addresses, extraparams, data, headers);
	}
	protected HttpResult httpGet(String addresses, String data, Map<String, String> headers){
		try {
			return this.httpSendRouter("GET", addresses, data, "".getBytes(), headers);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private boolean isNotSend(int httpStatus){
		return (httpStatus == 408
				|| httpStatus == HttpURLConnection.HTTP_BAD_REQUEST
				|| httpStatus == HttpURLConnection.HTTP_BAD_GATEWAY);
	}
	private void aftermath(HttpAddressQueue queue, HttpAddress add, HttpResult result){
		if(queue.getWeight() <= 0){
			queue.weightReset();
			add.weightReset();
			for(HttpAddress ha: queue.getAddressList()){
				ha.weightReset();
			}
		}
		if(result.getHttpStatus() == HttpURLConnection.HTTP_OK){
			add.active();
		} else if(this.isNotSend(result.getHttpStatus())){
			add.stop();
			logger.info(result.getHttpStatus() + " HttpAddress.stop() StopTimes=" + add.getStopTimes());
		} else {
			add.errorOne();
			logger.info(result.getHttpStatus() + " HttpAddress.errorOne() ErrorTimes=" + add.getErrorTimes() + " StopTimes=" + add.getStopTimes());
		}
	}
	/**
	 * HTTP POST ����
	 * @param addresses
	 * ��ʽ�� �����ַ1;�����ַ2;......|Ŀ���ַ1;Ŀ���ַ2......|Ȩ��1;Ȩ��2;Ȩ��3;Ȩ��4......|���ӳ�ʱ(��λ����)|��ȡ�ȴ���ʱ(��λ����)|���ȴ���ʱ����(���������ͣN������)|����ȴ���ʱʱ������|��ͣ����
	 * @param extraparams ��������
	 * @param data        ��������
	 * @param headers     ����http request header
	 * @return HttpResult
	 */
	private HttpResult httpSendRouter(String method, String addresses, String extraparams, byte[] data, Map<String, String> headers){
		proxyType = method;
		HttpResult result = new HttpResult();
		HttpAddressQueue queue = null;
		try {
			synchronized(this.proxyAddressMap){
				queue = proxyAddressMap.get(addresses);
				if(null == proxyAddressMap.get(addresses)){
					queue = fillAddress(addresses);
				}
			}
			if(null == queue){
				result.setHttpStatus(HttpURLConnection.HTTP_BAD_REQUEST);
				result.setHttpMessage(proxyType+" No router address");
				logger.info(result.getHttpMessage());
				return result;
			}
			boolean send = false;
			queue.setLastUseTime(System.currentTimeMillis());
			for(HttpAddress add: queue.getAddressList()){
				if(add.getWeight() > 0 && add.isAvailable()){
					send = true;
					add.setLastTryTime(System.currentTimeMillis());
					add.weightOneReduce();
					queue.weightOneReduce();
					result = this.httpSend(method, add, headers, "", extraparams, data, result);
					this.aftermath(queue, add, result);
					if(result.getHttpStatus() == HttpURLConnection.HTTP_OK 
							|| this.isNotSend(result.getHttpStatus())){
						break;
					}
				}
			}
			if(!send){
				HttpAddress add = null;
				add = queue.getAddressList().get(0);
				for(int i=1; i<queue.getAddressList().size(); i++){
					if(queue.getAddressList().get(i).getLastTryTime() < add.getLastTryTime()){
						add = queue.getAddressList().get(i);
					}
				}
				logger.info(proxyType+" RouterAddress get target min lasttrytime.");
				if(null != add){
					add.setLastTryTime(System.currentTimeMillis());
					add.weightOneReduce();
					queue.weightOneReduce();
					result = this.httpSend(method, add, headers, "", extraparams, data, result);
					this.aftermath(queue, add, result);
				} else {
					result.setHttpStatus(HttpURLConnection.HTTP_BAD_REQUEST);
					result.setHttpMessage(proxyType+" No Available address");
					logger.info(result.getHttpMessage());
				}
			}
		} catch(Exception e){
			logger.error(proxyType + " httpSendRouter", e);
		}
		logger.info(proxyType + " httpSendRouter " + result);
		return result;
	}
	/**
	 * ����http����
	 * @param method
	 * @param add
	 * @param headers
	 * @param charset
	 * @param data
	 * @param result
	 * 			result.httpStatus 	408 ���ӳ�ʱ��δ��������ת��ַ/Ŀ���ַ
	 *                              400 �������ӣ���ת����Ŀ���ַ������������쳣
	 *                              ���������Ѿ�����Ŀ���ַ
	 * 								
	 * @return
	 */
	private HttpResult httpSend(String method, HttpAddress add, Map<String, String> headers, String charset, String extraparams, byte[] data, HttpResult result){
		HttpURLConnection connect = null;
		boolean isconnect = false;
		try {
			Proxy py = null;
			String url = add.getTargetAddress();
			if(Toolkit.isNotBlank(extraparams)){
				url += "?"+extraparams;
			}
			if(Toolkit.isNotBlank(add.getAddress())){
				String[] proxy = add.getAddress().split(":");
				if(proxy.length > 1){
					InetSocketAddress addr = new InetSocketAddress(proxy[0], Integer.parseInt(proxy[1]));
					py = new Proxy(Proxy.Type.HTTP, addr);
				}
			}
        	if(url.toLowerCase().startsWith("https")){
        		connect = new SslConnection().openConnection(py, url, add.getSslVersion());
        	} else {
        		URL u = new URL(url);
        		if(null != py){
        			connect = (HttpURLConnection) u.openConnection(py);
        		} else {
        			connect = (HttpURLConnection) u.openConnection();
        		}
        	}
        	if(null != headers){
        		for(Map.Entry<String, String> header: headers.entrySet()){
        			connect.setRequestProperty(header.getKey(), header.getValue());
        		}
        	}
        	connect.setRequestMethod(method);
        	connect.setConnectTimeout(add.getConnectTimeout());
			connect.setReadTimeout(add.getReadTimeout());
			if("POST".equals(method)){
				connect.setDoOutput(true);
			} else {
				isconnect = true;
			}
			connect.setDoInput(true);
			connect.connect();
			isconnect = true;
			
			if("POST".equals(method)){
				connect.getOutputStream().write(data);
				connect.getOutputStream().flush();
				connect.getOutputStream().close();
			}
			result.setHttpStatus(connect.getResponseCode());
			result.setHttpMessage(connect.getResponseMessage());
			if(result.getHttpStatus() == HttpURLConnection.HTTP_OK){
				byte[] res = ToolUtil.readHttp(connect);
				connect.getInputStream().close();
				result.setData(res);
			}
		} catch (ConnectException e) {
			logger.error("",e);
			if(!isconnect){
				//408 Request Timeout
				result.setHttpStatus(408);
				result.setHttpMessage(e.getMessage());
			}
		} catch (SocketTimeoutException e) {
			logger.error("",e);
			if(!isconnect){
				//408 Request Timeout
				result.setHttpStatus(408);
				result.setHttpMessage(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("",e);
			if(!isconnect){
				//400 Bad Request
				result.setHttpStatus(HttpURLConnection.HTTP_BAD_REQUEST);
				result.setHttpMessage(e.getMessage());
			}
		} finally {
			if(null != connect){
				connect.disconnect();
			}
		}
		result.addTrace(add.getAddress()+"-->"+add.getTargetAddress()+": "+result.getHttpStatus() + " " + result.getHttpMessage());
		return result;
	}
	
}