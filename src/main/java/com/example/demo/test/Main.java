package com.example.demo.test;


import com.example.demo.controller.TestReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        TestData testData = new TestData();
        testData.setName("t1");
        testData.setGender("m");
        testData.setName("cccc");

        System.out.println(testData);
        String testStr = "status=1&need_reset_cookie=0&session_id=&uid=0&created_time=0&last_login_time=0&access_count=0&last_updated_time=0&global_access_time=0&private_access_time=0&pwd_flag=0&reserved=0&secureemail=&securemobil=&risk_rank=0&risk_code=0&username=&displayname=&global_data=%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00&private_data=%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00%00";

        String[] keyValues = testStr.split("&");
        int i = 0;
        String jsonStr ="{";
        for (String keyValue : keyValues) {
            String[] tempArray = keyValue.split("=");
            if (null != tempArray && tempArray.length >= 2) {
                // set key
                jsonStr += "\"" + tempArray[0] + "\"";
                // check the value if the string
                Pattern pattern = Pattern.compile("^[0-9]*$");
                Matcher matcher = pattern.matcher(tempArray[1]);
                //tempArray[1].matches("^[0-9]*$")
                if (tempArray[1].matches("^[0-9]*$")) { // tempArray[1] is number
                    jsonStr += (":" + tempArray[1] +",");
                } else {
                    jsonStr += (":\"" + tempArray[1] +"\",");
                }
            }
        }
        char[] jsonChars = jsonStr.toCharArray();
        jsonChars[jsonChars.length - 1] = ' ';


        String realJsonStr = String.copyValueOf(jsonChars);

        realJsonStr += "}";

        System.out.println(realJsonStr);

        PassportRsp passportRsp = new PassportRsp();
        try {
            passportRsp = new ObjectMapper().readValue(realJsonStr, PassportRsp.class);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(passportRsp.getStatus());

        String str = "status=1&need_reset_cookie=0&session_id=&uid=0&created_time=0&last_login_time=0&access_count=0&last_updated_time=0&global_access_time=0&private_access_time=0&pwd_flag=0&reserved=0&secureemail=&securemobil=&risk_rank=0&risk_code=0&username=&displayname=&" +
                "global_data=%00&private_data=%00%0";
        if (null != str && str.contains("status")) {
            str = str.split("status")[0];
            System.out.println(str );
        }

    }



}

@Data
@ToString
class TestData {

    private String name;

    private String gender;

    private int age;

    public void setDbAge(int age) {
        this.age = age;
    }

    public int getDbAge() {
        return this.age;
    }
}
