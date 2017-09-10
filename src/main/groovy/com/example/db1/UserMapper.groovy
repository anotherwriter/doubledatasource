package com.example.db1

import com.example.demo.model.db.User
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select


/**
 * Created by donghao04 on 2017/7/28.
 */
interface UserMapper {

    @Select('''
        SELECT
            userId,
            userName,
            nickName
        FROM
            usertable
        WHERE
            userId = #{userId}
    ''')
    User getUser(@Param("userId") int userId)

}
