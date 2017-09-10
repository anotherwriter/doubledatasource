package com.example.db2

import com.example.demo.model.db.UserInfo
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

/**
 * Created by donghao04 on 2017/7/28.
 */
interface UserInfoMapper {

    @Select('''
        SELECT
            infoId,
            info
        FROM
            userinfotable
        WHERE
            infoId = #{infoId}
    ''')
    UserInfo getUserInfo(@Param("infoId") int infoId)
}
