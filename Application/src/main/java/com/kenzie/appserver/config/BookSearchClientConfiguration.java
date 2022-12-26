package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.BookSearchServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookSearchClientConfiguration {

    @Bean
    public BookSearchServiceClient bookSearchServiceClient() { return new BookSearchServiceClient(); }
}
