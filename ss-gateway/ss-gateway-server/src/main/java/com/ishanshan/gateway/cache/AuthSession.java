/*
 * 文件名称：AuthSession.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 19:37
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901101937 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.cache;

import com.ishanshan.gateway.auth.AuthDetail;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthSession implements Serializable {

    private static final long serialVersionUID = 6533255490745378123L;

    private String token;

    private AuthDetail authDetail;

    private String cachedJson;
}
