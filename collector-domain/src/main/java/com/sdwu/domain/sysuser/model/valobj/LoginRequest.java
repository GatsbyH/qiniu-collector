package com.sdwu.domain.sysuser.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

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
   }
