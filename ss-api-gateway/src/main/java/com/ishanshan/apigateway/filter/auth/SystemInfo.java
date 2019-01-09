/*
 * 文件名称：SystemAuthority.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2018, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20181110 13:44
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20181110-01         Rushing0711     M201811101344 新建文件
 ********************************************************************************/
package com.ishanshan.apigateway.filter.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SystemInfo implements Serializable {

    private static final long serialVersionUID = 5417313738602358670L;

    private List<MenuDTO> menuDTOList = new ArrayList<>();

    @Data
    public static class MenuDTO implements Serializable {

        private static final long serialVersionUID = 2396196969163569337L;

        private Long menuId;

        private Integer systemType;

        private String menuKey;

        private String menuName;

        private String menuPath;

        private String urlPattern;

        private Long parentId;

        private List<MenuItemDTO> menuItemDTOList = new ArrayList<>();

        /** 该菜单出现在了那些角色中. */
        private List<String> roleList = new ArrayList<>();
    }

    @Data
    public static class MenuItemDTO implements Serializable {

        private static final long serialVersionUID = -5046123081811893979L;

        private Long itemId;

        private String itemKey;

        private String itemName;
    }
}
