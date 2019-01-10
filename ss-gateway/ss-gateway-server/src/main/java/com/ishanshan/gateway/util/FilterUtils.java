/*
 * 文件名称：FilterUtils.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190111 01:32
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190111-01         Rushing0711     M201901110132 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.util;

import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import com.ishanshan.gateway.jwt.JwtAuthConstants;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FilterUtils {

    public static String getResponseBody() {
        InputStream stream = RequestContext.getCurrentContext().getResponseDataStream();
        try {
            byte[] bodyBytes = FileCopyUtils.copyToByteArray(stream);
            String responseBody = new String(bodyBytes, JwtAuthConstants.DEFAULT_CHARSET);
            RequestContext.getCurrentContext().setResponseBody(responseBody);
            return responseBody;
        } catch (IOException e) {
            log.error("【应答体解析】解析应答体失败", e);
            throw new GatewayException(GatewayStatus.GATEWAY_POST_PARSE_RESPONSE_BODY_ERROR);
        }
    }
}
