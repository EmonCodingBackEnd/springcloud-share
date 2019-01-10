/*
 * 文件名称：ErrorHandler.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 13:28
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901101328 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.controller;

import com.ishanshan.apigateway.exception.GatewayException;
import com.ishanshan.apigateway.filter.auth.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorHandler {

    @GetMapping("/error")
    public ResponseEntity<AuthResponse> error(HttpServletRequest request) {
        GatewayException gatewayException =
                (GatewayException) request.getAttribute("javax.servlet.error.exception");
        AuthResponse authResponse = new AuthResponse();
        authResponse.setErrorCode(gatewayException.getCode());
        authResponse.setErrorMessage(gatewayException.getMessage());
        return new ResponseEntity<>(authResponse, HttpStatus.BAD_GATEWAY);
    }
}
