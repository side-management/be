package com.example.sidemanagementbe.web.feign.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.example.sidemanagementbe")
@Configuration
public class FeignConfiguration {
}

