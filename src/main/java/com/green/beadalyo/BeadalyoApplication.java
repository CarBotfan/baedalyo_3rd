package com.green.beadalyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BeadalyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeadalyoApplication.class, args);
    }

}
