package com.example.cloudfunction;

import cn.hutool.cron.CronUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudFunctionApplication.class, args);
        CronUtil.setMatchSecond(true);
        CronUtil.start(true);
    }

}
