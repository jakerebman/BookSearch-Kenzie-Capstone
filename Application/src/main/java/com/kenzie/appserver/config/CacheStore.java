package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.appserver.controller.model.BookSearchResponse;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, BookSearchResponse> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // Initialize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public BookSearchResponse get(String key) {
        // Retrieve and return the book
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Invalidate/evict the book from the cache
        cache.invalidate(key);
    }

    public void add(String key, BookSearchResponse value) {
        // Add book to cache
        cache.put(key, value);
    }
}
