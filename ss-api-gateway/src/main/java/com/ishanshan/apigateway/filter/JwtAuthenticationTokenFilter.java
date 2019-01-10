/*
 * 文件名称：TokenFilter.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190109 05:05
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190109-01         Rushing0711     M201901090505 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter;

import com.ishanshan.apigateway.cache.RedisCache;
import com.ishanshan.apigateway.exception.GatewayException;
import com.ishanshan.apigateway.exception.GatewayStatus;
import com.ishanshan.apigateway.filter.jwt.JwtTokenUtil;
import com.ishanshan.apigateway.regex.RegexDefine;
import com.ishanshan.apigateway.regex.RegexSupport;
import com.ishanshan.apigateway.regex.result.GatewayUrlRegexResult;
import com.ishanshan.apigateway.util.UrlUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 权限拦截.
 *
 * <p>创建时间: <font style="color:#00FFFF">20190109 05:17</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends ZuulFilter {

    @Autowired private RedisCache redisCache;

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String gatewayUrl = UrlUtils.buildRequestUrl(request);
        GatewayUrlRegexResult gatewayUrlRegexResult = RegexSupport.matchGatewayUrl(gatewayUrl);
        if (!gatewayUrlRegexResult.isMatched()) {
            requestContext.setSendZuulResponse(false);
            log.error("【网关前置路由】url不合法，正确格式需满足正则校验， regex={}", RegexDefine.GATEWAY_URL_REGEX);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_BAD_REQUEST);
        }

        String authToken = jwtTokenUtil.fetchToken(request);
        if (StringUtils.isEmpty(authToken)) {
            requestContext.setSendZuulResponse(false);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_TOKEN_NOT_FOUND);
        }

        /*String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if (StringUtils.isEmpty(username)) {
            throw new GatewayException(GatewayStatus.GATEWAY_TOKEN_EXPIRED_OR_INVALID);
        }
        log.info("checking authentication " + username);

        String eurekaServerName = gatewayUrlRegexResult.getEurekaServerName();
        // Redis中是否还存在（比如登出删除/过期丢弃等）
        String redisKey = RedisKeyUtil.getUserinfoCacheRedisKey(eurekaServerName, username);
        boolean existAuthToken = stringRedisTemplate.opsForValue().getOperations().hasKey(redisKey);
        if (!existAuthToken) {
            throw new GatewayException(GatewayStatus.GATEWAY_TOKEN_EXPIRED);
        }

        LoginSession loginSession = redisCache.userSession(eurekaServerName, username);
        CustomUserDetails customUserDetails =
                new CustomUserDetails(
                        loginSession.getUserId(),
                        loginSession.getTenantId(),
                        loginSession.getCurrentShopId());
        if (jwtTokenUtil.validateToken(authToken, customUserDetails)) {
            throw new GatewayException(GatewayStatus.GATEWAY_TOKEN_INVALID);
        }

        String userCacheJson = loginSession.getPostJson();
        // TODO: 2019/1/9 如何存入Request
        log.info("authenticated user " + username + ", setting security context");*/
        return null;
    }
}
