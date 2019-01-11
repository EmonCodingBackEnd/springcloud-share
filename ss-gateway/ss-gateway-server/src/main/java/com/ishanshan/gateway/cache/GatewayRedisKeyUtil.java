/*
 * 文件名称：GatewayRedisKeyUtil.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181106 15:00
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181106-01         Rushing0711     M201811061500 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.cache;

public class GatewayRedisKeyUtil {

    /** 用户登录标识缓存 */
    public static String getUserinfoTokenRedisKey(String eurekaServerName, String username) {
        return GatewayRedisKeyType.USERINFO_TOKEN.getKey().concat(username);
    }

    /** 用户jwtToken白名单列表 */
    public static String getUserinfoTokenWhitelistRedisKey(String eurekaServerName, String userId) {
        return eurekaServerName
                .concat(GatewayRedisKeyType.USERINFO_TOKENWHITELIST.getKey())
                .concat(userId);
    }

    /** 用户登录缓存信息. */
    public static String getUserinfoCacheRedisKey(String eurekaServerName, String userId) {
        return eurekaServerName
                .concat(GatewayRedisKeyType.USERINFO_SESSION.getKey())
                .concat(userId);
    }
}
