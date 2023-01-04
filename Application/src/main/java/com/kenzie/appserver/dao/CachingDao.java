package com.kenzie.appserver.dao;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kenzie.capstone.service.model.BookSearch;

import javax.inject.Inject;

public class CachingDao {
    private final LoadingCache<String, BookSearch> searchCache;

    @Inject
    public CachingDao(final BookSearchDao searchDao){
        searchCache = CacheBuilder.newBuilder()
                .build(CacheLoader.from(searchDao::getBook));
    }

    public BookSearch getBook(String bookId){
        return searchCache.getUnchecked(bookId);
    }
}
