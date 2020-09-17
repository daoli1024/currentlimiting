package com.example.currentlimiting.rateLimiterService;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

/*
适用于单台服务器
 */
@Service
public class rateLimiterService implements rateLimiterInterface{

    RateLimiter rateLimiter = RateLimiter.create(1);

    public boolean tryAquare(){
        return rateLimiter.tryAcquire();
    }

}
