package com.example.demo.test;


import java.util.EnumSet;
import java.util.Set;

import static java.util.EnumSet.of;

/**
 * Created by donghao04 on 2017/7/28.
 */

public class Main {

    public static void main(String[] args){

       ClientPageType clientPageType = ClientPageType.valueOf("SEARCH");
       System.out.println(clientPageType.getOrderByString());

       String errorMsg = ErrorType.getErrorMsgByErrorCode(0);
       System.out.println(errorMsg);


    }

}
