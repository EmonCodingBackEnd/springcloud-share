package com.ishanshan.gateway.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 缘起于高韩.
 *
 * <p>创建时间: <font style="color:#00FFFF">20181112 09:52</font><br>
 * [请在此输入功能详述]
 *
 * @author Rushing0711
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class RedisCache {

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        RedisCache.redisTemplate = redisTemplate;
    }

    @Cacheable(
        value = "userCache",
        key =
                "T(String).valueOf(#eurekaServerName) + T(com.ishanshan.apigateway.cache.RedisKeyType).USERINFO_SESSION.key + T(String).valueOf(#userId)",
        sync = true
    )
    public AuthSession authSession(String eurekaServerName, String username) {
        // TODO: 2019/1/9 通过Feign获取，并放入缓存
        //        LoginSession loginSession = authService.loginSession(userId);
        //        log.info("【缓存】载入用户会话信息到缓存中, key={}{}", RedisKeyType.USERINFO_SESSION.getKey(),
        // userId);
        return null;
    }

    public static void deleteInfo(String key) {
        if (redisTemplate.opsForValue().getOperations().hasKey(key)) {
            redisTemplate.opsForValue().getOperations().delete(String.valueOf(key));
            log.info("【缓存】删除缓存信息, key={}", key);
        } else {
            log.info("【缓存】删除缓存信息, 缓存不存在, key={}", key);
        }
    }
}
