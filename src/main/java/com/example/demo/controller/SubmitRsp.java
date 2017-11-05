package com.example.demo.controller;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class SubmitRsp {
    /**
     * 0 发送成功
     * 1 产品不存在
     * 2 产品售完或已经下架
     * 3 网关通讯错误
     * 4 网关应答错误
     * 5 用户名密码不正确
     * 6 手机号码不正确或者不被支持
     * 7 预存余额不足
     * 8 提交数据格式不正确
     * 9 非法IP源
     * 10 其他问题
     * 11 签名验证不合法
     * 12 请填写正确的参数信息
     * 13 数据提交异常
     * 14 流量包或者手机号为空
     * 15 此业务暂未开通
     * 16 此号码为黑名单
     * 17 不支持的提交方式
     * 18 订单号为空
     */
    private String code;

    private String description;

    private String sendid; // 响应合作方流水号ID

    private String Requestid;

}
