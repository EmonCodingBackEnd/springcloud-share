/*
 * 文件名称：UserService.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved. 
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190109 07:02
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190109-01         Rushing0711     M201901090702 新建文件
 ********************************************************************************/
package com.coding.user.service;

import com.coding.user.dataobject.UserInfo;

public interface UserService {

    /** 通过openid来查询用户信息. */
    UserInfo findByOpenid(String openid);
}
