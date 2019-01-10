/*
 * 文件名称：BigDecimal2StringSerializer.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181112 14:53
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181112-01         Rushing0711     M201811121453 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimal2StringSerializer extends JsonSerializer<BigDecimal> {
    // 默认的格式化字符样式 “#.00”  还可以是像“#.0000”
    private static final String DEFAULT_FORMAT_PATTERN = "#,##0.00";

    private DecimalFormat decimalFormat = new DecimalFormat(DEFAULT_FORMAT_PATTERN);

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeString(decimalFormat.format(value));
    }
}
