package com.sdwu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Configurable
@MapperScan("com.sdwu.infrastructure.persistent.dao") // 指定Mapper接口所在的包
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
