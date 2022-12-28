package com.kenzie.appserver.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kenzie.appserver.dao.models.CacheKey;

import javax.inject.Inject;

public class CachingDao {
    private final LoadingCache<CacheKey, Boolean> searchCache;

    @Inject
    public CachingDao(final SearchDao searchDao){
        searchCache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(searchDao::isUserInGroup)); //TODO: method to pass in from the regular dao
    }

    public boolean method(CacheKey cacheKey){// TODO: figure out what this method is doing and what it should be named
        return searchCache.getUnchecked(cacheKey);
    }
}
