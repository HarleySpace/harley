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
public class PactStructuredRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PactStructuredRestApplication.class, args);
    }
}
