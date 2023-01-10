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

    private String imageURL;

    @DynamoDBHashKey(attributeName = "Book_Search_Id")
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"AuthorIndex", "GenreIndex"}, attributeName = "Book_Search_Id")
    public String getBookSearchId() {
        return bookSearchId;
    }

    public void setBookSearchId(String bookSearchId) {
        this.bookSearchId = bookSearchId;
    }

    @DynamoDBAttribute(attributeName = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "Author")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "AuthorIndex", attributeName = "Author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DynamoDBAttribute(attributeName = "Genre")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "GenreIndex", attributeName = "Genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @DynamoDBAttribute(attributeName = "Num_Pages")
    public String getNumPages() {
        return numPages;
    }

    public void setNumPages(String numPages) {
        this.numPages = numPages;
    }

    @DynamoDBAttribute(attributeName = "ISBN13")
    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "Image_URL")
    public String getImageURL(){return imageURL;}

    public void setImageURL(String imageURL){this.imageURL = imageURL;}
}
