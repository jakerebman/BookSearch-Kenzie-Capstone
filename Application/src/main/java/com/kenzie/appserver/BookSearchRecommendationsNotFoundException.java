package com.kenzie.appserver;

public class BookSearchRecommendationsNotFoundException extends RuntimeException {

    public BookSearchRecommendationsNotFoundException(){
        System.out.println("No Recommendation for Genre or Author");
    }

    public BookSearchRecommendationsNotFoundException(String message){
        super(message);
    }

    public BookSearchRecommendationsNotFoundException(String message, Throwable e){
        super(message, e);
    }

    public BookSearchRecommendationsNotFoundException(Throwable e){
        super(e);
    }
}
