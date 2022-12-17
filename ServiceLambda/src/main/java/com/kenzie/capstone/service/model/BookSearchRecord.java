package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.List;

@DynamoDBTable(tableName = "BookSearch")
public class BookSearchRecord {

    private String bookSearchId;

    private String title;

    private String author;

    private String genre;

    private String numPages;

    private String isbn13;

    private String description;

    @DynamoDBHashKey(attributeName = "bookSearchId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"AuthorIndex", "GenreIndex"}, attributeName = "bookSearchId")
    public String getBookSearchId() {
        return bookSearchId;
    }

    public void setBookSearchId(String bookSearchId) {
        this.bookSearchId = bookSearchId;
    }

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "author")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "AuthorIndex", attributeName = "author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DynamoDBAttribute(attributeName = "genre")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "GenreIndex", attributeName = "genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @DynamoDBAttribute(attributeName = "numPages")
    public String getNumPages() {
        return numPages;
    }

    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    @DynamoDBAttribute(attributeName = "isbn13")
    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
