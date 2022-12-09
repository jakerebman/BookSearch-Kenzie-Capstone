package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.repositories.BookmarkRepository;
import com.kenzie.appserver.repositories.model.BookmarkRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookmarkService {

    private BookmarkRepository bookmarkRepository;
    //private BookServiceClient bookServiceClient;

    public BookmarkService(BookmarkRepository bookmarkRepository/*,BookServiceClient bookServiceClient */ ){
        this.bookmarkRepository = bookmarkRepository;
        //this.bookServiceClient = bookServiceClient;
    }

    public BookmarkResponse addNewBookmark(CreateBookmarkRequest createBookmarkRequest){
        // Do I need to do any null or empty checks, or since we are pulling from API, do we know the correct info comes over everytime?
        BookmarkRecord record = new BookmarkRecord();
        record.setBookmarkId(createBookmarkRequest.getBookmarkId());
        record.setBookmarkCreationDate(LocalDateTime.now().toString()); //is this where we want to set this?
        record.setTitle(createBookmarkRequest.getTitle());
        record.setAuthor(createBookmarkRequest.getAuthor());
        record.setGenre(createBookmarkRequest.getGenre());
        record.setNumPages(createBookmarkRequest.getNumPages());
        record.setIsbn13(createBookmarkRequest.getIsbn13());
        record.setReadStatus(createBookmarkRequest.getReadStatus());

        bookmarkRepository.save(record);

        return recordToResponse(record);
    }

    public BookmarkResponse updateBookmarkStatus(String bookmarkId, String status){
         return Optional.of(bookmarkRepository.findById(bookmarkId))
                 .stream()
                 .peek(bookmarkRecord -> {
                     if(bookmarkRecord.isPresent()){
                     bookmarkRecord.get().setReadStatus(status);
                     }else{
                     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bookmark not found");
                     }
                 })
                 .map(bookmarkRecord -> recordToResponse(bookmarkRecord.get()))
                 .findAny().get();

    }

    public void deleteBookmark(String bookmarkId){
        bookmarkRepository.deleteById(bookmarkId);
    }

    private BookmarkResponse recordToResponse(BookmarkRecord bookmarkRecord){
        if(bookmarkRecord == null){
            return null;
        }
        BookmarkResponse bookmarkResponse = new BookmarkResponse();
        bookmarkResponse.setBookmarkId(bookmarkRecord.getBookmarkId());
        bookmarkResponse.setBookmarkCreationDate(bookmarkRecord.getBookmarkCreationDate());
        bookmarkResponse.setTitle(bookmarkRecord.getTitle());
        bookmarkResponse.setAuthor(bookmarkRecord.getAuthor());
        bookmarkResponse.setGenre(bookmarkRecord.getGenre());
        bookmarkResponse.setNumPages(bookmarkRecord.getNumPages());
        bookmarkResponse.setIsbn13(bookmarkRecord.getIsbn13());
        bookmarkResponse.setReadStatus(bookmarkRecord.getReadStatus());

        return bookmarkResponse;
    }

}
