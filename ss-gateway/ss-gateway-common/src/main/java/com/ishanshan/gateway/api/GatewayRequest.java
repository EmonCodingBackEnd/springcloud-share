/*
 * 文件名称：GatewayRequest.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181111 09:19
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181111-01         Rushing0711     M201811110919 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.api;

import com.ishansha.api.AppRequest;
import com.ishanshan.gateway.auth.AuthDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GatewayRequest<T extends GatewayResponse> extends AppRequest<T> {

    private static final long serialVersionUID = 6355146217352098846L;

    /** 前端请求，经过网关后会被塞入权限信息，在对应服务端解析出结果. */
    private AuthDetail authDetail;
}
