package com.kenzie.appserver.service.model;

public class BookSearch {

    private String bookSearchId;

    private String title;

    private String author;

    private String genre;

    private String numPages;

    private String isbn13;

    private String description;

    private String imageURL;

    public BookSearch(String bookSearchId, String title, String author, String genre, String numPages, String isbn13, String description, String imageURL) {
        this.bookSearchId = bookSearchId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.numPages = numPages;
        this.isbn13 = isbn13;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getBookSearchId() {
        return bookSearchId;
    }

    public void setBookSearchId(String bookSearchId) {
        this.bookSearchId = bookSearchId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
