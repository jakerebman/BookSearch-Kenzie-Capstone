package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.BookmarkService;
import com.kenzie.appserver.controller.model.BookSearchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    static final Logger log = LogManager.getLogger();

    BookmarkController(BookmarkService bookmarkService) { this.bookmarkService = bookmarkService; }

    @PostMapping
    public ResponseEntity<BookmarkResponse> createNewBookmark(@RequestBody CreateBookmarkRequest createBookmarkRequest) {
        if (createBookmarkRequest.getAuthor() == null || createBookmarkRequest.getAuthor().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Bookmark Author.");
        }

        BookmarkResponse response = bookmarkService.addNewBookmark(createBookmarkRequest);

        log.info(String.format("Bookmark ID: %s", response.getBookmarkId()));

        if (response.getBookmarkId() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Bookmark ID invalid.");
        }

        return ResponseEntity.created(URI.create("/bookmarks" + response.getBookmarkId())).body(response);
    }

    @PutMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkResponse> updateBookmarkStatusById(@RequestBody BookmarkUpdateRequest bookmarkUpdateRequest) {

        log.info(String.format("Bookmark ID: %s", bookmarkUpdateRequest.getBookmarkId()));

        if (bookmarkUpdateRequest.getBookmarkId() == null || bookmarkUpdateRequest.getBookmarkId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "BookmarkId is null.");
        }

        BookmarkResponse bookmarkResponse = bookmarkService.updateBookmarkStatus(bookmarkUpdateRequest.getBookmarkId(),
                bookmarkUpdateRequest.getStatus());

        log.info(String.format("Bookmark ID: %s", bookmarkResponse.getBookmarkId()));

        return ResponseEntity.ok(bookmarkResponse);
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity deleteBookmarkById(@PathVariable("bookmarkId") String bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getAllBookmarksByStatus() {
        List<BookmarkResponse> bookmarkResponseList = bookmarkService.getAllBookMarksByStatus();
        if (bookmarkResponseList == null || bookmarkResponseList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookmarkResponseList);
    }

    @GetMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkResponse> getBookmarkById(@PathVariable("bookmarkId") String bookmarkId) {
        try {
            BookmarkResponse bookmarkResponse = bookmarkService.getBookMark(bookmarkId);
            if (bookmarkResponse == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bookmarkResponse);
        }catch (ResponseStatusException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{author}/authors")
    public ResponseEntity<List<BookSearchResponse>> getBooksByAuthor(@PathVariable("author") String author) {
        List<BookSearchResponse> books = bookmarkService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{genre}/genres")
    public ResponseEntity<List<BookSearchResponse>> getBooksByGenre(@PathVariable("genre") String genre) {
        List<BookSearchResponse> books = bookmarkService.getBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookSearchId}/books")
    public ResponseEntity<BookSearchResponse> getBookById(@PathVariable("bookSearchId") String bookSearchId) {
        BookSearchResponse bookSearchResponse = bookmarkService.getBook(bookSearchId);
        if (bookSearchResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookSearchResponse);
    }
}
