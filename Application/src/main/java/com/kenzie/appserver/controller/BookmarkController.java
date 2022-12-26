package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    BookmarkController(BookmarkService bookmarkService) { this.bookmarkService = bookmarkService; }

    @PostMapping
    public ResponseEntity<BookmarkResponse> createNewBookmark(@RequestBody CreateBookmarkRequest createBookmarkRequest) {
        if (createBookmarkRequest.getAuthor() == null || createBookmarkRequest.getAuthor().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Bookmark Author.");
        }

        BookmarkResponse response = bookmarkService.addNewBookmark(createBookmarkRequest);

        return ResponseEntity.created(URI.create("/bookmarks" + response.getBookmarkId())).body(response);
    }

    @PostMapping("{/bookmarkId}")
    public ResponseEntity<BookmarkResponse> updateBookmarkStatusById(@RequestBody BookmarkUpdateRequest bookmarkUpdateRequest) {
        BookmarkResponse bookmarkResponse = bookmarkService.updateBookmarkStatus(bookmarkUpdateRequest.getBookmarkId(),
                bookmarkUpdateRequest.getStatus());
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
        BookmarkResponse bookmarkResponse = bookmarkService.getBookMark(bookmarkId);
        if (bookmarkResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookmarkResponse);
    }
}
