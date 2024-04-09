package com.chw;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chw.mapper")
@Slf4j
public class ClimbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClimbApplication.class, args);
        log.info("climb application 启动成功");
    }
}
