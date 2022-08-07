package com.example.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@SpringBootTest
class CommunityApplicationTests {
    @Resource
    private JedisPool jedisPool;

    @Test
    void contextLoads() {
        System.out.println(jedisPool);
        //在连接池中得到Jedis连接
        Jedis jedis=jedisPool.getResource();
        String name = jedis.get("name");
        System.out.println(name);
        //关闭当前连接
        jedis.close();
    }

}
