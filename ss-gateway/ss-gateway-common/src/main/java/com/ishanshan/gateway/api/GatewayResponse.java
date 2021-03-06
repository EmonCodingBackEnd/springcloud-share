/*
 * 文件名称：GatewayResponse.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181111 09:38
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181111-01         Rushing0711     M201811110938 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.api;

import com.ishansha.api.AppResponse;

import java.io.Serializable;

public class GatewayResponse<T extends Serializable> extends AppResponse<T> {

    private static final long serialVersionUID = 5005298608206740834L;

    public GatewayResponse() {
        this.errorCode = 0;
    }
}
