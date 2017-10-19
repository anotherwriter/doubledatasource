package com.example.demo.test;

import lombok.Data;

@Data
public class PassportRsp {

    private Integer status; // 代表含义请参照PassortStatusCommon

    private Integer need_reset_cookie; // 指示是否需要重置用户Cookie，每次请求都有效，1：需要重置，0：不需要重置

    private String session_id; // 可能的SessionID

    private Integer uid; // 可能的用户ID

    private Long created_time; // session创建时间

    private Long last_login_time; // 当前session的登录时间

    private Integer access_count; // 本次会话访问总次数

    private Long last_updated_time; // 最后更新时间

    private Long global_access_time; // 访问所有应用的最后时间

    private Long private_access_time; // 访问本应用的最后时间

    private Integer pwd_flag; // 是否记住密码：1为记住密码，0为未记住密码

    private Integer reserved; // 保留数据

    private String secureemail;

    private Long securemobil;

    private Integer risk_rank;

    private Integer risk_code;

    private String username; // 可能的用户名

    private String displayname;

    private String global_data; // 可能的需要修改的公共Session数据

    private String private_data; // 可能的私有Session数据

}
