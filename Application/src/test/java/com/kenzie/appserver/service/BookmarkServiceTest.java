package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.repositories.BookmarkRepository;
import com.kenzie.appserver.repositories.model.BookmarkRecord;
import com.kenzie.capstone.service.client.BookSearchServiceClient;
import com.kenzie.capstone.service.model.BookSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private BookSearchServiceClient client;

    @InjectMocks
    private BookmarkService service;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addNewBookmark_validInput_responseCreated(){
        CreateBookmarkRequest request = new CreateBookmarkRequest();
        request.setTitle("Harry Potter and the Philosopher's Stone");
        request.setAuthor("JK Rowling");
        request.setGenre("Fantasy");
        request.setNumPages("223");
        request.setIsbn13("978-0-7475-3269-9");
        request.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        request.setImageURL("fakeURL");
        request.setReadStatus("Interested");

        BookmarkResponse response = service.addNewBookmark(request);

        Mockito.verify(bookmarkRepository, times(1)).save(any());

        assertNotNull(response.getBookmarkId());
        assertNotNull(response.getBookmarkCreationDate());
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getAuthor(), response.getAuthor());
        assertEquals(request.getGenre(), response.getGenre());
        assertEquals(request.getNumPages(), response.getNumPages());
        assertEquals(request.getIsbn13(), response.getIsbn13());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getImageURL(), response.getImageURL());
        assertEquals(request.getReadStatus(), response.getReadStatus());
    }

    @Test
    void updateBookmarkStatus_validInput_readStatusUpdated(){
        String newStatus = "read";

        BookmarkRecord record = createRecord();

        when(bookmarkRepository.findById(record.getBookmarkId())).thenReturn(Optional.of(record));

        BookmarkResponse response = service.updateBookmarkStatus(record.getBookmarkId(), newStatus);

        Mockito.verify(bookmarkRepository, times(1)).save(any());

        assertEquals(record.getBookmarkId(), response.getBookmarkId());
        assertEquals(record.getBookmarkCreationDate(), response.getBookmarkCreationDate());
        assertEquals(record.getTitle(), response.getTitle());
        assertEquals(record.getAuthor(), response.getAuthor());
        assertEquals(record.getGenre(), response.getGenre());
        assertEquals(record.getNumPages(), response.getNumPages());
        assertEquals(record.getIsbn13(), response.getIsbn13());
        assertEquals(record.getDescription(), response.getDescription());
        assertEquals(record.getImageURL(), response.getImageURL());
        assertEquals(newStatus, response.getReadStatus());
    }

    @Test
    void updateBookmarkStatus_invalidBookmarkId_throwsResponseStatusException(){
        String id = UUID.randomUUID().toString();
        String status = "read";
        when(bookmarkRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                ()-> service.updateBookmarkStatus(id, status),
                "Exception should be thrown when Id is not in Database");
    }

    @Test
    void deleteBookmark_validId_repositoryAccessed(){
        String id = UUID.randomUUID().toString();

        service.deleteBookmark(id);

        verify(bookmarkRepository, times(1)).deleteById(id);
    }

    @Test
    void getAllBookMarksByStatus_returnsSortedList(){ //list sorted interested first than read
        List<BookmarkRecord> records = createRecords();

        System.out.println("Record order:\n"
                + records.get(0).getTitle() + " - " + records.get(0).getReadStatus() + "\n"
                + records.get(1).getTitle() + " - " + records.get(1).getReadStatus() + "\n"
                + records.get(2).getTitle() + " - " + records.get(2).getReadStatus() + "\n"
                + records.get(3).getTitle() + " - " + records.get(3).getReadStatus());

        when(bookmarkRepository.findAll()).thenReturn(records);

        List<BookmarkResponse> responses = service.getAllBookMarksByStatus();

        System.out.println("Responses order: \n"
                + responses.get(0).getTitle() + " - " + responses.get(0).getReadStatus() + "\n"
                + responses.get(1).getTitle() + " - " + responses.get(1).getReadStatus() + "\n"
                + responses.get(2).getTitle() + " - " + responses.get(2).getReadStatus() + "\n"
                + responses.get(3).getTitle() + " - " + responses.get(3).getReadStatus());

        assertEquals(responses.get(0).getTitle(), records.get(1).getTitle());
        assertEquals(responses.get(1).getTitle(), records.get(3).getTitle());
        assertEquals(responses.get(2).getTitle(), records.get(0).getTitle());
        assertEquals(responses.get(3).getTitle(), records.get(2).getTitle());
    }

    @Test
    void getBookMark_validId_returnsBookMark(){
        BookmarkRecord record = createRecord();

        when(bookmarkRepository.findById(record.getBookmarkId())).thenReturn(Optional.of(record));

        BookmarkResponse response = service.getBookMark(record.getBookmarkId());


        assertEquals(record.getTitle(), response.getTitle());
        assertEquals(record.getReadStatus(), response.getReadStatus());
        assertNull(response.getBookmarkId());
        assertNull(response.getBookmarkCreationDate());
        assertNull(response.getAuthor());
        assertNull(response.getGenre());
        assertNull(response.getNumPages());
        assertNull(response.getIsbn13());
        assertNull(response.getDescription());
        assertNull(response.getImageURL());
    }

    @Test
    void getBookMark_notInDB_throwsNewResponseStatusException(){
        String id = UUID.randomUUID().toString();

        when(bookmarkRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                ()-> service.getBookMark(id),
                "ResponseStatusExcpetion should be thrown when Id is not in DB");
    }

    @Test
    void getBooksByGenre_validGenre_returnsListOfBookSearchResponse(){
        String genre = "Fantasy";
        List<BookSearchResponse> responses = createBookSearchResponses();
        when(client.getBookRecommendationsByGenre(genre)).thenReturn(responses);
        List<BookSearchResponse> result = service.getBooksByGenre(genre);

        assertEquals(result.get(0).getTitle(), responses.get(0).getTitle());
        assertEquals(result.get(1).getTitle(), responses.get(1).getTitle());
    }

    @Test
    void getBooksByGenre_invalidGenre_returnsEmptyList(){
        String genre = "";
        when(client.getBookRecommendationsByGenre(genre)).thenReturn(null);
        List<BookSearchResponse> result = service.getBooksByGenre(genre);
        assertEquals(0, result.size());
    }

    @Test
    void getBooksByAuthor_validAuthor_returnsListOfBookSearchResponse(){
        String author = "JK Rowling";
        List<BookSearchResponse> responses = createBookSearchResponses();
        when(client.getBookRecommendationsByAuthor(author)).thenReturn(responses);
        List<BookSearchResponse> result = service.getBooksByAuthor(author);

        assertEquals(result.get(0).getTitle(), responses.get(0).getTitle());
        assertEquals(result.get(1).getTitle(), responses.get(1).getTitle());
    }

    @Test
    void getBooksByAuthor_invalidAuthor_returnsEmptyList(){
        String author = "";
        when(client.getBookRecommendationsByAuthor(author)).thenReturn(null);
        List<BookSearchResponse> result = service.getBooksByAuthor(author);
        assertEquals(0, result.size());
    }

    @Test
    void getBooks_validId_returnsBookSearchResponse(){
        String id = UUID.randomUUID().toString();
        BookSearchResponse response = new BookSearchResponse();
        response.setBookSearchId(id);
        response.setTitle("Harry Potter and the Philosopher's Stone");
        response.setAuthor("JK Rowling");
        response.setGenre("Fantasy");
        response.setNumPages("223");
        response.setIsbn13("978-0-7475-3269-9");
        response.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        response.setImageURL("fakeURL");

        when(client.getBookSearch(id)).thenReturn(response);
        BookSearchResponse result = service.getBook(id);

        assertEquals(response.getBookSearchId(), result.getBookSearchId());
        assertEquals(response.getTitle(), result.getTitle());
        assertEquals(response.getAuthor(), result.getAuthor());
        assertEquals(response.getGenre(), result.getGenre());
        assertEquals(response.getDescription(), result.getDescription());
        assertEquals(response.getNumPages(), result.getNumPages());
        assertEquals(response.getIsbn13(), result.getIsbn13());
        assertEquals(response.getImageURL(), result.getImageURL());
    }

    @Test
    void getBooks_invalidId_returnsEmptyBookSearchResponse(){
        String id = "";
        when(client.getBookSearch(id)).thenReturn(null);

        BookSearchResponse result = service.getBook(id);
        assertNull(result.getBookSearchId());
    }

    private List<BookSearchResponse> createBookSearchResponses(){
        List<BookSearchResponse> responses = new ArrayList<>();
        BookSearchResponse response = new BookSearchResponse();
        response.setBookSearchId(UUID.randomUUID().toString());
        response.setTitle("Harry Potter and the Philosopher's Stone");
        response.setAuthor("JK Rowling");
        response.setGenre("Fantasy");
        response.setNumPages("223");
        response.setIsbn13("978-0-7475-3269-9");
        response.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        response.setImageURL("fakeURL");

        BookSearchResponse response2 = new BookSearchResponse();
        response2.setBookSearchId(UUID.randomUUID().toString());
        response2.setTitle("Harry Potter and the Chamber of Secrets");
        response2.setAuthor("JK Rowling");
        response2.setGenre("Fantasy");
        response2.setNumPages("251");
        response2.setIsbn13("0-7475-3849-2");
        response2.setDescription("Harry and his friends are now forced to secretly uncover the truth" +
                " about the chamber before the school closes or any lives are taken.");
        response2.setImageURL("fakeURL");

        responses.add(response);
        responses.add(response2);

        return responses;
    }

    private BookmarkRecord createRecord(){

        String id = UUID.randomUUID().toString();
        String time = LocalDateTime.now().toString();

        BookmarkRecord record = new BookmarkRecord();
        record.setBookmarkId(id);
        record.setBookmarkCreationDate(time);
        record.setTitle("Harry Potter and the Philosopher's Stone");
        record.setAuthor("JK Rowling");
        record.setGenre("Fantasy");
        record.setNumPages("223");
        record.setIsbn13("978-0-7475-3269-9");
        record.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        record.setImageURL("fakeURL");
        record.setReadStatus("Interested");

        return record;
    }
    private List<BookmarkRecord> createRecords(){
        List<BookmarkRecord> records = new ArrayList<>();
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String id3 = UUID.randomUUID().toString();
        String id4 = UUID.randomUUID().toString();
        String time1 = LocalDateTime.now().toString();
        String time2 = LocalDateTime.now().toString();
        String time3 = LocalDateTime.now().toString();
        String time4 = LocalDateTime.now().toString();

        BookmarkRecord record1 = new BookmarkRecord();
        record1.setBookmarkId(id1);
        record1.setBookmarkCreationDate(time1);
        record1.setTitle("Harry Potter and the Philosopher's Stone");
        record1.setAuthor("JK Rowling");
        record1.setGenre("Fantasy");
        record1.setNumPages("223");
        record1.setIsbn13("978-0-7475-3269-9");
        record1.setDescription("An orphaned boy enrolls in a school of wizardry, " +
                "where he learns the truth about himself, " +
                "his family and the terrible evil that haunts the magical world.");
        record1.setImageURL("fakeURL");
        record1.setReadStatus("Interested");

        BookmarkRecord record2 = new BookmarkRecord();
        record2.setBookmarkId(id2);
        record2.setBookmarkCreationDate(time2);
        record2.setTitle("Harry Potter and the Chamber of Secrets");
        record2.setAuthor("JK Rowling");
        record2.setGenre("Fantasy");
        record2.setNumPages("251");
        record2.setIsbn13("0-7475-3849-2");
        record2.setDescription("Harry and his friends are now forced to secretly uncover the truth" +
                " about the chamber before the school closes or any lives are taken.");
        record2.setImageURL("fakeURL");
        record2.setReadStatus("Interested");

        BookmarkRecord record3 = new BookmarkRecord();
        record3.setBookmarkId(id3);
        record3.setBookmarkCreationDate(time3);
        record3.setTitle("Inside Delta Force");
        record3.setAuthor("Eric L. Haney");
        record3.setGenre("Memoir");
        record3.setNumPages("336");
        record3.setIsbn13("978-0385732529");
        record3.setDescription("Inside Delta Force: The Story of America's Elite Counterterrorist Unit" +
                " is a 2002 memoir written by Eric L. Haney about his experiences as a founding" +
                " special forces operator in the 1st Special Forces Operational Detachment–Delta" +
                " the U.S. Army's counterterrorist unit.");
        record3.setImageURL("fakeURL");
        record3.setReadStatus("Read");

        BookmarkRecord record4 = new BookmarkRecord();
        record4.setBookmarkId(id4);
        record4.setBookmarkCreationDate(time4);
        record4.setTitle("Last of the Breed");
        record4.setAuthor("Louis L'Amour");
        record4.setGenre("Western Fiction");
        record4.setNumPages("496");
        record4.setIsbn13("9780593129944");
        record4.setDescription("Last of the Breed, is a 1987 book by Louis L'Amour. " +
                "It tells the fictional story of Native American United States Air Force pilot Major Joseph Makatozi, " +
                "captured by the Soviets over the Bering Strait. " +
                "The story follows his escape from captivity through the Siberian wilderness.");
        record4.setImageURL("fakeURL");
        record4.setReadStatus("Read");

        records.add(record4);
        records.add(record2);
        records.add(record3);
        records.add(record1);

        return records;
    }
}
