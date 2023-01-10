package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.BookSearchService;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Declares the dependency roots that Dagger will provide.
 */
@Singleton
@Component(modules = {DaoModule.class, ServiceModule.class})
public interface ServiceComponent {
    BookSearchService provideBookSearchService();
}
