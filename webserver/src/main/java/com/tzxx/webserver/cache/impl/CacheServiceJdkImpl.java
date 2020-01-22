package com.tzxx.webserver.cache.impl;

import com.tzxx.common.exception.CacheException;
import com.tzxx.webserver.cache.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author tzxx
 */
@Service
@ConditionalOnExpression("'${cache.type:default}'.equals('jdk')")
public class CacheServiceJdkImpl implements CacheService {

    private Map<String, Object> cache = new ConcurrentHashMap<>(16);

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public void add(String key, Object value) {
        cache.put(key,value);
    }

    @Override
    public boolean zSet(String key, Object value, Double score) {
        throw new CacheException("不支持zSet");
    }

    @Override
    public List<Object> zSetList(String key) {
        throw new CacheException("不支持zSet");
    }

    @Override
    public Long zSetCount(String key) {
        throw new CacheException("不支持zSet");
    }

    @Override
    public Long zSetRemove(String key, Object value) {
        throw new CacheException("不支持zSet");
    }

    @Override
    public boolean zSetContains(String key, Object value) {
        throw new CacheException("不支持zSet");
    }

    @Override
    public Object zSetMinScore(String key) {
        throw new CacheException("不支持zSet");    }
}
