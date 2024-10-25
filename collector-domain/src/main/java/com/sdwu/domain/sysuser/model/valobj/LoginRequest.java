package com.sdwu.domain.sysuser.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

   @Data
   @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   public class LoginRequest {
       @NotNull(message = "Username cannot be null")
       @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
       private String username;

       @NotNull(message = "Password cannot be null")
       private String password;

       /**
        * 验证码
        */
       @NotNull(message = "验证码不能为空")
       private String code;

       /**
        * 唯一标识
        */
       @NotNull(message = "uuid不能为空")
       private String uuid;
   }
