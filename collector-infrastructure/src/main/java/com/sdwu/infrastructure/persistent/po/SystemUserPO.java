package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.*;
import com.sdwu.domain.sysuser.model.entity.SystemUser;
import com.sdwu.infrastructure.persistent.utils.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class SystemUserPO extends BaseDO implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("dept_id")
    private Long deptId;

    @TableField("user_name")
    private String userName;

    @TableField("nick_name")
    private String nickName;

    @TableField("user_type")
    private String userType;

    @TableField("email")
    private String email;

    @TableField("phonenumber")
    private String phoneNumber;

    @TableField("sex")
    private String sex;

    @TableField("avatar")
    private String avatar;

    @TableField("password")
    private String password;

    @TableField("status")
    private String status;

    @TableField("login_ip")
    private String loginIp;

    @TableField("login_date")
    private Date loginDate;


    public static SystemUser convertToDomain(SystemUserPO userPO) {
        SystemUser user = new SystemUser();
        user.setUserId(userPO.getUserId());
        user.setUserName(userPO.getUserName());
        user.setNickName(userPO.getNickName());
        user.setUserType(userPO.getUserType());
        user.setEmail(userPO.getEmail());
        user.setPhoneNumber(userPO.getPhoneNumber());
        user.setSex(userPO.getSex());
        user.setAvatar(userPO.getAvatar());
        user.setPassword(null);
        user.setStatus(userPO.getStatus());
        user.setLoginIp(userPO.getLoginIp());
        user.setLoginDate(userPO.getLoginDate());
        return user;
    }

    public static SystemUserPO convertToPo(SystemUser user) {
        return SystemUserPO.builder()
                .userId(user.getUserId())
                .deptId(user.getDeptId())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .userType(user.getUserType())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .sex(user.getSex())
                .avatar(user.getAvatar())
                .password(user.getPassword())
                .status(user.getStatus())
                .build();
    }
}
