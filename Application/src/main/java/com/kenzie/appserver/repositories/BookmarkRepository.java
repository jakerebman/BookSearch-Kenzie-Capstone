package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.BookmarkRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface BookmarkRepository extends CrudRepository<BookmarkRecord, String> {
}
