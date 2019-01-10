package com.ishanshan.gateway.converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ishanshan.gateway.auth.AuthResponse;
import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseBody2AuthResponseConverter {

    public static AuthResponse convert(String json) {

        Gson gson = new Gson();

        AuthResponse authResponse;
        try {
            authResponse = gson.fromJson(json, new TypeToken<AuthResponse>() {}.getType());
        } catch (JsonSyntaxException e) {
            log.error(String.format("【对象转换】错误,json=%s", json), e);
            throw new GatewayException(GatewayStatus.GATEWAY_POST_PARSE_RESPONSE_BODY_ERROR);
        }

        return authResponse;
    }
}
