package com.yuliavslv.shop.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@SpringBootApplication
public class BackendApplication {
    public final static String MESSAGES_PROP = "messages_ENG.properties";

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        try(InputStream propertiesInputStream = getClass().getClassLoader().getResourceAsStream(MESSAGES_PROP)) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            log.error(MESSAGES_PROP + "was not found");
        }
        return properties;
    }
}
