package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookSearchRequest;

public class JsonStringToReferralConverter {

    public BookSearchRequest convert(String body){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BookSearchRequest request = gson.fromJson(body, BookSearchRequest.class);
            return  request;
        }catch (Exception e){
            throw new InvalidDataException("Invalid Json Body", e);
        }
    }
}
