package com.ishanshan.apigateway.regex.result;

import com.ishanshan.apigateway.regex.RegexResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GatewayUrlRegexResult extends RegexResult {

    private String url;

    private String eurekaServerName;

    private String serverName;

    private String apiVersion;

    private String others;

    /** api = serverName/serverName/apiVersion/others. */
    private String api;

    public static GatewayUrlRegexResult instance() {
        return new GatewayUrlRegexResult();
    }
}
