package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.BookSearch;

import java.util.List;

public class BookSearchServiceClient {

    //endpoints (I am not sure what exactly they will be)

    private static final String GET_BOOK_RECOMMENDATION_BY_GENRE = "BookRecommendation/list/{genre}";

    private static final String GET_BOOK_RECOMMENDATION_BY_AUTHOR = "BookSearch/list/{author}";

    private ObjectMapper mapper;

    public BookSearchServiceClient(){this.mapper = new ObjectMapper();}

    public List<BookSearch> getBookRecommendationsByGenre(){
        EndpointUtility utility = new EndpointUtility();
        String response = utility.getEndpoint(GET_BOOK_RECOMMENDATION_BY_GENRE);
        List<BookSearch> recommendation;
        try{
            recommendation = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }

        return recommendation;
    }

    public List<BookSearch> getBookRecommendationsByAuthor(){
        EndpointUtility utility = new EndpointUtility();
        String response = utility.getEndpoint(GET_BOOK_RECOMMENDATION_BY_AUTHOR);
        List<BookSearch> recommendation;
        try{
            recommendation = mapper.readValue(response, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }
        return recommendation;
    }
}
