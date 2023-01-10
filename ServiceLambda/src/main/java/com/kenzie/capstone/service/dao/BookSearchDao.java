package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.BookSearchRecord;

import java.util.List;

public class BookSearchDao {

    private final DynamoDBMapper mapper;

    public BookSearchDao(final DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public List<BookSearchRecord> getRecommendationbyGenre(String genre) {
        BookSearchRecord record = new BookSearchRecord();
        record.setGenre(genre);
        try {
            DynamoDBQueryExpression<BookSearchRecord> queryExpression = new DynamoDBQueryExpression<BookSearchRecord>()
                    .withHashKeyValues(record)
                    .withIndexName("GenreIndex")
                    .withConsistentRead(false);

            return mapper.query(BookSearchRecord.class, queryExpression);
        }catch (Exception e){
            throw new InvalidDataException(String.format("failed to retrieve recommendation for genre: %s \n",genre), e);
        }
    }

    public List<BookSearchRecord> getRecommendationbyAuthor(String author) {
        BookSearchRecord record = new BookSearchRecord();
        record.setAuthor(author);

        try {
            DynamoDBQueryExpression<BookSearchRecord> queryExpression = new DynamoDBQueryExpression<BookSearchRecord>()
                   .withHashKeyValues(record)
                   .withIndexName("AuthorIndex")
                   .withConsistentRead(false);

            return mapper.query(BookSearchRecord.class, queryExpression);
        } catch (Exception e) {
            throw new InvalidDataException(String.format("failed to retrieve recommendation for author: %s \n",author), e);
        }
    }

    public BookSearchRecord getBookSearch(String bookSearchId) {
        BookSearchRecord record = new BookSearchRecord();
        record.setBookSearchId(bookSearchId);
        try {
            return mapper.load(record);
        } catch (Exception e) {
            throw new InvalidDataException(String.format("failed to retrieve book for id: %s \n",bookSearchId), e);
        }
    }
}
