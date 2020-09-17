package com.example.currentlimiting.rateLimiterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class redisRateLimiterService implements rateLimiterInterface{
    private boolean isInit = false;
    private Logger logger = LoggerFactory.getLogger(redisRateLimiterService.class);

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;

    //令牌桶最大容量
    @Value("${rateLimiter.max_permits}")
    private int max_permits;
    //每秒产生的令牌数（每秒可以处理的请求个数）
    @Value("${rateLimiter.permits_per_second}")
    private int permits_per_second;
    //每次获取的令牌数
    @Value("${rateLimiter.acquire_permits}")
    private int acquire_permits;
    //初始令牌数
    @Value("${rateLimiter.init_permits}")
    private int init_permits;

    public redisRateLimiterService() {
    }

    public void init(){
        // 执行 lua 脚本
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redisRateLimiterInit.lua")));
        redisTemplate.execute(redisScript,
                Collections.singletonList("redisRateLimiter"),
                max_permits, init_permits, System.currentTimeMillis(), permits_per_second);

        isInit = true;
    }

    public boolean tryAquare(){
        //初始化
        if(!isInit){
            init();
        }

        // 执行 lua 脚本
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redisRateLimiter.lua")));
        //设置返回类型
        redisScript.setResultType(Long.class);
        Long result = (Long)redisTemplate.execute(redisScript,
                Collections.singletonList("redisRateLimiter"), System.currentTimeMillis(), acquire_permits);
        logger.info("tryAquare result:"+result);

        //返回-1代表没有拿到令牌，否则返回剩余令牌数
        if(result > -1){
            return true;
        }
        return false;
    }

}
