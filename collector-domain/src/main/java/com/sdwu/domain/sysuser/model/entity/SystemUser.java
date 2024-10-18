package com.sdwu.domain.sysuser.model.entity;

import com.sdwu.types.model.PageParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemUser extends PageParam {

    private Long userId;

    private Long deptId;

    private String userName;

    private String nickName;

    private String userType;

    private String email;

    private String phoneNumber;

    private String sex;

    private String avatar;

    private String password;

    private String status;

    private String loginIp;

    private Date loginDate;

    private Long[] roleIds;

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

}
