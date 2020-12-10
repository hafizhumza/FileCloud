package com.filecloud.authserver.security.role;

import org.springframework.security.access.prepost.PreAuthorize;

import com.filecloud.authserver.constant.ConstUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@PreAuthorize("hasRole('" + ConstUtil.ROLE_USER + "')")
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface User {
}
