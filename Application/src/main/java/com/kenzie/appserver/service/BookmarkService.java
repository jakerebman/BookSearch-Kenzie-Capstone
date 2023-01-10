package com.kenzie.appserver.service;


import com.google.common.cache.Cache;
import com.kenzie.appserver.BookSearchRecommendationsNotFoundException;
import com.kenzie.appserver.SortByStatusComparator;
import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.repositories.BookmarkRepository;
import com.kenzie.appserver.repositories.model.BookmarkRecord;
import com.kenzie.capstone.service.client.BookSearchServiceClient;


import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.appserver.controller.model.BookSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookSearchServiceClient bookSearchServiceClient;
    private CacheStore cache;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, BookSearchServiceClient bookSearchServiceClient, CacheStore cache) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookSearchServiceClient = bookSearchServiceClient;
        this.cache = cache;
    }

    // TODO: The following needs to be pulled from cache: title, author, genre, numPages, Isbn13, description and imageurl
    // TODO: The frontend needs to pass the read status chosen by the user to the backend to store in the db
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
                        bookmarkRepository.save(bookmarkRecord.get());
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
    // TODO: Do we want to return a list of BookmarkResponses which includes all data - relevant if caching?
    // Or use the BookmarkUpdateResponse and only return what's needed for rendering the front page?
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
                    .map(this::recordToResponse)
                    .get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bookmarkId not found");
        }
    }

//    public List<BookSearchResponse> getBooksByGenre(String genre){
//        return Optional.ofNullable(bookSearchServiceClient.getBookRecommendationsByGenre(genre))
//                .orElse(Collections.emptyList());
//    }
public List<BookSearchResponse> getBooksByGenre(String genre){

    List<BookSearch> books = bookSearchServiceClient.getBookRecommendationsByGenre(genre);

//    if (books == null) {
//        return ResponseEntity.notFound().build();
//    }
    return Optional.ofNullable(books)
            .orElseThrow(BookSearchRecommendationsNotFoundException::new)
            .stream()
            .map(this::convertBookSearchToResponse)
            .collect(Collectors.toList());
}

//    public List<BookSearchResponse> getBooksByAuthor(String author){
//        return Optional.ofNullable(bookSearchServiceClient.getBookRecommendationsByAuthor(author))
//                .orElse(Collections.emptyList());
//    }
    public List<BookSearchResponse> getBooksByAuthor(String author) {
        List<BookSearch> books = bookSearchServiceClient.getBookRecommendationsByAuthor(author);

        return Optional.ofNullable(books)
                .orElseThrow(BookSearchRecommendationsNotFoundException::new)
                .stream()
                .map(this::convertBookSearchToResponse)
                .collect(Collectors.toList());
    }

//    public BookSearchResponse getBook(String bookSearchId){
//        return Optional.ofNullable(bookSearchServiceClient.getBookSearch(bookSearchId))
//                .orElse(new BookSearchResponse());
//    }

    public BookSearchResponse getBook(String bookSearchId){
        BookSearchResponse cachedBook = cache.get(bookSearchId);

        if(cachedBook != null) {
            return cachedBook;
        } else {
            BookSearch nonCachedBook = bookSearchServiceClient.getBookSearch(bookSearchId);
            if (nonCachedBook == null){
                throw new IllegalArgumentException();
            }
            BookSearchResponse response = convertBookSearchToResponse(nonCachedBook);
            cache.add(bookSearchId, response);
            return response;
    }
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

    private BookSearchResponse convertBookSearchToResponse(BookSearch bookSearch) {
        BookSearchResponse bookSearchResponse = new BookSearchResponse();
        bookSearchResponse.setBookSearchId(bookSearch.getBookSearchId());
        bookSearchResponse.setTitle(bookSearch.getTitle());
        bookSearchResponse.setAuthor(bookSearch.getAuthor());
        bookSearchResponse.setGenre(bookSearch.getGenre());
        bookSearchResponse.setNumPages(bookSearch.getNumPages());
        bookSearchResponse.setIsbn13(bookSearch.getIsbn13());
        bookSearchResponse.setDescription(bookSearch.getDescription());
        bookSearchResponse.setImageURL(bookSearch.getImageURL());
        return bookSearchResponse;
    }
}
