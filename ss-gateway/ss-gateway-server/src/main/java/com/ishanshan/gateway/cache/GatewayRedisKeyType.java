/*
 * 文件名称：GatewayRedisKeyType.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190109 10:25
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190109-01         Rushing0711     M201901091025 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.cache;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum GatewayRedisKeyType {
    USERINFO_TOKEN("user", "token", "用户登录标识缓存"),
    USERINFO_TOKENWHITELIST("user", "tokenWhitelist", "用户authToken白名单列表"),
    USERINFO_SESSION("user", "cache", "用户登录缓存信息"),
    SYSTEM_INFO("system", "cache", "系统缓存信息"),
    ;

    private String delimiter = ":";
    private String EMPTY = "";

    GatewayRedisKeyType(String serverName, String purpose, String description) {
        keys.add(StringUtils.trimWhitespace(serverName));
        keys.add(StringUtils.trimWhitespace(purpose));
        keys.add(EMPTY);
        this.description = description;
    }

    private List<String> keys = new ArrayList<>();
    private String description;

    public String getKey() {
        return StringUtils.collectionToDelimitedString(keys, delimiter);
    }

    public String getDescription() {
        return description;
    }
}
