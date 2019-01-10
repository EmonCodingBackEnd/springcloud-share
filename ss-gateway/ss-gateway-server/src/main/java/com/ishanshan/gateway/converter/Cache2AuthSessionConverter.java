package com.ishanshan.gateway.converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ishanshan.gateway.cache.AuthSession;
import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Cache2AuthSessionConverter {

    public static AuthSession convert(String json) {

        Gson gson = new Gson();

        AuthSession authSession;
        try {
            authSession = gson.fromJson(json, new TypeToken<AuthSession>() {}.getType());
        } catch (JsonSyntaxException e) {
            log.error(String.format("【对象转换】错误,json=%s", json), e);
            throw new GatewayException(GatewayStatus.GATEWAY_POST_PARSE_AUTH_SESSION_CACHE_ERROR);
        }

        return authSession;
    }
}
