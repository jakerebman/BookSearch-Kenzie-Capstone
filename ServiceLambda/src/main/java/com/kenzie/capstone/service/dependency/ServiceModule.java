package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.BookSearchService;
import com.kenzie.capstone.service.dao.BookSearchDao;

import com.kenzie.capstone.service.model.BookSearch;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public BookSearchService provideBookSearchService(@Named("BookSearchDao") BookSearchDao bookSearchDao) {
        return new BookSearchService(bookSearchDao);
    }

//    @Singleton
//    @Provides
//    @Inject
//    public BookSearchService provideBookSearchService(@Named("ExampleDao") BookSearch exampleDao) {
//        return new LambdaService(exampleDao);
//    }
}

