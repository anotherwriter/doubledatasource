package com.example.demo.dao;

import com.example.db2.ChargingUserMapper;
import com.example.demo.model.db.ChargingUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ChargingUserDao {

    private final ChargingUserMapper chargingUserMapper;

    public List<ChargingUser> getChargingUserByPhoneOrMd5(String phone, String md5) {
        return chargingUserMapper.getChargingUserByPhoneOrMd5(phone, md5);
    }

    public ChargingUser getChargingUserByMd5(String md5) {
        return chargingUserMapper.getChargingUserByMd5(md5);
    }

    public ChargingUser getChargingUserByPhone(String phone) {
        return chargingUserMapper.getChargingUserByPhone(phone);
    }

    public int insertChargingUser(ChargingUser chargingUser) {
        return chargingUserMapper.insertChargingUser(chargingUser);
    }

    public int updateChargingUserStatusByPhone(String phone, int status) {
        return chargingUserMapper.updateChargingUserStatusByPhone(phone, status);
    }

}
