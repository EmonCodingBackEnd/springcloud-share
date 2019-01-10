/*
 * 文件名称：TokenResponse.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190111 04:04
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190111-01         Rushing0711     M201901110404 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ishanshan.gateway.api.GatewayResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse extends GatewayResponse {

    private static final long serialVersionUID = 8699939359290158519L;

    private String token;
}
