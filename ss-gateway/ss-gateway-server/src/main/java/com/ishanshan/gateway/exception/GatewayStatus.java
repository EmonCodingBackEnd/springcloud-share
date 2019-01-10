/*
 * 文件名称：EdenStatus.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20180806 09:58
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20180806-01         Rushing0711     M201808060958 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.exception;

import lombok.Getter;

@Getter
public enum GatewayStatus {
    SUCCESS(0, "成功"),

    GATEWAY_ERROR(990000, "网关异常"),

    GATEWAY_PRE_ERROR(990100, "限流控制"),
    GATEWAY_PRE_REQUEST_COUNT_LIMIT(990101, "系统繁忙，请稍后重试"),
    GATEWAY_PRE_BAD_REQUEST(990102, "请求地址不合法"),
    GATEWAY_PRE_TOKEN_NOT_FOUND(990103, "token not found!"),
    GATEWAY_PRE_TOKEN_INVALID(990104, "token invalid"),
    GATEWAY_PRE_TOKEN_EXPIRED(990105, "token expired"),

    GATEWAY_ROUTE_ERROR(990200, "路由过滤异常"),

    GATEWAY_POST_ERROR(990300, "后置过滤异常"),

    GATEWAY_ERROR_FILTER_ERROR(990100, "错误过滤异常"),
    ;

    private Integer code;

    private String message;

    GatewayStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}