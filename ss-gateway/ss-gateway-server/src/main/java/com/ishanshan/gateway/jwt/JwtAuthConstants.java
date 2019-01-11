package com.ishanshan.gateway.jwt;

import com.ishanshan.gateway.util.AntPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface JwtAuthConstants {

    Logger log = LoggerFactory.getLogger(JwtAuthConstants.class);

    String WEBSOCKET_URL_PATTERN = "/websocket/**";

    /** 系统中使用到的默认编码 */
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /** 逗号. */
    String COMMA = ",";

    /** 冒号. */
    String COLON = ":";

    /** 下划线. */
    String UNDERLINE = "_";

    /** 空字符串. */
    String EMPTY = "";

    String TRANSVERSE = "-";

    /** 文件夹分隔符或者默认斜杠分隔符. */
    String FOLDER_SEPARATOR = "/";

    /** 登录. */
    String AUTH_LOGIN = "/*/*/auth/login";

    /** 用户会话信息. */
    String AUTH_SESSIOIN = "/*/*/auth/session";

    /** 登出. */
    String AUTH_LOGOUT = "/*/*/auth/logout";

    /** 存放解析的url结果. */
    String GATEWAY_URL_REGEX_RESULT = "GATEWAY_URL_REGEX_RESULT";

    /** 解析出来的AuthSession对象. */
    String GATEWAY_PARSED_AUTH_SESSION = "GATEWAY_PARSED_AUTH_SESSION";

    AntPathRequestMatcher authLoginRequestMatcher = new AntPathRequestMatcher(AUTH_LOGIN, "POST");

    AntPathRequestMatcher authSessionRequestMatcher =
            new AntPathRequestMatcher(AUTH_SESSIOIN, "POST");

    AntPathRequestMatcher authLogoutRequestMatcher = new AntPathRequestMatcher(AUTH_LOGOUT, "POST");

    AntPathRequestMatcher postRequestMatcher = new AntPathRequestMatcher("/**", "POST");

    static boolean isAuthLogin(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authLoginRequestMatcher.matches(request);
    }

    static boolean isAuthSession(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authSessionRequestMatcher.matches(request);
    }

    static boolean isAuthLogout(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authLogoutRequestMatcher.matches(request);
    }

    static boolean isPostJson(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        if (!postRequestMatcher.matches(request)) {
            return false;
        }
        String contentType = request.getContentType();
        if (!ContentType.APPLICATION_JSON_VALUE.equals(contentType)
                && !ContentType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
            return false;
        }
        return true;
    }
}
