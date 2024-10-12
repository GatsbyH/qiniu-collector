package com.sdwu.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("system_user")
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


}
