package com.polling.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NotificationRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final String PREFIX = "sms:";  //sms 딱지 달고 보내기
    private final int LIMIT_TIME = 3 * 60;  //3분동안 유효

    public void createSmsCertification(String phone, String certificationNumber) {
        System.out.println("opsforvalue");
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        System.out.println("setvalue");
        values.set(PREFIX + phone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));  // 3분 뒤 메모리에서 삭제된다.

    }

    public String getSmsCertification(String phone) { // (4)
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(PREFIX + phone);
    }

    public void removeSmsCertification(String phone) {
        redisTemplate.delete(PREFIX + phone);
    }

    public boolean hasKey(String phone) {  //(6)
        return stringRedisTemplate.hasKey(PREFIX + phone);
    }

}