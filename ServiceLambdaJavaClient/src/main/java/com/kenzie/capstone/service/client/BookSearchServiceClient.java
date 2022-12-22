package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.BookSearch;

import java.util.List;

public class BookSearchServiceClient {

    //endpoints (I am not sure what exactly they will be)

    private static final String GET_BOOK_RECOMMENDATION_BY_GENRE = "BookSearch/list/{genre}";

    private static final String GET_BOOK_RECOMMENDATION_BY_AUTHOR = "BookSearch/list/{author}";

    private static final String GET_BOOK = "BookSearch/Book_Search_Id/{bookSearchId}";

    private ObjectMapper mapper;

    public BookSearchServiceClient(){this.mapper = new ObjectMapper();}

    public List<BookSearch> getBookRecommendationsByGenre(String genre){
        EndpointUtility utility = new EndpointUtility();
        String response = utility.getEndpoint(GET_BOOK_RECOMMENDATION_BY_GENRE.replace("{genre}", genre));
        List<BookSearch> recommendation;
        try{
            recommendation = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }

        return recommendation;
    }

    public List<BookSearch> getBookRecommendationsByAuthor(String author){
        EndpointUtility utility = new EndpointUtility();
        String response = utility.getEndpoint(GET_BOOK_RECOMMENDATION_BY_AUTHOR.replace("{author}", author));
        List<BookSearch> recommendation;
        try{
            recommendation = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }
        return recommendation;
    }

    public BookSearch getBookSearch(String bookSearchId){
        EndpointUtility utility = new EndpointUtility();
        String response = utility.getEndpoint(GET_BOOK.replace("{bookSearchId}", bookSearchId));
        BookSearch bookSearch;
        try{
           bookSearch = mapper.readValue(response, BookSearch.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }
        return bookSearch;
    }
}
