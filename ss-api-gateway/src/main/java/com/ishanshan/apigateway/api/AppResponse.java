/*
 * 文件名称：AppResponse.java
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
package com.ishanshan.apigateway.api;

import com.ishanshan.apigateway.exception.GatewayStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppResponse<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -3455461468232581561L;

    /** 错误码. */
    protected Integer errorCode = GatewayStatus.SUCCESS.getCode();

    /** 提示信息. */
    protected String errorMessage = GatewayStatus.SUCCESS.getMessage();

    /** 具体内容. */
    private T data;
}
