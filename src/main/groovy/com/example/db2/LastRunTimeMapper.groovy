package com.example.db2

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface LastRunTimeMapper {

    @Select('''
        SELECT
            count(1)
        FROM
            last_run_time
        WHERE
            xuid_md5 = #{md5}
    ''')
    int getMd5Num(@Param("md5") String md5)
}