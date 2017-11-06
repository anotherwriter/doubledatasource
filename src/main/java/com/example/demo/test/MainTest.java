package com.example.demo.test;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class MainTest {

    public static void main(String[] args){

//        Date date = new Date(10234567891234567L);
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        System.out.println(df.format(date));

        Random random = new Random();
        int randomNum = random.nextInt(9999) + 1;
        System.out.println(String.format("%04d", randomNum));

        String testmd5 = "w";

        System.out.println(Tools.MD5(testmd5));

        try {
            int i = 10 / 0;   // 抛出 Exception，后续处理被拒绝
            System.out.println("i vaule is : " + i);
        } catch (Exception e) {
            System.out.println(" -- Exception :" + e);
        }

        long userId = 2500394669L; // 2803628248L (another_writer) 699267942L(重雨0)
        String userName = "%76%72%5F%65%64%75%63%61%74%69%6F%6E";//%61%6E%6F%74%68%65%72%5F%77%72%69%74%65%72 (another_writer) %D6%D8%D3%EA%30(重雨0)
        System.out.println(userName);
        try {
            userName = URLDecoder.decode(userName, "gb2312"); // UTF-8
            System.out.println(userName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            userName = "";
        }

        String encryptStr = Tools.cryptEncode(userId, userName);

        String url = String.format("http://himg.bdimg.com/sys/portrait/item/%s.jpg", encryptStr); // 66fbe9878de99ba830ad29
        System.out.println(url);

        Long threshold = 200L;

        Long result = threshold << 20;
        System.out.println(result/1024/1024);

        //System.out.println(Tools.MD5("abcde123"));
    }
}

class Tools {

    private static ByteBuffer buffer = ByteBuffer.allocate(8);


    public static long[] longToArray(long s) {
        long[] longArray = new long[4];
        longArray[0] = s & 0x000000ff;
        longArray[1] = (s & 0x0000ff00) >> 8;
        longArray[2] = (s & 0x00ff0000) >> 16;
        longArray[3] = (s >> 24) & 0x000000ff;

        return longArray;
    }

    public static String cryptEncode(long uid, String userName) {

        String strChars = "0123456789abcdef";
        long[] longArray = longToArray(uid);
        byte[] userNameByteArray = userName.getBytes();

        String strCode = "";
        strCode = strCode + strChars.charAt((int)(longArray[0] >> 4)) +  strChars.charAt((int)(longArray[0] & 15));
        System.out.println(strCode);
        strCode = strCode + strChars.charAt((int)(longArray[1] >> 4)) +  strChars.charAt((int)(longArray[1] & 15));
        System.out.println(strCode);

        for (int i = 0; i < userNameByteArray.length; i++) {

            int value = userNameByteArray[i] & 0xFF;
            //System.out.println("char:" + userNameByteArray[i] + " value:" + value +" value >> 4 = "  + (value >> 4));
            strCode = strCode + strChars.charAt(value >> 4) + strChars.charAt(value & 15);
        }

        strCode = strCode + strChars.charAt((int)(longArray[2] >> 4)) +  strChars.charAt((int)(longArray[2] & 15));
        strCode = strCode + strChars.charAt((int)(longArray[3] >> 4)) +  strChars.charAt((int)(longArray[3] & 15));

        return strCode;
    }



    public static byte[] longToByteArray(long s) {
        buffer.putLong(s);
        byte[] byteArray = buffer.array();
        byte[] finalByteArray = new byte[4];
        for (int i = 0; i < finalByteArray.length; i++) {
            finalByteArray[i] = byteArray[i + 4];
        }
        return finalByteArray;
    }


    public static String ucryptEncode(long userId, String username) {

        System.out.println(Long.toBinaryString(userId));

        byte[] userIdByte = longToByteArray(userId);
        String userIdStr = new String(userIdByte);

        for (int i = 0; i < userIdByte.length; i++) {
            System.out.print(String.valueOf(userIdByte[i]) +" ");
        }
        System.out.println("\nbyte size:" + userIdByte.length);

        username = userIdStr.charAt(0) + userIdStr.charAt(1) + username + userIdStr.charAt(2) + userIdStr.charAt(3);
        String codeStr = "";

        for (int i = 0; i < username.length(); i++) {
            int intValue = username.charAt(i);
            int intHead = intValue >>> 4;
            int intTail = intValue ^ (intHead << 4);
            codeStr = (codeStr + Integer.toHexString(intHead) + Integer.toHexString(intTail));
        }

        return codeStr;
    }

    public static String MD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashText = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static long MD5edLong(String input) {
        String str = MD5(input);
        return Long.parseLong(str.substring(0, 8), 16);
    }

    public static String removeUTF3BytesLonger(String s) {
        return s.replaceAll("[^\\u0000-\\uffff]", "");
    }
}
