package com.payeco.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class TripleDes {
	
	public static void main(String[] args) {
		String key = "1234567890qwertyuiopasdf";
		String src = "abcdef";
		byte[] srcEnc;
		try {
			srcEnc = TripleDes.encrypt(key.getBytes("UTF-8"), src.getBytes("UTF-8"));
	        String srcEncrypt = Toolkit.base64Encode(srcEnc);
	        System.out.println("srcEncrypt:\n"+srcEncrypt+"\n");
	        
	        //String tempEnc = "ejZSwzA+m+2CbxMBZOOSMoEZOPTCGmL/07FMUh2aANKo7ccIo6tV3yTY/6XCRGu4OEmaQmSi727PHAF/YsR2cKohaI9Mvx56b9HjxQgp+q0HOA9XCqqHf1HgQQcRwGwfxyeNmZeLKtUWoQ8Noj84UkTelTMsoyh6TYNl6Eh6wuQ6H2Ad0uBgxP5CUcKgUU5dJPwatlF0qF6gNNydEGqJNV4DPp7WjMqkRKOfuKGxZkT8kLeuBgtPS035qg1yDBkGk+4DLCOUW2FQ1PV7gp7NaPshS9LCT7JRkSCvv1biqBlV9vKQ5/CTWiQTQEtLVZMVHoCmQERNtzBqq5YAoCN4JbAQRo9IT8lS34BfSlkiEfyBig+FD32s47lLHkaAxyJ1Nbh09wyBUTpV+zUMKVBzePERjzfazjuUseRoN7T6zxcUeR4EQQ8wGWHbjo/bYN14MVo47m65lsuaAoWPJphTzgouTUd83Lu2idU5h1RkpzJde0mNL+EXMbPy8DjcyOPYWm79+RpCNvN4AF4+lQoLsMgVE6VZ/s1K/yB9f5T2dlul4U6PpF/COi5A30UCmYsYBCVJwtMYj8vtjKA4OpioyfOFKiU321tsWkXzmqzCuRhPy21psmGUfzVdiv+Ov4ZIglP0UGWmIeWlLb3eAabigOh0B6W/RRY9hOAsRsE8vzixIsCziAQlScLTGI/KH/uCHOSRxLQK+BuRk5+MfyU6vM8KC49YPkS7GlC7wAbEqce6uocdv+f0XjqUgTN7Rfplit7XMgYDHy3Wk+NRUX7hoUBvZkBcH7/t9oH7sBC1NXbU2uluofPiV937VE7lSTdvOhEyLqqCDj/z30KBvHA2f00W9vfe6R2gH8FwhkdBGoj/pSm7bsCymm25GAEV9CSVopdXN59Zmmg=";
	        //String tempEnc = "Msoyh6TYNl6Eh6wuQ6H2Ad0uBgxP5CUcKgUU5dJPwatlF0qF6gNNydEGqJNV4DPp7WjMqkRKOfuKGxZkT8kLeuBgtPS035qg1yDBkGk+4DLCOUW2FQ1PV7gp7NaPshS9LCT7JRkSCvv1biqBlV9vKQ5/CTWiQTQEtLVZMVHoCmQERNtzBqq5YAoCN4JbAQRo9IT8lS34BfSlkiEfyBig+FD32s47lLHkaAxyJ1Nbh09wyBUTpV+zUMKVBzePERjzfazjuUseRoN7T6zxcUeR4EQQ8wGWHbjo/bYN14MVo47m65lsuaAoWPJphTzgouTUd83Lu2idU5h1RkpzJde0mNL+EXMbPy8DjcyOPYWm79+RpCNvN4AF4+lQoLsMgVE6VZ/s1K/yB9f5T2dlul4U6PpF/COi5A30UCmYsYBCVJwtMYj8vtjKA4OpioyfOFKiU321tsWkXzmqzCuRhPy21psmGUfzVdiv+Ov4ZIglP0UGWmIeWlLb3eAabigOh0B6W/RRY9hOAsRsE8vzixIsCziAQlScLTGI/KH/uCHOSRxLQK+BuRk5+MfyU6vM8KC49YPkS7GlC7wAbEqce6uocdv+f0XjqUgTN7Rfplit7XMgYDHy3Wk+NRUX7hoUBvZkBcH7/t9oH7sBC1NXbU2uluofPiV937VE7lSTdvOhEyLqqCDj/z30KBvHA2f00W9vfe6R2gH8FwhkdBGoj/pSm7bsCymm25GAEV9CSVopdXN59Zmmg=";
	        String tempEnc = "ejZSwzA+m+r2CrbxMBZOOSMoEZOPTCGmL/07nFMUh2bXuSAxjefNXjWkGwunUi2okp0wpoG+J5iUgU10QjaDPJeVBafniheraT+FVMhT76gVKKRd2bRgCJSBTXRCNoM86tqLY1Xz8NoXsflhqQXJdvy0tuLInU5tB3S4GDE/msIWn4/sKxmPQblrA/zQbb+G03J0Qaok2dUbj8mb42xxX9N3OUHj3tnPXhWCK/xKQQiLKdxet+KuvsaEGnOz6teH/j7E8E5ftFCR7oYb1a+PV1uUO/Xom+zM4U5JL3VKYmFJNWt9qPSsduj2Ob9zO+Whful/T2eRfDnan9hjc+lIftG0rUFDJsWcKUbYMkG6AiPCA2tntWgu6X22RKD0YQldSKsa+oVYCHOx0I25jg9JSvfujIITTsg0/YHr2Dv6K1NIqxr6hVgIcxP7MO5gR0jvo2GDB+d/C3ZofNiv8RrW3qNhgwfnfwt2ngHslqDluyinF3v4GE64bHEwzXdrmF1R/25TjfU6Y+bqNvuE8ZcDJXb5TZkNmhSSciJnMwGggzP37oyCE07INEOAA2wzfKYHsWkcGcVGPAuW/b4zlPsjtb4LonTechnQaXodYojv9K671PqJaycfRpUXEsEluLIuYNcyXm6jBRkKyqAAi7PR27rivFmuJtDDDaC0N74KvThB7tEitcic9X1dn9NMC11S7nhkUCqyTnG+C6J03nIZ0PNXjA3Hzri50AtzGYsKOlQAoVXJGSJx/hSRxAX5OAUaQxUuwc1Mba1u4VD1A/9Ggcn6iZJ3AtoigqoYPJSExaYqbSFla2SA33veMOG+FFNab+yjVIEiwLXLobus2EIoqXYrqZ7nE57EuuJckf4Ihnoq6r72tibjEHkqZDATs4ByzwrmqnVSwzqVdgBZmvHitxruDhJmkJkoyG5csMVwQqc=";
	        byte[] EncByte = Toolkit.base64Decode(tempEnc);
	        byte[] srcDec = TripleDes.decrypt(key.getBytes("UTF-8"), EncByte);
	        String srcDecrypt = new String(srcDec,"utf-8");
	        System.out.println("srcDecrypt:\n"+srcDecrypt+"\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    private static final String Algorithm = "DESede"; 
    public static byte[] encrypt(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] bts = c1.doFinal(src);
            return bts;
        } catch (java.lang.Exception e3) {
        	e3.printStackTrace();
        }
        return null;
    }
    
    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decrypt(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            byte[] bts = c1.doFinal(src);
            return bts;
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

   
}