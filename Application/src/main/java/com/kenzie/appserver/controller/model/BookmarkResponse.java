package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookmarkResponse {

    @JsonProperty("Bookmark_Id")
    private String bookmarkId;

    @JsonProperty("Bookmark_Creation_Date")
    private String bookmarkCreationDate;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Author")
    private String author;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Num_Pages")
    private String numPages;

    @JsonProperty("ISBN13")
    private String isbn13;

    @JsonProperty("Read_Status")
    private String readStatus;

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getBookmarkCreationDate() {
        return bookmarkCreationDate;
    }

    public void setBookmarkCreationDate(String bookmarkCreationDate) {
        this.bookmarkCreationDate = bookmarkCreationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNumPages() {
        return numPages;
    }

    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
