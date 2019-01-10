/*
 * 文件名称：AuthConstants.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 20:12
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901102012 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.auth;

public interface AuthConstants {

    /** 空字符串. */
    String EMPTY = "";

    /** 网关根据token得到的缓存信息，放入request中的属性名. */
    String GATEWAY_RAW_AUTH_SESSION = "GATEWAY_RAW_AUTH_SESSION";
}
