package com.kenzie.appserver.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheClient {
    private Cache<String, String> cache;

    public CacheClient(int expiry, TimeUnit timeUnit){
        this.cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expiry, timeUnit)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    }

    public Concert get(String key) {
        // Write your code here
        // Retrieve and return the concert
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the concert from cache
        cache.invalidate(key);
    }

    public void add(String key, Concert value) {
        // Write your code here
        // Add concert to cache
        cache.put(key, value);
    }
}
