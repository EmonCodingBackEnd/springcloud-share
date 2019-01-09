/*
 * 文件名称：LoginSession.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190109 17:28
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190109-01         Rushing0711     M201901091728 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginSession implements Serializable {

    private static final long serialVersionUID = 2530656755926475513L;

    /** 用户编号. */
    private String userId;

    /** 用户所属租户. */
    private String tenantId;

    /** 用户的当前门店，用户切换前取自系统中的默认门店 */
    private String currentShopId;

    /** 用户在当前门店拥有的角色. */
    private List<String> authorityList;

    private String postJson;
}
