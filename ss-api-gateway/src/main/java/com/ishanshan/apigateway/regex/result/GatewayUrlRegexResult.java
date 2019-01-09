package com.ishanshan.apigateway.regex.result;

import com.ishanshan.apigateway.regex.RegexResult;
import lombok.Data;

@Data
public class GatewayUrlRegexResult extends RegexResult {

    private String url;

    private String eurekaServerName;

    private String api;

    public static GatewayUrlRegexResult instance() {
        return new GatewayUrlRegexResult();
    }
}
