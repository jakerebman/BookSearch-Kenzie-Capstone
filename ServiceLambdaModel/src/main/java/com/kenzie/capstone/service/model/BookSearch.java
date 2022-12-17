package com.kenzie.capstone.service.model;

import java.util.List;
import java.util.Objects;

public class BookSearch {

    private String bookSearchId;

    private String title;

    private String author;

    private String genre;

    private String numPages;

    private String isbn13;

    private String description;

    public String getBookSearchId(){return bookSearchId;}

    public void setBookSearchId(String bookSearchId){this.bookSearchId = bookSearchId;}

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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSearch bookRecommendation = (BookSearch) o;
        return Objects.equals(isbn13, bookRecommendation.isbn13);
    }

    @Override
    public int hashCode(){return Objects.hash(isbn13);}

    @Override
    public String toString(){
        return "Book Recommendation" + "\n"
                + "title: " + title + "\n"
                + "author: " + author;
    }
}
