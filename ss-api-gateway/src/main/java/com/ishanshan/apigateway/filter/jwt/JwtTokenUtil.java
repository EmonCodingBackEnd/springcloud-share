/*
 * 文件名称：JwtTokenUtil.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181104 08:39
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181104-01         Rushing0711     M201811040839 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter.jwt;

import com.ishanshan.apigateway.filter.auth.CustomUserDetails;
import com.ishanshan.apigateway.util.AntPathRequestMatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -1171596647511353722L;

    private static InputStream inputStream =
            Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("static/jwt/jwt.jks");
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    private static AntPathRequestMatcher webSocketRequestMatcher;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "ss541018".toCharArray());
            privateKey = (PrivateKey) keyStore.getKey("jwt", "ss541018".toCharArray());
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        } catch (KeyStoreException
                | IOException
                | NoSuchAlgorithmException
                | CertificateException
                | UnrecoverableKeyException e) {
            log.error("【JWT】加载密钥库失败", e);
        }

        webSocketRequestMatcher =
                new AntPathRequestMatcher(JwtAuthConstants.WEBSOCKET_URL_PATTERN, "GET");
    }

    /** Token在HTTP请求头中存在的具体属性. */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token值前缀. */
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    /** 默认20小时过期. */
    public static Long expiration;

    @Value("${jwt.expiration:72000}")
    public void setExpiration(Long expiration) {
        JwtTokenUtil.expiration = expiration;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(
                        created, userDetails.getLastPasswordResetDate()));
    }
    /**
     * 根据HttpServletRequest获取token.
     *
     * <p>创建时间: <font style="color:#00FFFF">20181203 20:38</font><br>
     * [请在此输入功能详述]
     *
     * @param request
     * @return java.lang.String
     * @author Rushing0711
     * @since 1.0.0
     */
    public String fetchToken(HttpServletRequest request) {
        String authToken = null;
        String authHeader;
        if (webSocketRequestMatcher.matches(request)) {
            authHeader = request.getParameter(JwtTokenUtil.TOKEN_HEADER);
            if (authHeader != null) {
                try {
                    authHeader = URLDecoder.decode(authHeader, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    authHeader = null;
                    log.error("【JwtTokenUtil】解析authToken失败", e);
                }
            }
        } else {
            authHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        }
        if (authHeader != null && authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            authToken =
                    authHeader.substring(
                            JwtTokenUtil.TOKEN_PREFIX.length()); // The part after "Bearer "
        }
        return authToken;
    }

    // 公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    // 私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    // 字节数组转Base64编码
    public static String byte2Base64(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    // Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }

    public static String rsaEncrypt(String rawData) throws Exception {
        Assert.hasText(rawData, "待加密的字符串不允许为空");
        return byte2Base64(
                publicEncrypt(rawData.getBytes(JwtAuthConstants.DEFAULT_CHARSET), publicKey));
    }

    public static String rsaDecrypt(String encryptedData) throws Exception {
        Assert.hasText(encryptedData, "待解密的字符串不允许为空");
        return new String(
                privateDecrypt(base642Byte(encryptedData), privateKey),
                JwtAuthConstants.DEFAULT_CHARSET);
    }
}

/*
使用JDK自带的keytool工具生成非对称加密的token。
keytool -genkey -alias jwt -keyalg  RSA -keysize 1024 -validity 3650 -keystore jwt.jks
【命令解释】：
jwt     秘钥别名
RSA     算法
3650    有效期
jwt.jks 文件名
【命令执行概述】：
输入密钥库口令：				    ss541018
再次输入新口令：				    ss541018
您的名字与姓氏是什么？				[忽略]
您的组织单位名称是什么？			    [忽略]
您的组织名称是什么？				[忽略]
您所在的城市或区域名称是什么？		[忽略]
您所在的省/市自治区名称是什么？		[忽略]
该单位的双字母国家/地区代码是什么？	[忽略]
CN=Unknown，OU=Unknown，O=Unknown，L=Unknown，ST=Unknown，C=Unknown是否正确？
[否]：y
输入<jwt>的密钥口令
    （如果和秘钥库口令相同，按回车）：[回车]
 */
