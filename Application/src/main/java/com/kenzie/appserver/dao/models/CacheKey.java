package com.kenzie.appserver.dao.models;

import java.util.Objects;

public class CacheKey {
    public static final String GSI = "gsi"; //not sure if we need this??

    private String bookId;
    private String genre;
    private String author;

    public CacheKey(String bookId){
        this.bookId = bookId;
    }

    public CacheKey(String bookId, String author){
        this.bookId = bookId;
        this.author = author;
    }


    public String getBookId(){
        return this.bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return Objects.equals(bookId, cacheKey.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}
