package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AniListApplication {
    public static void main(String[] args) {
        SpringApplication.run(AniListApplication.class, args);
    }
}