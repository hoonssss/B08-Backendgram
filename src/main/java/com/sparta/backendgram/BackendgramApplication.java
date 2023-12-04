package com.sparta.backendgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class BackendgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendgramApplication.class, args);
    }

}
