package com.alouzou.sondage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SondageApplication {

    public static void main(String[] args) {
        SpringApplication.run(SondageApplication.class, args);
    }

}
