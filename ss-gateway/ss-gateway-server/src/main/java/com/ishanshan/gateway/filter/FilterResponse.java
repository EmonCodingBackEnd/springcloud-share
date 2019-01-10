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
package com.ishanshan.gateway.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ishanshan.gateway.api.GatewayResponse;
import com.ishanshan.gateway.exception.GatewayStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterResponse extends GatewayResponse {

    private static final long serialVersionUID = 228747756203068857L;
    /** 用户编号. */
    protected String userId;

    public static FilterResponse getAuthResponse(GatewayStatus gatewayStatus) {
        FilterResponse authResponse = new FilterResponse();
        if (gatewayStatus != null) {
            authResponse.setErrorCode(gatewayStatus.getCode());
            authResponse.setErrorMessage(gatewayStatus.getMessage());
        }
        return authResponse;
    }
}
