/*
 * 文件名称：AppRequest.java
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
package com.ishanshan.apigateway.api;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class AppRequest implements Serializable {

    private static final long serialVersionUID = 6386500729119806662L;

    /** 请求ID用作幂等性校验 */
    protected String requestId;
}
