package org.zhuyuqinlan.lemall.common.service;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作Service
 */
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ======================= 通用操作 =======================

    public void set(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ======================= Hash操作 =======================

    public void hSet(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Boolean hSet(String key, String hashKey, String value, long time) {
        hSet(key, hashKey, value);
        return expire(key, time);
    }

    public String hGet(String key, String hashKey) {
        Object val = redisTemplate.opsForHash().get(key, hashKey);
        return val == null ? null : val.toString();
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void hSetAll(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Boolean hSetAll(String key, Map<String, String> map, long time) {
        hSetAll(key, map);
        return expire(key, time);
    }

    public void hDel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Long hIncr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Long hDecr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    // ======================= Set操作 =======================

    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long sAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public Long sAdd(String key, long time, String... values) {
        Long count = sAdd(key, values);
        expire(key, time);
        return count;
    }

    public Boolean sIsMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Long sRemove(String key, String... values) {
        return redisTemplate.opsForSet().remove(key, (Object[]) values);
    }

    // ======================= List操作 =======================

    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public Long lPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Long lPush(String key, String value, long time) {
        Long index = lPush(key, value);
        expire(key, time);
        return index;
    }

    public Long lPushAll(String key, String... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public Long lPushAll(String key, long time, String... values) {
        Long count = lPushAll(key, values);
        expire(key, time);
        return count;
    }

    public Long lRemove(String key, long count, String value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    public List<String> scanKeys(String prefix, int count) {
        List<String> keys = new ArrayList<>();
        RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();

        try (Cursor<byte[]> cursor = connection.scan(
                ScanOptions.scanOptions().match(prefix + "*").count(count).build()
        )) {
            cursor.forEachRemaining(k -> keys.add(new String(k, StandardCharsets.UTF_8)));
        }
        return keys;
    }


    // ======================= 分布式锁 =======================

    public boolean tryLock(String key, long expireSeconds) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "LOCKED", Duration.ofSeconds(expireSeconds));
        return Boolean.TRUE.equals(success);
    }

    public void unlock(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ignored) {}
    }
}
