package com.jxtb.sdk.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jxtb on 2019/6/19.
 */
public class MD5Util {

    private static final MessageDigest md5 = getDigest("MD5");

    public MD5Util(){

    }

    public static byte[] md5(byte[] data){
        return md5.digest(data);
    }

    public static byte[] md5(String data){
        return md5(data.getBytes(Charset.forName("UTF-8")));
    }

    public static String md5Hex(byte[] data){
        return HexUtils.encodeHexString(md5(data));
    }

    public static String md5Hex(String data){
        return HexUtils.encodeHexString(md5(data));
    }

    private static MessageDigest getDigest(String algorithm) {
       try{
           return MessageDigest.getInstance(algorithm);
       }catch (NoSuchAlgorithmException e){
           throw new IllegalArgumentException(e);
       }
    }


}
