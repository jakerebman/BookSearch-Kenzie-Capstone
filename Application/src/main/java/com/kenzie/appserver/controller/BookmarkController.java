package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.service.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

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
}
