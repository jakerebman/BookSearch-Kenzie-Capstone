package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.capstone.service.model.BookSearchRecord;
import com.kenzie.capstone.service.model.BookSearchRequest;
import com.kenzie.capstone.service.model.BookSearchResponse;

public class BookSearchConverter {

    public BookSearchRecord fromRequestToRecord(BookSearchRequest request){
        BookSearchRecord record = new BookSearchRecord();
        record.setBookSearchId(request.getBookSearchId());
        record.setTitle(request.getTitle());
        record.setAuthor(request.getAuthor());
        record.setGenre(request.getGenre());
        record.setNumPages(request.getNumPages());
        record.setIsbn13(request.getIsbn13());
        record.setDescription(request.getDescription());
        record.setImageURL(request.getImageURL());
        return record;
    }

    public BookSearchResponse fromRecordToResponse(BookSearchRecord record){
        BookSearchResponse response = new BookSearchResponse();
        response.setBookSearchId(record.getBookSearchId());
        response.setTitle(record.getTitle());
        response.setAuthor(record.getAuthor());
        response.setGenre(record.getGenre());
        response.setNumPages(record.getNumPages());
        response.setIsbn13(record.getIsbn13());
        response.setDescription(record.getDescription());
        response.setImageURL(record.getImageURL());
        return response;
    }

    public BookSearch fromRecordToBookSearch(BookSearchRecord record){
        BookSearch bookSearch = new BookSearch();
        bookSearch.setBookSearchId(record.getBookSearchId());
        bookSearch.setTitle(record.getTitle());
        bookSearch.setAuthor(record.getAuthor());
        bookSearch.setGenre(record.getGenre());
        bookSearch.setNumPages(record.getNumPages());
        bookSearch.setIsbn13(record.getIsbn13());
        bookSearch.setDescription(record.getDescription());
        bookSearch.setImageURL(record.getImageURL());
        return bookSearch;
    }
}
