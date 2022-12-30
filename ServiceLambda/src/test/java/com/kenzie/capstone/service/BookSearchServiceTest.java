package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.BookSearchDao;
import com.kenzie.capstone.service.model.BookSearch;
import com.kenzie.capstone.service.model.BookSearchRecord;
import com.kenzie.capstone.service.model.BookSearchResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class BookSearchServiceTest {

    @Mock
    private BookSearchDao dao;

    @InjectMocks
    private BookSearchService service;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getRecommendationsByGenre_validGenre_returnsListOfBookSearch(){
        String genre = "Fantasy";
        List<BookSearchRecord> records = createBookSearchList();
        when(dao.getRecommendationbyGenre(genre)).thenReturn(records);

        List<BookSearch> result = service.getRecommendationsByGenre(genre);

        Assertions.assertEquals(records.get(0).getTitle(), result.get(0).getTitle());
        Assertions.assertEquals(records.get(0).getAuthor(), result.get(0).getAuthor());
        Assertions.assertEquals(records.get(0).getGenre(), result.get(0).getGenre());
        Assertions.assertEquals(records.get(0).getBookSearchId(), result.get(0).getBookSearchId());
        Assertions.assertEquals(records.get(0).getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(records.get(0).getImageURL(), result.get(0).getImageURL());
        Assertions.assertEquals(records.get(0).getIsbn13(), result.get(0).getIsbn13());
        Assertions.assertEquals(records.get(0).getNumPages(), result.get(0).getNumPages());
        Assertions.assertEquals(records.get(1).getTitle(), result.get(1).getTitle());
        Assertions.assertEquals(records.get(1).getAuthor(), result.get(1).getAuthor());
        Assertions.assertEquals(records.get(1).getGenre(), result.get(1).getGenre());
        Assertions.assertEquals(records.get(1).getBookSearchId(), result.get(1).getBookSearchId());
        Assertions.assertEquals(records.get(1).getDescription(), result.get(1).getDescription());
        Assertions.assertEquals(records.get(1).getImageURL(), result.get(1).getImageURL());
        Assertions.assertEquals(records.get(1).getIsbn13(), result.get(1).getIsbn13());
        Assertions.assertEquals(records.get(1).getNumPages(), result.get(1).getNumPages());
    }

    @Test
    void getRecommendationsByAuthor_validAuthor_returnsListOfBookSearch(){
        String author = "JK Rowling";
        List<BookSearchRecord> records = createBookSearchList();
        when(dao.getRecommendationbyAuthor(author)).thenReturn(records);

        List<BookSearch> result = service.getRecommendationsByAuthor(author);

        Assertions.assertEquals(records.get(0).getTitle(), result.get(0).getTitle());
        Assertions.assertEquals(records.get(0).getAuthor(), result.get(0).getAuthor());
        Assertions.assertEquals(records.get(0).getGenre(), result.get(0).getGenre());
        Assertions.assertEquals(records.get(0).getBookSearchId(), result.get(0).getBookSearchId());
        Assertions.assertEquals(records.get(0).getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(records.get(0).getImageURL(), result.get(0).getImageURL());
        Assertions.assertEquals(records.get(0).getIsbn13(), result.get(0).getIsbn13());
        Assertions.assertEquals(records.get(0).getNumPages(), result.get(0).getNumPages());
        Assertions.assertEquals(records.get(1).getTitle(), result.get(1).getTitle());
        Assertions.assertEquals(records.get(1).getAuthor(), result.get(1).getAuthor());
        Assertions.assertEquals(records.get(1).getGenre(), result.get(1).getGenre());
        Assertions.assertEquals(records.get(1).getBookSearchId(), result.get(1).getBookSearchId());
        Assertions.assertEquals(records.get(1).getDescription(), result.get(1).getDescription());
        Assertions.assertEquals(records.get(1).getImageURL(), result.get(1).getImageURL());
        Assertions.assertEquals(records.get(1).getIsbn13(), result.get(1).getIsbn13());
        Assertions.assertEquals(records.get(1).getNumPages(), result.get(1).getNumPages());
    }

    @Test
    void getBookSearch_validBookSearchId_returnsBookSearch(){
        String id = UUID.randomUUID().toString();
        BookSearchRecord record = new BookSearchRecord();
        record.setBookSearchId(UUID.randomUUID().toString());
        record.setTitle("Harry Potter and the Philosopher's Stone");
        record.setAuthor("JK Rowling");
        record.setGenre("Fantasy");
        record.setNumPages("223");
        record.setIsbn13("978-0-7475-3269-9");
        record.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        record.setImageURL("fakeURL");

        when(dao.getBookSearch(id)).thenReturn(record);

        BookSearch result = service.getBookSearch(id);

        Assertions.assertEquals(record.getTitle(), result.getTitle());
        Assertions.assertEquals(record.getBookSearchId(), result.getBookSearchId());
        Assertions.assertEquals(record.getNumPages(), result.getNumPages());
        Assertions.assertEquals(record.getGenre(), result.getGenre());
        Assertions.assertEquals(record.getDescription(), result.getDescription());
        Assertions.assertEquals(record.getIsbn13(), result.getIsbn13());
        Assertions.assertEquals(record.getImageURL(), result.getImageURL());
        Assertions.assertEquals(record.getAuthor(), result.getAuthor());
    }

    private List<BookSearchRecord> createBookSearchList(){
        List<BookSearchRecord> records = new ArrayList<>();
        BookSearchRecord record = new BookSearchRecord();
        record.setBookSearchId(UUID.randomUUID().toString());
        record.setTitle("Harry Potter and the Philosopher's Stone");
        record.setAuthor("JK Rowling");
        record.setGenre("Fantasy");
        record.setNumPages("223");
        record.setIsbn13("978-0-7475-3269-9");
        record.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        record.setImageURL("fakeURL");

        BookSearchRecord record2 = new BookSearchRecord();
        record2.setBookSearchId(UUID.randomUUID().toString());
        record2.setTitle("Harry Potter and the Chamber of Secrets");
        record2.setAuthor("JK Rowling");
        record2.setGenre("Fantasy");
        record2.setNumPages("251");
        record2.setIsbn13("0-7475-3849-2");
        record2.setDescription("Harry and his friends are now forced to secretly uncover the truth" +
                " about the chamber before the school closes or any lives are taken.");
        record2.setImageURL("fakeURL");

        records.add(record);
        records.add(record2);

        return records;
    }
}
