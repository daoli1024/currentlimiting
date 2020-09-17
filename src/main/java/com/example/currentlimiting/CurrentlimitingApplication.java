package com.example.currentlimiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class CurrentlimitingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentlimitingApplication.class, args);
    }

}
