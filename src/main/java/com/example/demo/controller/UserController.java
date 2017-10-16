package com.example.demo.controller;

import com.example.db2.UserInfoMapper;
import com.example.demo.annotation.StatAnnotation;
import com.example.demo.dao.ResourceDao;
import com.example.demo.dao.UserDao;
import com.example.demo.log.property.BaseStatProperty;
import com.example.demo.model.db.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by donghao04 on 2017/7/28.
 */
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserDao userDao;
    private final UserInfoMapper userInfoMapper;

    @Value("${zookeeper_servers:127.0.0.1:2180,127.0.0.1:2181,127.0.0.1:2182}")
    private String zookeeperServers;

    @Autowired
    private ResourceDao resourceDao;



    @RequestMapping(method = RequestMethod.GET, value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public String search(@RequestParam String keyword,
                         @RequestParam Map<String, String> params) {

        if (null == params)
            return "error";

        List<String> searchKeys = new ArrayList<>();
        for (String key : params.keySet()) {
            if (key.contains("keyword")) {
                String value = params.get(key);
                searchKeys.add(value);
            }
        }


        return "keyword=" + keyword + ", params=" + params + ",keywords=" + searchKeys;
    }



    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") int id) {
        //log.info("getUser id:{}",id);
        User user = userDao.getUserById(id);
        user.setUserInfo(getUserInfoByUserId(id));

        return user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/info")
    @ResponseStatus(HttpStatus.OK)
    @StatAnnotation(interval = 70*1000,name = "userInfo",className = "BaseStatProperty",beanName = "userInfoProperty",statProperty = BaseStatProperty.class)
    public UserInfo getUserInfo(@RequestParam(value = "infoId") Integer infoId,
                                @RequestParam(value = "ai",defaultValue = "aiai") String aaa) throws Exception {
        log.info("getUserInfo id:{}",infoId);
        UserInfo userInfo = userInfoMapper.getUserInfo(infoId);
        List<Goods> goodsList = userDao.getGoodsById(infoId);

        System.out.println(zookeeperServers);
        return userInfo;
    }

    private UserInfo getUserInfoByUserId(int id){
        UserInfo userInfo = userInfoMapper.getUserInfo(id);
        return userInfo;
    }

    @PreDestroy
    public void destory() {
        System.out.println("read to destory................");
    }

}
