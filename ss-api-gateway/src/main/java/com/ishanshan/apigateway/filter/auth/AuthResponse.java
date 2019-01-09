/*
 * 文件名称：AuthResponse.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181106 15:44
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181106-01         Rushing0711     M201811061544 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ishanshan.apigateway.api.AppResponse;
import com.ishanshan.apigateway.exception.GatewayStatus;
import com.ishanshan.apigateway.serializer.Long2StringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse extends AppResponse {

    private static final long serialVersionUID = 6265601817348900418L;

    private String token;

    /** 用户编号. */
    @JsonSerialize(using = Long2StringSerializer.class)
    protected Long userId;

    public static AuthResponse getAuthResponse(GatewayStatus gatewayStatus) {
        AuthResponse authResponse = new AuthResponse();
        if (gatewayStatus != null) {
            authResponse.setErrorCode(gatewayStatus.getCode());
            authResponse.setErrorMessage(gatewayStatus.getMessage());
        }
        return authResponse;
    }
}
