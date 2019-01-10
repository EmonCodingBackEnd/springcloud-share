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
package com.ishanshan.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishanshan.gateway.auth.AuthConstants;
import com.ishanshan.gateway.cache.AuthSession;
import com.ishanshan.gateway.cache.GatewayRedisKeyUtil;
import com.ishanshan.gateway.cache.RedisCache;
import com.ishanshan.gateway.converter.Cache2AuthSessionConverter;
import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import com.ishanshan.gateway.jwt.JwtAuthConstants;
import com.ishanshan.gateway.jwt.JwtTokenUtil;
import com.ishanshan.gateway.regex.RegexDefine;
import com.ishanshan.gateway.regex.RegexSupport;
import com.ishanshan.gateway.regex.result.GatewayUrlRegexResult;
import com.ishanshan.gateway.util.UrlUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

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

    @Autowired private ObjectMapper objectMapper; // Json转化工具

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
            log.error("【网关前置过滤】url不合法，正确格式需满足正则校验， regex={}", RegexDefine.GATEWAY_URL_REGEX);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_BAD_REQUEST);
        }
        requestContext.set(JwtAuthConstants.GATEWAY_URL_REGEX_RESULT, gatewayUrlRegexResult);

        if (JwtAuthConstants.isAuthLogin(request)) {
            return false;
        }

        String authToken = jwtTokenUtil.fetchToken(request);
        if (StringUtils.isEmpty(authToken)) {
            requestContext.setSendZuulResponse(false);
            log.error("【网关前置过滤】token not found");
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_TOKEN_NOT_FOUND);
        }

        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        if (StringUtils.isEmpty(username)) {
            requestContext.setSendZuulResponse(false);
            log.error("【网关前置过滤】token invalid， token={}", authToken);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_TOKEN_INVALID);
        }
        log.info("checking authentication " + username);

        String eurekaServerName = gatewayUrlRegexResult.getEurekaServerName();
        // Redis中是否还存在（比如登出删除/过期丢弃等）
        String userinfoCacheRedisKey =
                GatewayRedisKeyUtil.getUserinfoCacheRedisKey(eurekaServerName, username);
        boolean existAuthToken =
                stringRedisTemplate.opsForValue().getOperations().hasKey(userinfoCacheRedisKey);
        if (!existAuthToken) {
            requestContext.setSendZuulResponse(false);
            log.error("【网关前置过滤】token expired");
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_TOKEN_EXPIRED);
        }

        String authSessionJson = stringRedisTemplate.opsForValue().get(userinfoCacheRedisKey);
        AuthSession authSession = Cache2AuthSessionConverter.convert(authSessionJson);
        if (!jwtTokenUtil.validateToken(authToken, authSession.getAuthDetail())) {
            requestContext.setSendZuulResponse(false);
            log.error(
                    "【网关前置过滤】token 被篡改, originTokenUsername={}, reqTokenUsername={}",
                    authSession.getAuthDetail().getUsername(),
                    username);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_TOKEN_FORGED);
        }

        // 白名单校验
        /*String whitelistRedisKey =
                GatewayRedisKeyUtil.getUserinfoTokenWhitelistRedisKey(
                        gatewayUrlRegexResult.getEurekaServerName(), username);
        String whiteToken = stringRedisTemplate.opsForValue().get(whitelistRedisKey);
        if (authToken.equals(whiteToken)) {
            log.error("【网关前置过滤】您已被强制登出,请及时登录修改密码,username={}", username);
            throw new GatewayException(GatewayStatus.GATEWAY_PRE_USER_FORCED_LOGOUT);
        }*/

        requestContext.set(JwtAuthConstants.GATEWAY_PARSED_AUTH_SESSION, authSession);
        request.setAttribute(AuthConstants.GATEWAY_RAW_AUTH_SESSION, authSession.getCachedJson());
        requestContext.setRequest(
                new HttpServletRequestWrapper(request) {
                    @Override
                    public ServletInputStream getInputStream() throws IOException {
                        return super.getInputStream();
                    }
                });
        return null;
    }
}
