/*
 * 文件名称：AddResponseHeaderFilter.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190109 05:18
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190109-01         Rushing0711     M201901090518 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.filter;

import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import com.ishanshan.gateway.jwt.JwtAuthConstants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * 后置处理.
 *
 * <p>创建时间: <font style="color:#00FFFF">20190109 05:18</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class PostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (!requestContext.sendZuulResponse()) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        if (JwtAuthConstants.isAuthSessioin(request)) {
            // TODO: 2019/1/9 获取Session放入缓存中
        } else {
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_BAD_REQUEST);
        }
        response.setHeader("X-Foo", UUID.randomUUID().toString());
        return null;
    }
}
