package com.sdwu.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "成功"),
    UN_ERROR(500, "未知失败"),
    Password_ERROR(0001, "密码错误"),
    ILLEGAL_PARAMETER(0002, "非法参数"),
    USER_NOT_EXIST(0003, "用户不存在"),
    USER_NAME_EXIST(0004, "用户已存在"),
    ROLE_NAME_EXIST(0005, "角色名称已存在"),
    ROLE_KEY_EXIST(0006, "权限字符已存在"),
    ;

    private Integer code;
    private String info;

}
