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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishanshan.gateway.auth.AuthResponse;
import com.ishanshan.gateway.cache.AuthSession;
import com.ishanshan.gateway.cache.GatewayRedisKeyUtil;
import com.ishanshan.gateway.converter.ResponseBody2AuthResponseConverter;
import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import com.ishanshan.gateway.jwt.JwtAuthConstants;
import com.ishanshan.gateway.jwt.JwtTokenUtil;
import com.ishanshan.gateway.regex.result.GatewayUrlRegexResult;
import com.ishanshan.gateway.util.FilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
@Slf4j
public class PostFilter extends ZuulFilter {
    @Autowired private ObjectMapper objectMapper; // Json转化工具

    @Autowired private JwtTokenUtil jwtTokenUtil;

    @Autowired private StringRedisTemplate stringRedisTemplate;

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
        GatewayUrlRegexResult gatewayUrlRegexResult =
                (GatewayUrlRegexResult)
                        requestContext.get(JwtAuthConstants.GATEWAY_URL_REGEX_RESULT);

        String responseBody = FilterUtils.getResponseBody();
        if (StringUtils.isEmpty(responseBody)) {
            log.error("【网关后置过滤】应答体为空");
            throw new GatewayException(GatewayStatus.GATEWAY_POST_RESPONSE_BODY_EMPTY_ERROR);
        }

        AuthResponse authResponse = ResponseBody2AuthResponseConverter.convert(responseBody);
        if (!GatewayStatus.SUCCESS.getCode().equals(authResponse.getErrorCode())) {
            log.error("【网关后置过滤】用户登录失败 responseBody=", responseBody);
            throw new GatewayException(authResponse.getErrorCode(), authResponse.getErrorMessage());
        }

        if (JwtAuthConstants.isAuthLogin(request)) {
            String authToken = jwtTokenUtil.generateToken(authResponse.getAuthDetail());
            try {
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(authToken);
                String newResponseBody = objectMapper.writeValueAsString(tokenResponse);
                requestContext.setResponseBody(newResponseBody);
            } catch (JsonProcessingException e) {
                log.error("【网关后置过滤】生成token失败", e);
                throw new GatewayException(GatewayStatus.GATEWAY_POST_GENERATE_TOKEN_ERROR);
            }
            AuthSession authSession = new AuthSession();
            authSession.setToken(authToken);
            authSession.setAuthDetail(authResponse.getAuthDetail());
            authSession.setCachedJson(responseBody);

            String authSessionJson = null;
            try {
                authSessionJson = objectMapper.writeValueAsString(authSession);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            String eurekaServerName = gatewayUrlRegexResult.getEurekaServerName();
            String userinfoCacheRedisKey =
                    GatewayRedisKeyUtil.getUserinfoCacheRedisKey(
                            eurekaServerName, authSession.getAuthDetail().getUsername());
            stringRedisTemplate
                    .opsForValue()
                    .set(
                            userinfoCacheRedisKey,
                            authSessionJson,
                            JwtTokenUtil.expiration,
                            TimeUnit.SECONDS);

            String whitelistRedisKey =
                    GatewayRedisKeyUtil.getUserinfoTokenWhitelistRedisKey(
                            gatewayUrlRegexResult.getEurekaServerName(),
                            authResponse.getAuthDetail().getUsername());
            stringRedisTemplate
                    .opsForValue()
                    .set(whitelistRedisKey, authToken, JwtTokenUtil.expiration, TimeUnit.SECONDS);
        } else if (JwtAuthConstants.isAuthSession(request)) {
            AuthSession authSession =
                    (AuthSession) requestContext.get(JwtAuthConstants.GATEWAY_PARSED_AUTH_SESSION);
            authSession.setAuthDetail(authResponse.getAuthDetail());
            authSession.setCachedJson(responseBody);

            String authSessionJson = null;
            try {
                authSessionJson = objectMapper.writeValueAsString(authSession);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String eurekaServerName = gatewayUrlRegexResult.getEurekaServerName();
            String userinfoCacheRedisKey =
                    GatewayRedisKeyUtil.getUserinfoCacheRedisKey(
                            eurekaServerName, authSession.getAuthDetail().getUsername());
            stringRedisTemplate
                    .opsForValue()
                    .set(
                            userinfoCacheRedisKey,
                            authSessionJson,
                            JwtTokenUtil.expiration,
                            TimeUnit.SECONDS);

            String whitelistRedisKey =
                    GatewayRedisKeyUtil.getUserinfoTokenWhitelistRedisKey(
                            eurekaServerName, authSession.getAuthDetail().getUsername());
            stringRedisTemplate
                    .opsForValue()
                    .set(
                            whitelistRedisKey,
                            authSession.getToken(),
                            JwtTokenUtil.expiration,
                            TimeUnit.SECONDS);

        } else {
            throw new GatewayException(GatewayStatus.GATEWAY_POST_PARSE_RESPONSE_BODY_ERROR);
        }
        response.setHeader("X-Foo", UUID.randomUUID().toString());
        return null;
    }
}
