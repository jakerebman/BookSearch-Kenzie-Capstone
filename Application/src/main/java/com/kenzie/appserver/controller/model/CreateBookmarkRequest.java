package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateBookmarkRequest {


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

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Image_URL")
    private String imageURL;

    @NotEmpty
    @JsonProperty("Read_Status")
    private String readStatus;

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

    public String getDescription(){return description;}

    public void setDescription(String description) { this.description = description;}

    public String getImageURL(){return imageURL;}

    public void setImageURL(String imageURL){this.imageURL = imageURL;}

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
