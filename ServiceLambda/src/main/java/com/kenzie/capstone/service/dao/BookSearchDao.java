package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.kenzie.capstone.service.model.BookSearchRecord;

import java.util.List;

public class BookSearchDao {

    private final DynamoDBMapper mapper;

    public BookSearchDao(final DynamoDBMapper mapper){this.mapper = mapper;}

    public List<BookSearchRecord> getRecommendationbyGenre(String genre){
        BookSearchRecord record = new BookSearchRecord();
        record.setGenre(genre);

        DynamoDBQueryExpression<BookSearchRecord> queryExpression = new DynamoDBQueryExpression<BookSearchRecord>()
                .withHashKeyValues(record)
                .withIndexName("GenreIndex")
                .withConsistentRead(false);

        return mapper.query(BookSearchRecord.class, queryExpression);
    }
    public List<BookSearchRecord> getRecommendationbyAuthor(String author) {
        BookSearchRecord record = new BookSearchRecord();
        record.setGenre(author);

        DynamoDBQueryExpression<BookSearchRecord> queryExpression = new DynamoDBQueryExpression<BookSearchRecord>()
                .withHashKeyValues(record)
                .withIndexName("AuthorIndex")
                .withConsistentRead(false);

        return mapper.query(BookSearchRecord.class, queryExpression);
    }
}
