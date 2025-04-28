package com.locfox.qr_book.account_service.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            var source = new ClassPathResource(".env");
            var properties = new Properties();
            properties.load(source.getInputStream());

            var dotenvMap = properties.stringPropertyNames().stream().collect(Collectors.toMap(Function.identity(), properties::get));

            environment.getPropertySources().addFirst(new MapPropertySource("env", dotenvMap));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file", e);
        }
    }

}
