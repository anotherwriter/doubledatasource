package com.example.db2

import com.example.demo.model.db.ChargingUser
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

interface ChargingUserMapper {

    @Select('''
        SELECT
            id,
            xuid,
            `md5`,
            phone,
            charge_status
        FROM
            charging_user
        WHERE
            id = #{id}
    ''')
    ChargingUser getChargingUserById(@Param("id") long id)

    @Select('''
        SELECT
            id,
            xuid,
            `md5`,
            phone,
            charge_status
        FROM
            charging_user
        WHERE
            `md5` = #{md5}
    ''')
    ChargingUser getChargingUserByMd5(@Param("md5") String m5d)

    @Select('''
        SELECT
            id,
            xuid,
            `md5`,
            phone,
            charge_status
        FROM
            charging_user
        WHERE
            phone = #{phone}
    ''')
    ChargingUser getChargingUserByPhone(@Param("phone") String phone)

    @Select('''
        SELECT
            id,
            xuid,
            `md5`,
            phone,
            charge_status
        FROM
            charging_user
        WHERE
            `md5` = #{md5}
            OR
            phone = #{phone}
    ''')
    List<ChargingUser> getChargingUserByPhoneOrMd5(@Param("phone") String phone, @Param("md5") String md5)

    @Insert('''
        INSERT IGNORE INTO
            charging_user
        (
            xuid,
            `md5`,
            phone,
            charge_status,
            create_time
        )
        VALUES
        (
            #{xuid},
            #{md5},
            #{phone},
            #{chargeStatus},
            NOW()
        )
    ''')
    @Options(useGeneratedKeys = true)
    int insertChargingUser(ChargingUser chargingUser)

    @Update('''
        UPDATE
            charging_user
        SET
            charge_status=#{chargeStatus},
        WHERE
            id = @{id}
    ''')
    int updateChargingUserStatus(@Param("id") long id, @Param("chargeStatus") int status)

    @Update('''
        UPDATE
            charging_user
        SET
            charge_status=#{chargeStatus},
        WHERE
            phone = #{phone}
    ''')
    int updateChargingUserStatusByPhone(@Param("phone") String phone, @Param("chargeStatus") int status)
}