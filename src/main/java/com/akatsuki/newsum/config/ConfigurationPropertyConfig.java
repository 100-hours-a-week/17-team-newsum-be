package com.akatsuki.newsum.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.akatsuki.newsum.extern.properties.AiServerProperties;

@Configuration
// @ConfigurationPropertiesScan
@EnableConfigurationProperties(AiServerProperties.class)
public class ConfigurationPropertyConfig {
}
