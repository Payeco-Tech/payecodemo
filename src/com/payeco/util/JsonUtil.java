package com.payeco.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;



public class JsonUtil {
	
	public static void main(String[] args) {
		String str = "{\"Version\":\"2.1.0\",\"MerchantNo\":\"1472181543236\",\"Description\":\"Test\",\"SdkExtData\":\"{\\\"geelyUserId\\\":\\\"001221\\\", \\\"walletUserId\\\":\\\"10234\\\"}\",\"MerName\":\"收银台代理商\"}";
		System.out.println("str: \n"+str);
		Map<String, Object> map = (Map<String, Object>) jsonStr2Map(str);
		System.out.println("map.get(Version): "+map.get("Version"));
		System.out.println("map.get(SdkExtData): "+map.get("SdkExtData"));
	}
	
	/**
	 * 将json字符串转为对象
	 * @param jsonStr	json字符串
	 * @param cls		要转化的目的类对象
	 * @return
	 */
	public static <T> T json2Object(String jsonStr, Class<T> cls) {
		if(jsonStr==null || jsonStr.equals("")) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		T t = null;
		try {
			t = (T) mapper.readValue(jsonStr, cls);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static String object2Json(Object obj) {
		if(obj==null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
 
	/**
	 * json字符串转Map对象
	 * @param jsonStr
	 * @return
	 */
	public static Map<?,?> jsonStr2Map(String jsonStr) {
		if(jsonStr==null || jsonStr.equals("")) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<?, ?> map = null;
		try {
			map = mapper.readValue(jsonStr, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	} 
	
	/**
	 * Map对象转String字符串
	 * @param map
	 * @return
	 */
	@SuppressWarnings("finally")
	public static String map2JsonStr(Map<?, ?> map) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return jsonStr;
		}
	}
	
	/**
	 * json文件转字符串
	 * @param path
	 * @return
	 */
	public static String jsonFileToString(String path) {
		FileInputStream in = null;
		StringBuilder sb = new StringBuilder();
		byte[] buf = new byte[128];
		int buf_size;
		try {
			in = new FileInputStream(new File(path));
			while((buf_size=in.read(buf))!=-1){
				sb.append(readBuffer(buf,buf_size));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String readBuffer(byte[] buffer, int size){
		if(buffer==null)
			return null;
		if(buffer.length==0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<size;i++){
			sb.append((char)buffer[i]);		//转换成字符拼接成字符串
		}
		return sb.toString();
	}
	
	/**
	 * 未完善的方法，供测试输出使用
	 * @param map
	 * @return
	 */
	protected static String toString(Map<?, ?> map) {
		if(map==null) {
			return null;
		}
		Iterator<?> iterator = map.keySet().iterator(); 
		StringBuilder mapSb = new StringBuilder();
		while ( iterator.hasNext() ) {
			Object key = iterator.next();   
			mapSb.append(key+":");
			if(map.get(key) instanceof String) {
				mapSb.append(map.get(key).toString()+"\n");
			} else if(map.get(key) instanceof List){
				List l = (List)map.get(key);
				for(int i=0;i<l.size();i++) {
					mapSb.append(l.get(i)+"\n");
				}
			}
		}
		return mapSb.toString();
	}
	
}
