package com.sdwu.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "成功"),
    UN_ERROR(500, "未知失败"),
    Password_ERROR(1, "密码错误"),
    ILLEGAL_PARAMETER(2, "非法参数"),
    USER_NOT_EXIST(3, "用户不存在"),
    USER_NAME_EXIST(4, "用户已存在"),
    ROLE_NAME_EXIST(5, "角色名称已存在"),
    ROLE_KEY_EXIST(6, "权限字符已存在"),
    MENU_NAME_EXIST(7, "菜单名称已存在"),
    PARENT_VIEW(8, "上级菜单不能选择自己"),
    ROLE_EXIST_USER(9, "上级菜单不能选择自己"),
    MENU_HAS_CHILD(10, "菜单存在子菜单，不允许删除"),
    Menu_EXIST_USER(11, "菜单已分配角色，不允许删除"),
    CAPTCHA_EXPIRED(12, "验证码已过期"),
    CAPTCHA_ERROR(13, "验证码错误")
    ;
    private Integer code;
    private String info;

}
