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

import com.ishansha.api.AppRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.ParameterizedType;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class GatewayRequest<T extends GatewayResponse> extends AppRequest {

    private static final long serialVersionUID = 6355146217352098846L;

    public Class<T> responseClass() {
        return (Class<T>)
                ((ParameterizedType) this.getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }
}
