/*
 * 文件名称：AuthResponse.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 20:03
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901102003 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.auth;

import com.ishanshan.gateway.api.GatewayResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 获取用户授权信息的应答.
 *
 * <p>创建时间: <font style="color:#00FFFF">20190110 23:16</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthResponse<T extends Serializable> extends GatewayResponse<T> {

    private static final long serialVersionUID = -1616421716930756640L;

    /** 根据用户的token，换取的用户详情. */
    private AuthDetail authDetail;
}
