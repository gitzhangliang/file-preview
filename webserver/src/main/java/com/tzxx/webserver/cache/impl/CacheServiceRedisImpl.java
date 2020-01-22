package com.tzxx.webserver.cache.impl;

import com.tzxx.webserver.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author tzxx
 */
@ConditionalOnExpression("'${cache.type:default}'.equals('redis')")
@Service
public class CacheServiceRedisImpl implements CacheService {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ValueOperations<String, Object> valueOperations;
    @Resource
    private ZSetOperations<String, Object> zSetOperations;

    public <T>T get(String key) {
        return (T) valueOperations.get(key);
    }

    public void set(String key, Object value, long timeout) {
        valueOperations.set(key,value,timeout, TimeUnit.SECONDS);
    }

    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public boolean exists(String key) {
        if(StringUtils.isEmpty(key)){
            return false;
        }else {
            return redisTemplate.hasKey(key);
        }
    }

    @Override
    public boolean containsKey(String key) {
        return exists(key);
    }

    @Override
    public void add(String key, Object value) {
        valueOperations.set(key,value);
    }

    @Override
    public boolean zSet(String key,Object value, Double score){
        return zSetOperations.add(key,value,score);
    }

    @Override
    public List<Object> zSetList(String key) {
        List<Object> objects = new ArrayList<>(500);
        Set<ZSetOperations.TypedTuple<Object>> tuples =  zSetOperations.rangeWithScores(key, 0, -1);
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            objects.add(tuple.getValue());
        }
        return objects;
    }

    @Override
    public Long zSetCount(String key) {
        return zSetOperations.size(key);
    }

    @Override
    public Long zSetRemove(String key, Object value){
        return zSetOperations.remove(key,value);
    }

    @Override
    public boolean zSetContains(String key, Object value){
        Long rank = zSetOperations.rank(key, value);
        return rank != null;
    }

    @Override
    public Object zSetMinScore(String key) {
        Set<Object> range = zSetOperations.range(key, 0, 0);
        return range == null ? null : range.toArray(new Object[0])[0];
    }
}
