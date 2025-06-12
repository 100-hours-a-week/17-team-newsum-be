package com.akatsuki.newsum.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.akatsuki.newsum")
public class OpenFeignConfig {
}
