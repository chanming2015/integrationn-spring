package com.github.chanming2015.domain.authority.aop;

import java.lang.annotation.Annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import com.github.chanming2015.domain.authority.RequiresFrontUser;
import com.github.chanming2015.domain.authority.ShiroTool;


public class MyFrontUserAnnotationHandler extends AuthorizingAnnotationHandler {
    public MyFrontUserAnnotationHandler() {
        super(RequiresFrontUser.class);
    }
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if(!(a instanceof RequiresFrontUser)) return;
        
        if(ShiroTool.isFrontAuthenticated()==false)
        	throw new UnauthorizedException("user does not have a front role");
    }
}
