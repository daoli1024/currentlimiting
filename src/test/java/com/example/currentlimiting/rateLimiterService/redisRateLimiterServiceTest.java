package com.example.currentlimiting.rateLimiterService;

import com.example.currentlimiting.CurrentlimitingApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class redisRateLimiterServiceTest extends CurrentlimitingApplicationTests {

    @Autowired
    private redisRateLimiterService redisRateLimiterService;

    @Test
    public void tryAquare() throws InterruptedException {
        for(int i=0;i<10;i++){
            redisRateLimiterService.tryAquare();
            Thread.sleep(200);
        }
    }
}
