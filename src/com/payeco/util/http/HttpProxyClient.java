package com.payeco.util.http;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * @author luoyunqiu
 * @time 2016-09-12 20:54
 */
public class HttpProxyClient {
	
	private static Map<String, HttpProxyRouter> routers = Collections.synchronizedMap(new HashMap<String, HttpProxyRouter>());
	
	public static void main(String[] args) {
		try {
			//String url = "|http://10.123.76.51:8080/JavaDemo/notify"+"||10000|30000|3|20000|60000";
			//String url = "|http://10.123.76.51:8080/payecodemo/third/OrderServerWeb"+"||10000|30000|3|20000|60000";
			//String url = "|http://test.payeco.com:9080/payecodemo/third/OrderServerWeb"+"||10000|30000|3|20000|60000";
			String url = "|http://localhost:8080/payment/hicardWxAliResUrl"+"||10000|30000|3|20000|60000";
			//String respContent = "{\"version\":\"V003\",\"hicardMerchNo\":\"000000000000001\",\"payType\":\"011\",\"merchOrderNo\":\"152017053000022068\",\"hicardOrderNo\":\"g695468181568884736\",\"amount\":\"1\",\"createTime\":\"2017-05-30 18:37:24\",\"payTime\":\"2017-05-30 18:40:04\",\"respCode\":\"00\",\"respMsg\":\"成功\",\"cardNo\":\"\",\"sign\":\"6e223fa2fadc37acdc3476aa942802f6\",\"organNo\":\"44015862\",\"buyerId\":\"oHmbkt04kdqtaTCoSqCh5fSoh2hQ\",\"isCredit\":\"\",\"remark\":\"测试\",\"channelOrderNo\":\"7551000001201705301138534335\"}";
			String respContent = "{\"version\":\"V003\",\"hicardMerchNo\":\"000000000000001\",\"payType\":\"011\",\"merchOrderNo\":\"152017060700022091\",\"hicardOrderNo\":\"g698447207870763008\",\"amount\":\"1\",\"createTime\":\"2017-06-07 23:54:59\",\"payTime\":\"2017-06-07 23:53:30\",\"respCode\":\"00\",\"respMsg\":\"成功\",\"cardNo\":\"\",\"sign\":\"c8d93263b6c7b6d6783b012fcfd6831d\",\"organNo\":\"44015862\",\"buyerId\":\"o_1_gv_HEk8trwJEd907dC9n3klA\",\"isCredit\":\"2\",\"remark\":\"测试\",\"channelOrderNo\":\"100000341571\"}";

			String resp =(String) httpPost(url, respContent.getBytes("GBK"), null).getData().toString();
			System.out.println("resp:"+resp);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static HttpProxyRouter getRouter(String clientName){
		synchronized(routers){
			HttpProxyRouter router = routers.get(clientName);
			if(null == router){
				router = new HttpProxyRouter();
				routers.put(clientName, router);
			} else {
				router.setLastUserTime(System.currentTimeMillis());
			}
			clearUnUseRouter();
			return router;
		}
	}
	private static void clearUnUseRouter(){
		Iterator<Map.Entry<String, HttpProxyRouter>> iterator = routers.entrySet().iterator();
		long max = 1000L * 60 * 60;
		while(iterator.hasNext()){
			Map.Entry<String, HttpProxyRouter> entity = iterator.next();
			long dis = System.currentTimeMillis() - entity.getValue().getLastUserTime();
			if(dis > max){
				iterator.remove();
			}
		}
	}
	/**
	 * 
	 * @param addresses 地址格式：
	 *  代理地址1;代理地址2;......|目标地址1;目标地址2......|权重1;权重2;权重3;权重4......|连接超时(单位毫秒)|读取等待超时(单位毫秒)|最大等待超时次数(超过后会暂停N个周期)|计算等待超时时间周期|暂停周期
	 *  权重1=代理地址1--目标地址1
		权重2=代理地址1--目标地址2
		权重3=代理地址2--目标地址1
		权重4=代理地址2--目标地址2
		如果不配置权重填空，则默认都为1:1:1:1......
	 * @param data 数据
	 * @param headers 特殊要求的http请求头
	 * @return
	 */
	public static HttpResult httpPost(String addresses, byte[] data, Map<String, String> headers){
		return getRouter(addresses).httpPost(addresses, data, headers);
	}
	public static HttpResult httpPost(String method, String addresses, String extraparams, byte[] data, Map<String, String> headers){
		return getRouter(addresses).httpPost(addresses, extraparams, data, headers);
	}
	
	public static HttpResult httpGet(String addresses, String data, Map<String, String> headers){
		return getRouter(addresses).httpGet(addresses, data, headers);
	}
}