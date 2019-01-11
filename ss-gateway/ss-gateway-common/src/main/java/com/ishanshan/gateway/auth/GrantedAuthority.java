/*
 * 文件名称：GrantedAuthority.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 18:02
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901101802 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.auth;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;

@Data
public class GrantedAuthority implements Serializable {
    private static final long serialVersionUID = -6213739599007493368L;

    private final String url;

    public GrantedAuthority(String url) {
        Assert.hasText(url, "A granted authority textual representation is required");
        this.url = url;
    }

    public String getAuthority() {
        return url;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof GrantedAuthority) {
            return url.equals(((GrantedAuthority) obj).url);
        }

        return false;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public String toString() {
        return this.url;
    }
}
