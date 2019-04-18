package com.swkang.springcloud.apigateawaydemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ApiGateawayDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateawayDemo1Application.class, args);
    }

}
