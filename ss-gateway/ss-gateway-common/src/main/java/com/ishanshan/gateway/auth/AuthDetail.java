/*
 * 文件名称：AuthDetail.java
 * 系统名称：[系统名称]
 * 模块名称：[模块名称]
 * 软件版权：Copyright (c) 2011-2019, liming20110711@163.com All Rights Reserved.
 * 功能说明：[请在此处输入功能说明]
 * 开发人员：Rushing0711
 * 创建日期：20190110 18:05
 * 修改记录：
 * <Version>        <DateSerial>        <Author>        <Description>
 * 1.0.0            20190110-01         Rushing0711     M201901101805 新建文件
 ********************************************************************************/
package com.ishanshan.gateway.auth;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

@Data
public class AuthDetail implements Serializable {

    private static final long serialVersionUID = -25873329848935908L;

    /** 注意：这里是用户的唯一标识，一般来讲是用户的ID. */
    protected String userId;

    /** 用户的权限列表. */
    protected Set<GrantedAuthority> authorities;

    public AuthDetail() {
        this.userId = AuthConstants.EMPTY;
        this.authorities = new HashSet<>();
    }

    public AuthDetail(String userId, Collection<? extends GrantedAuthority> authorities) {
        if (((userId == null) || "".equals(userId))) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }

        this.userId = userId;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities =
                new TreeSet<GrantedAuthority>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(
                    grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = 1567990802306306641L;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set.
            // If the authority is null, it is a custom authority and should precede
            // others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof AuthDetail) {
            return userId.equals(((AuthDetail) rhs).userId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("userId: ").append(this.userId).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
