package com.payeco.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class TripleDes {

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