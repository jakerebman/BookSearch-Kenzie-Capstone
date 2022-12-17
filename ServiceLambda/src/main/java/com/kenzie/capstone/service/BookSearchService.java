package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.BookSearchConverter;
import com.kenzie.capstone.service.dao.BookSearchDao;
import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.capstone.service.model.BookSearchRecord;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


public class BookSearchService {

    private BookSearchDao bookSearchDao;

    private BookSearchConverter converter = new BookSearchConverter();

    @Inject
    public BookSearchService(BookSearchDao bookSearchDao){
        this.bookSearchDao = bookSearchDao;
    }

    public List<BookSearch> getRecommendationsByGenre(String genre){
        List<BookSearchRecord> recommendationRecords = bookSearchDao.getRecommendationbyGenre(genre);
        return recommendationRecords.stream()
                .map(record -> converter.fromRecordToBookSearch(record))
                .collect(Collectors.toList());
    }

    public List<BookSearch> getRecommendationsByAuthor(String author){
        List<BookSearchRecord> records = bookSearchDao.getRecommendationbyAuthor(author);
        return records.stream()
                .map(record -> converter.fromRecordToBookSearch(record))
                .collect(Collectors.toList());
    }
}
