package com.kenzie.appserver.service;

import com.kenzie.appserver.SortByStatusComparator;
import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.repositories.BookmarkRepository;
import com.kenzie.appserver.repositories.model.BookmarkRecord;
import com.kenzie.capstone.service.client.BookSearchServiceClient;
import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.capstone.service.model.BookSearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookSearchServiceClient client;

    public BookmarkService(BookmarkRepository bookmarkRepository, BookSearchServiceClient client ){
        this.bookmarkRepository = bookmarkRepository;
        this.client = client;
    }

    public BookmarkResponse addNewBookmark(CreateBookmarkRequest createBookmarkRequest){
        BookmarkRecord record = new BookmarkRecord();
        record.setBookmarkId(UUID.randomUUID().toString());
        record.setBookmarkCreationDate(LocalDateTime.now().toString());
        record.setTitle(createBookmarkRequest.getTitle());
        record.setAuthor(createBookmarkRequest.getAuthor());
        record.setGenre(createBookmarkRequest.getGenre());
        record.setNumPages(createBookmarkRequest.getNumPages());
        record.setIsbn13(createBookmarkRequest.getIsbn13());
        record.setDescription(createBookmarkRequest.getDescription());
        record.setImageURL(createBookmarkRequest.getImageURL());
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

    //Todo: Talk about what we actually want to send to frontend
    public List<BookmarkResponse> getAllBookMarksByStatus(){
        List<BookmarkResponse> responses = new ArrayList<>();
        for(BookmarkRecord record : bookmarkRepository.findAll()){
                responses.add(recordToResponse(record));
        }
        responses.sort(new SortByStatusComparator());
        return responses;
    }

    public BookmarkResponse getBookMark(String bookmarkId){
        if (bookmarkRepository.findById(bookmarkId).isPresent()){
            return bookmarkRepository.findById(bookmarkId)
                    .map(this::recordToPartialResponse)
                    .get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookmarkId not found");
        }
    }

    public List<BookSearchResponse> getBooksByGenre(String genre){
        return Optional.ofNullable(client.getBookRecommendationsByGenre(genre))
                .orElse(Collections.emptyList());

    }

    public List<BookSearchResponse> getBooksByAuthor(String author){
        return Optional.ofNullable(client.getBookRecommendationsByAuthor(author))
                .orElse(Collections.emptyList());

    }

    public BookSearchResponse getBook(String bookSearchId){
        return Optional.ofNullable(client.getBookSearch(bookSearchId))
                .orElse(new BookSearchResponse());

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
        bookmarkResponse.setDescription(bookmarkRecord.getDescription());
        bookmarkResponse.setImageURL(bookmarkRecord.getImageURL());
        bookmarkResponse.setReadStatus(bookmarkRecord.getReadStatus());

        return bookmarkResponse;
    }

    private BookmarkResponse recordToPartialResponse(BookmarkRecord record){
        if (record == null){
            return null;
        }
        BookmarkResponse response = new BookmarkResponse();
        response.setTitle(record.getTitle());
        response.setReadStatus(record.getReadStatus());
        return response;
    }

}
