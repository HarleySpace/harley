package com.rzdata.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description 启动程序
 * @author hanj
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.rzdata"})
public class HarleyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarleyAdminApplication.class, args);
    }
}
