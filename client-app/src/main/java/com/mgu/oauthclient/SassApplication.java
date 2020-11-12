package com.mgu.oauthclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class SassApplication {
    public static void main(String[] args) {
        SpringApplication.run(SassApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
    }
}
