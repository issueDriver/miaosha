package com.zuojie.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author:zuojie
 */
public class MD5Util {
    //固定的盐
    private static  final String SALT="1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassFormPass(String inputPass){
      String str=""+ SALT.charAt(0)+SALT.charAt(2)+inputPass+SALT.charAt(5)+SALT.charAt(4);
      return md5(str);

    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbpass(String input ,String saltDB){
      String formPass=  inputPassFormPass(input);
      String dbPass=formPassToDBPass(formPass,saltDB);
      return  dbPass;
    }


    public static void main(String[] args) {
        //System.out.println(inputPassFormPass("123456"));
        //System.out.println(formPassToDBPass(inputPassFormPass("123456"),"1a2b3c4d"));
        System.out.println(inputPassToDbpass("123456","1a2b3c4d"));

    }

}
