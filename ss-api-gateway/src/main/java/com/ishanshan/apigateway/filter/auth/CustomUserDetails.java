/*
 * 文件名称：CustomUser.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181104 08:44
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181104-01         Rushing0711     M201811040844 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter.auth;

import lombok.Data;
import java.util.Date;

@Data
public class CustomUserDetails {

    private static final long serialVersionUID = 4511886230708246366L;

    private String username;

    /** 用户所属租户. */
    private String tenantId;

    /** 用户的当前门店，用户切换前取自系统中的默认门店 */
    private String currentShopId;

    private Date lastPasswordResetDate;

    public CustomUserDetails(String username, String tenantId, String currentShopId) {
        this.username = username;
        this.tenantId = tenantId;
        this.currentShopId = currentShopId;
    }
}
