package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.capstone.service.model.BookSearchResponse;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, BookSearchResponse> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public BookSearchResponse get(String key) {
        // Write your code here
        // Retrieve and return the concert
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the concert from cache
        cache.invalidate(key);
    }

    public void add(String key, BookSearchResponse value) {
        // Write your code here
        // Add concert to cache
        cache.put(key, value);
    }
}
