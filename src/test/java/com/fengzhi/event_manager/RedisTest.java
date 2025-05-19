package com.fengzhi.event_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest //单元测试执行前会先初始化Spring容器
public class RedisTest {

    StringRedisTemplate stringRedisTemplate;
    @Autowired
    public RedisTest(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Test
    public void test() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("name", "zhangsan");
        System.out.println(operations.get("name"));
    }
}
