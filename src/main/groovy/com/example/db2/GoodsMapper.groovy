package com.example.db2

import com.example.demo.model.db.Goods
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface GoodsMapper {
    @Select('''
        SELECT
            id,
            goods_name,
            goods_desc,
            user_id
        FROM
            goods
        WHERE
            user_id = #{userId}
    ''')
    List<Goods> getGoods(@Param("userId") int userId)
}
