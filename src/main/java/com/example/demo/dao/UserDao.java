package com.example.demo.dao;

import com.example.db1.UserMapper;
import com.example.db2.GoodsMapper;
import com.example.db2.UserInfoMapper;
import com.example.demo.annotation.MediaStatAnnotation;
import com.example.demo.annotation.StatAnnotation;
import com.example.demo.log.property.BaseStatProperty;
import com.example.demo.model.db.Goods;
import com.example.demo.model.db.User;
import com.example.demo.model.db.UserInfo;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserDao {
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;
    private final GoodsMapper goodsMapper;

    @Value("${loadingCache.user.ttl_minute:10}")
    private int userCacheTTL;

    @Value("${loadingCache.aifeed.max_size:997}")
    private int userCacheMaxSize;

    private LoadingCache<Integer, User> loadingCache;

    private class MyLoadCache extends CacheLoader<Integer, User> {

        @Override
        public User load(Integer key) throws Exception {
            return null;
        }
    }

    @PostConstruct
    private void init(){
        loadingCache = CacheBuilder.newBuilder().expireAfterWrite(userCacheTTL, TimeUnit.MINUTES)
                .maximumSize(userCacheMaxSize).build(new MyLoadCache());
    }

    public User getUserById(int id){
        User cacheUser = null;
        try {
            cacheUser = loadingCache.getIfPresent(id);
        } catch (Exception e) {
            System.out.println("??????????????????????????????????");
            e.printStackTrace();
        }

        if(null != cacheUser){
            log.info("this user({}) is in loadingCache", id);
        } else {
            log.info("this user({}) is not in loadingCache", id);
            cacheUser = userMapper.getUser(id);
            loadingCache.put(id, cacheUser);
        }

        return cacheUser;
    }
    @MediaStatAnnotation(beanName = "userInfoProperty")
    public List<Goods> getGoodsById(int userId){
        List<Goods> goodsList = null;
        goodsList = goodsMapper.getGoods(userId);

        return goodsList;
    }


    public UserInfo getUserInfoById(int id){
        UserInfo userInfo = null;
        userInfo = userInfoMapper.getUserInfo(id);

        return userInfo;
    }



}
