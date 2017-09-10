package com.example.demo.test;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainTest {

    public static void main(String[] args){
        Long threshold = 200L;

        Long result = threshold << 20;
        System.out.println(result/1024/1024);
        String s = "ceshi";
        if(s.equals(null)){
            System.out.println("it's null");
        }else {
            System.out.println("it's not null");
        }

        Object object = s;

        System.out.println(object);
        System.out.println(Tools.MD5(""));
    }
}

class Tools {
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
        return s.replaceAll("[^\\u0000-\\uFFFF]", " ");
    }
}
