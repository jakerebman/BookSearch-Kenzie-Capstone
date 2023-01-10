package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "Bookmark")
public class BookmarkRecord {

    // TODO: Do we want to change these names?
    public static final String READ_STATUS_GSI = "read_status";

    // TODO: Do we want to use underscores or camelcase?
    private String bookmarkId;

    // TODO: Do we want to save this as a String or use a ZoneDateTime converter class?
    private String bookmarkCreationDate;
    private String title;
    private String author;
    private String genre;
    private String numPages;
    private String isbn13;

    private String description;

    private String imageURL;
    private String readStatus;

    @DynamoDBHashKey(attributeName = "Bookmark_Id")
    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    @DynamoDBIndexRangeKey(globalSecondaryIndexName = READ_STATUS_GSI)
    @DynamoDBAttribute(attributeName = "Bookmark_Creation_Date")
    public String getBookmarkCreationDate() {
        return bookmarkCreationDate;
    }

    public void setBookmarkCreationDate(String bookmarkCreationDate) {
        this.bookmarkCreationDate = bookmarkCreationDate;
    }

    @DynamoDBAttribute(attributeName = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "Author")
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @DynamoDBAttribute(attributeName = "Genre")
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
    public String getDescription(){return description;}

    public void setDescription(String description) { this.description = description;}

    @DynamoDBAttribute(attributeName = "Image_URL")
    public String getImageURL(){return imageURL;}

    public void setImageURL(String imageURL){this.imageURL = imageURL;}

    @DynamoDBIndexHashKey(globalSecondaryIndexName = READ_STATUS_GSI)
    @DynamoDBAttribute(attributeName = "Read_Status")
    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkRecord that = (BookmarkRecord) o;
        return Objects.equals(bookmarkId, that.bookmarkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookmarkId);
    }
}
