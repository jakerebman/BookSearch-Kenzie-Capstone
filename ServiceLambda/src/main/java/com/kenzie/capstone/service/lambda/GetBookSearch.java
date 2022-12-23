package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.BookSearchService;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GetBookSearch implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        log.info(gson.toJson("input"));
        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        BookSearchService bookSearchService = serviceComponent.provideBookSearchService();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String bookSearchId = input.getPathParameters().get("Book_Search_Id");

        if (bookSearchId == null || bookSearchId.length() == 0){
            return response
                    .withStatusCode(400)
                    .withBody("Id is null");
        }

        try {
            String output = gson.toJson(bookSearchService.getBookSearch(bookSearchId));
            return response
                    .withStatusCode(200)
                    .withBody(output);
        }catch (InvalidDataException e){
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e));
        }
    }
}
