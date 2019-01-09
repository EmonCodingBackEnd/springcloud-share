package com.ishanshan.apigateway.filter.jwt;

import com.ishanshan.apigateway.util.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface JwtAuthConstants {

    String WEBSOCKET_URL_PATTERN = "/websocket/**";

    /** 用户被强制登出. */
    String ROLE_FORCED_LOGOUT = "ROLE_FORCED_LOGOUT";

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
    String AUTH_LOGIN = "/auth/login";

    /** 用户会话信息. */
    String AUTH_SESSIOIN = "/auth/session";

    /** 登出. */
    String AUTH_LOGOUT = "/auth/logout";

    AntPathRequestMatcher authLoginRequestMatcher = new AntPathRequestMatcher(AUTH_LOGIN, "POST");

    AntPathRequestMatcher authSessionRequestMatcher =
            new AntPathRequestMatcher(AUTH_SESSIOIN, "POST");

    AntPathRequestMatcher authLogoutRequestMatcher = new AntPathRequestMatcher(AUTH_LOGOUT, "POST");

    static boolean isAuthLogin(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authLoginRequestMatcher.matches(request);
    }

    static boolean isAuthSessioin(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authLoginRequestMatcher.matches(request);
    }

    static boolean isAuthLogout(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        return authLogoutRequestMatcher.matches(request);
    }
}
