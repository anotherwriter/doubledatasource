package com.example.demo.test;


import com.example.demo.model.db.User;
import com.example.demo.model.db.UserInfo;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTest {

    public static void main(String[] args){

        List<UserInfo> command = new ArrayList<>();

        UserInfo astr = new UserInfo(1, "1");
        UserInfo bstr = new UserInfo(2, "2");
        command.add(astr);
        command.add(bstr);

        UserInfo[] cmdArray = command.toArray(new UserInfo[command.size()]);
        cmdArray = cmdArray.clone();

        System.out.println(Arrays.toString(cmdArray));
        astr.setInfoId(3);
        astr.setInfo("3");
        System.out.println(Arrays.toString(cmdArray));

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
