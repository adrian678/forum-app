package com.github.adrian678.forum.forumapp.identityandaccess;

public class Role {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_BASIC_USER = "ROLE_BASIC_USER";
    public static final String HAS_ROLE_ADMIN_EXPR = "hasRole('" + ROLE_ADMIN + "')";        //spel expression
    public static final String HAS_ROLE_BASIC_USER_EXPR = "hasRole('" + ROLE_BASIC_USER + "')";   //spel expression

}
