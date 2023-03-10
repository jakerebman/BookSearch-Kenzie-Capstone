package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.BookSearchResponse;
import com.kenzie.appserver.controller.model.BookmarkResponse;
import com.kenzie.appserver.controller.model.BookmarkUpdateRequest;
import com.kenzie.appserver.controller.model.CreateBookmarkRequest;
import com.kenzie.appserver.service.BookmarkService;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
public class BookmarkControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    BookmarkService bookmarkService;

    private static final MockNeat mockNeat = MockNeat.threadLocal();
    private static final ObjectMapper mapper = new ObjectMapper();
//    @Autowired
//    private BookmarkRepository bookmarkRepository;


    /** ------------------------------------------------------------------------
     *  Add Bookmark
     *  ------------------------------------------------------------------------ **/
    @Test
    public void createBookmark_CreateSuccessful() throws Exception {
//        String bookmarkId = UUID.randomUUID().toString();
//        String bookmarkCreationDate = LocalDateTime.now().toString();
        String bookTitle = "Gideon the Ninth";
        String bookAuthor = "Tamsyn Muir";
        String genre = "Fantasy";
        String numPages = "479";
        String isbn = "125031318X";
        String description = "Gideon has a sword, some dirty magazines, and no more time for undead nonsense";
        String imageName = mockNeat.names().get();
        String imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/42036538.jpg";

        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest();
        bookmarkRequest.setTitle(bookTitle);
        bookmarkRequest.setAuthor(bookAuthor);
        bookmarkRequest.setGenre(genre);
        bookmarkRequest.setNumPages(numPages);
        bookmarkRequest.setIsbn13(isbn);
        bookmarkRequest.setDescription(description);
        bookmarkRequest.setImageURL(imageURL);
        bookmarkRequest.setReadStatus("Read");

        ResultActions actions = mvc.perform(post("/bookmarks")
                .content(mapper.writeValueAsString(bookmarkRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        BookmarkResponse bookmarkResponse = mapper.readValue(responseBody, BookmarkResponse.class);
        assertThat(bookmarkResponse.getBookmarkId()).isNotEmpty().as("The bookmark ID is populated");
        assertThat(bookmarkResponse.getBookmarkCreationDate()).isNotEmpty().as("The bookmark creation date is populated");
        assertThat(bookmarkResponse.getTitle()).isEqualTo(bookmarkRequest.getTitle()).as("The book title is correct");
        assertThat(bookmarkResponse.getAuthor()).isEqualTo(bookmarkRequest.getAuthor()).as("The book author is correct");
        assertThat(bookmarkResponse.getGenre()).isEqualTo(bookmarkRequest.getGenre()).as("The book genre is correct");
        assertThat(bookmarkResponse.getNumPages()).isEqualTo(bookmarkRequest.getNumPages()).as("The book page count is correct");
        assertThat(bookmarkResponse.getIsbn13()).isEqualTo(bookmarkRequest.getIsbn13()).as("The book ISBN is correct");
        assertThat(bookmarkResponse.getDescription()).isEqualTo(bookmarkRequest.getDescription()).as("The book description is correct");
        assertThat(bookmarkResponse.getImageURL()).isEqualTo(bookmarkRequest.getImageURL()).as("The book image url is correct");
        assertThat(bookmarkResponse.getReadStatus()).isEqualTo(bookmarkRequest.getReadStatus()).as("The book read status is correct");
    }

    /** ------------------------------------------------------------------------
     *  Update Bookmark
     *  ------------------------------------------------------------------------ **/
    @Test
    public void updateBookmarkStatus_ValidBookmarkId() throws Exception {
        String bookTitle = "Oathbringer";
        String bookAuthor = "Brandon Sanderson";
        String genre = "Fantasy";
        String numPages = "1294";
        String isbn = "978-1250297143";
        String description = "Dalinar Kholin%60s Alethi armies won a fleeting victory at a terrible cost...";
        String imageName = mockNeat.names().get();
        String imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/Oathbringer.png";

        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest();
        bookmarkRequest.setTitle(bookTitle);
        bookmarkRequest.setAuthor(bookAuthor);
        bookmarkRequest.setGenre(genre);
        bookmarkRequest.setNumPages(numPages);
        bookmarkRequest.setIsbn13(isbn);
        bookmarkRequest.setDescription(description);
        bookmarkRequest.setImageURL(imageURL);
        bookmarkRequest.setReadStatus("Interested");

        BookmarkResponse bookmarkResponse = bookmarkService.addNewBookmark(bookmarkRequest);

        System.out.println("Bookmark ID: " + bookmarkResponse.getBookmarkId());

        BookmarkUpdateRequest bookmarkUpdateRequest = new BookmarkUpdateRequest();
        bookmarkUpdateRequest.setBookmarkId(bookmarkResponse.getBookmarkId());
        bookmarkUpdateRequest.setStatus("Read");

        ResultActions actions = mvc.perform(put("/bookmarks/{bookmarkId}", bookmarkResponse.getBookmarkId())
                .content(mapper.writeValueAsString(bookmarkUpdateRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        BookmarkResponse response = mapper.readValue(responseBody, BookmarkResponse.class);
        assertThat(response.getBookmarkId()).isNotEmpty().as("The bookmark ID is populated");
        assertThat(response.getBookmarkCreationDate()).isNotEmpty().as("The bookmark creation date is populated");
        assertThat(response.getTitle()).isEqualTo(bookmarkRequest.getTitle()).as("The book title is correct");
        assertThat(response.getAuthor()).isEqualTo(bookmarkRequest.getAuthor()).as("The book author is correct");
        assertThat(response.getGenre()).isEqualTo(bookmarkRequest.getGenre()).as("The book genre is correct");
        assertThat(response.getNumPages()).isEqualTo(bookmarkRequest.getNumPages()).as("The book page count is correct");
        assertThat(response.getIsbn13()).isEqualTo(bookmarkRequest.getIsbn13()).as("The book ISBN is correct");
        assertThat(response.getDescription()).isEqualTo(bookmarkRequest.getDescription()).as("The book description is correct");
        assertThat(response.getImageURL()).isEqualTo(bookmarkRequest.getImageURL()).as("The book image url is correct");
        assertThat(response.getReadStatus()).isEqualTo(bookmarkUpdateRequest.getStatus()).as("The book read status is correct");
    }

    @Test
    public void updateBookmarkStatus_InvalidBookmarkId() throws Exception {
        String bookmarkId = UUID.randomUUID().toString();

        BookmarkUpdateRequest bookmarkUpdateRequest = new BookmarkUpdateRequest();
        bookmarkUpdateRequest.setBookmarkId(bookmarkId);
        bookmarkUpdateRequest.setStatus("Interested");

        ResultActions action = mvc.perform(put("/bookmarks/{bookmarkId}", bookmarkUpdateRequest.getBookmarkId())
                .content(mapper.writeValueAsString(bookmarkUpdateRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /** ------------------------------------------------------------------------
     *  Delete Bookmark
     *  ------------------------------------------------------------------------ **/
    @Test
    public void deleteBookmarkById_ValidBookmarkId() throws Exception {
        String bookTitle = "The Art of War";
        String bookAuthor = "Sun Tzu";
        String genre = "History";
        String numPages = "384";
        String isbn = "978-0813319513";
        String description = "The most famous study of strategy ever written...";
        String imageName = mockNeat.names().get();
        String imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/tAoW.png";

        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest();
        bookmarkRequest.setTitle(bookTitle);
        bookmarkRequest.setAuthor(bookAuthor);
        bookmarkRequest.setGenre(genre);
        bookmarkRequest.setNumPages(numPages);
        bookmarkRequest.setIsbn13(isbn);
        bookmarkRequest.setDescription(description);
        bookmarkRequest.setImageURL(imageURL);
        bookmarkRequest.setReadStatus("Read");

        BookmarkResponse bookmarkResponse = bookmarkService.addNewBookmark(bookmarkRequest);

        mvc.perform(delete("/bookmarks/{bookmarkId}", bookmarkResponse.getBookmarkId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        mvc.perform(get("/bookmarks/{bookmarkId}", bookmarkResponse.getBookmarkId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /** ------------------------------------------------------------------------
     *  Get All Bookmarks By Status
     *  ------------------------------------------------------------------------ **/
    @Test
    public void getAllBookmarksByStatus_Successful() throws Exception {
        String book1_bookTitle = "He Who Fights with Monsters: A LitRPG Adventure";
        String book1_bookAuthor = "Shirtaloon";
        String book1_bookGenre = "Fantasy";
        String book1_numPages = "678";
        String book1_isbn = "979-8712811786";
        String book1_description = "Jason wakes up in a mysterious world of magic and monsters...";
        String book1_imageName = mockNeat.names().get();
        String book1_imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/He_Who_Fights_With_Monsters.png";

        String book2_bookTitle = "Ready Player One";
        String book2_bookAuthor = "Ernest Cline";
        String book2_bookGenre = "Fantasy";
        String book2_numPages = "384";
        String book2_isbn = "978-0307887443";
        String book2_description = "A world at stake. A quest for the ultimate prize. Are you ready?";
        String book2_imageName = mockNeat.names().get();
        String book2_imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/ready-player-one.jpg";

        CreateBookmarkRequest bookmarkRequest1 = new CreateBookmarkRequest();
        bookmarkRequest1.setTitle(book1_bookTitle);
        bookmarkRequest1.setAuthor(book1_bookAuthor);
        bookmarkRequest1.setGenre(book1_bookGenre);
        bookmarkRequest1.setNumPages(book1_numPages);
        bookmarkRequest1.setIsbn13(book1_isbn);
        bookmarkRequest1.setDescription(book1_description);
        bookmarkRequest1.setImageURL(book1_imageURL);
        bookmarkRequest1.setReadStatus("Read");

        CreateBookmarkRequest bookmarkRequest2 = new CreateBookmarkRequest();
        bookmarkRequest2.setTitle(book2_bookTitle);
        bookmarkRequest2.setAuthor(book2_bookAuthor);
        bookmarkRequest2.setGenre(book2_bookGenre);
        bookmarkRequest2.setNumPages(book2_numPages);
        bookmarkRequest2.setIsbn13(book2_isbn);
        bookmarkRequest2.setDescription(book2_description);
        bookmarkRequest2.setImageURL(book2_imageURL);
        bookmarkRequest2.setReadStatus("Interested");

        bookmarkService.addNewBookmark(bookmarkRequest1);
        bookmarkService.addNewBookmark(bookmarkRequest2);

        ResultActions actions = mvc.perform(get("/bookmarks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responsebody = actions.andReturn().getResponse().getContentAsString();
        List<BookmarkResponse> responses = mapper.readValue(responsebody, new TypeReference<List<BookmarkResponse>>() {});
        assertThat(responses.size()).isGreaterThan(1).as("There are responses");
        for (BookmarkResponse response : responses) {
            assertThat(response.getBookmarkId()).isNotEmpty().as("The bookmark ID is populated");
            assertThat(response.getBookmarkCreationDate()).isNotEmpty().as("The bookmark creation date is populated");
            assertThat(response.getTitle()).isNotEmpty().as("The title is populated");
            assertThat(response.getAuthor()).isNotEmpty().as("The author is populated");
            assertThat(response.getGenre()).isNotEmpty().as("The genre is populated");
            assertThat(response.getNumPages()).isNotEmpty().as("The number of pages is populated");
            assertThat(response.getIsbn13()).isNotEmpty().as("The ISBN13 is populated");
            assertThat(response.getDescription()).isNotEmpty().as("The description is populated");
            assertThat(response.getImageURL()).isNotEmpty().as("The image url is populated");
            assertThat(response.getReadStatus()).isNotEmpty().as("The read status is populated");
        }
    }

    /** ------------------------------------------------------------------------
     *  Get Bookmark
     *  ------------------------------------------------------------------------ **/
    @Test
    public void getBookmarkById_Successful() throws Exception {
        String bookTitle = "Ready Player Two";
        String bookAuthor = "Ernest Cline";
        String genre = "Fantasy";
        String numPages = "367";
        String isbn = "978-1524761332";
        String description = "An unexpected quest. Two worlds at stake. Are you ready?";
        String imageName = mockNeat.names().get();
        String imageURL = "https://capstone-pbandj-book-image.s3.amazonaws.com/ready-player-2.jpg";

        CreateBookmarkRequest bookmarkRequest = new CreateBookmarkRequest();
        bookmarkRequest.setTitle(bookTitle);
        bookmarkRequest.setAuthor(bookAuthor);
        bookmarkRequest.setGenre(genre);
        bookmarkRequest.setNumPages(numPages);
        bookmarkRequest.setIsbn13(isbn);
        bookmarkRequest.setDescription(description);
        bookmarkRequest.setImageURL(imageURL);
        bookmarkRequest.setReadStatus("Interested");

        BookmarkResponse bookmarkResponse = bookmarkService.addNewBookmark(bookmarkRequest);

        ResultActions actions = mvc.perform(get("/bookmarks/{bookmarkId}", bookmarkResponse.getBookmarkId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        BookmarkResponse responses = mapper.readValue(responseBody, BookmarkResponse.class);
        assertThat(responses.getBookmarkId()).isNotEmpty().as("The bookmark ID is populated");
        assertThat(responses.getBookmarkCreationDate()).isNotEmpty().as("The bookmark creation date is populated");
        assertThat(responses.getTitle()).isEqualTo(bookmarkResponse.getTitle()).as("The book title is populated and correct");
        assertThat(responses.getAuthor()).isEqualTo(bookmarkResponse.getAuthor()).as("The book author is populated and correct");
        assertThat(responses.getGenre()).isEqualTo(bookmarkResponse.getGenre()).as("The book genre is populated and correct");
        assertThat(responses.getNumPages()).isEqualTo(bookmarkResponse.getNumPages()).as("The number of pages is populated and correct");
        assertThat(responses.getIsbn13()).isEqualTo(bookmarkResponse.getIsbn13()).as("The book ISBN is populated and correct");
        assertThat(responses.getDescription()).isEqualTo(bookmarkResponse.getDescription()).as("The book description is populated and correct");
        assertThat(responses.getImageURL()).isEqualTo(bookmarkResponse.getImageURL()).as("The book image is populated and correct");
        assertThat(responses.getReadStatus()).isEqualTo(bookmarkResponse.getReadStatus()).as("The read status for the book is populated and correct");
    }

    /** ------------------------------------------------------------------------
     *  Get Books By Author
     *  ------------------------------------------------------------------------ **/
    @Test
    public void getBooksByAuthor_Successful() throws Exception {
        String bookAuthor = "Brandon Sanderson";

        ResultActions actions = mvc.perform(get("/bookmarks/{author}/authors", bookAuthor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();

        List<BookSearchResponse> responses = mapper.readValue(responseBody, new TypeReference<List<BookSearchResponse>>() {});

        assertThat(responses.size()).isGreaterThan(0).as("There are books in the list");
        for (BookSearchResponse response : responses) {
            assertThat(response.getBookSearchId()).isNotEmpty().as("The ID is populated");
            assertThat(response.getTitle()).isNotEmpty().as("The book title is populated");
            assertThat(response.getAuthor()).isEqualTo(bookAuthor).as("The book author matches");
            assertThat(response.getGenre()).isNotEmpty().as("The book genre is populated");
            assertThat(response.getNumPages()).isNotEmpty().as("The number of book pages is populated");
            assertThat(response.getIsbn13()).isNotEmpty().as("The book ISBN is populated");
            assertThat(response.getDescription()).isNotEmpty().as("The book description is populated");
            assertThat(response.getImageURL()).isNotEmpty().as("The book image url is populated");
        }
    }

    /** ------------------------------------------------------------------------
     *  Get Books By Genre
     *  ------------------------------------------------------------------------ **/
    @Test
    public void getBooksByGenre_Successful() throws Exception {
        String genre = "Fantasy";

        ResultActions actions = mvc.perform(get("/bookmarks/{genre}/genres", genre)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();

        List<BookSearchResponse> responses = mapper.readValue(responseBody, new TypeReference<List<BookSearchResponse>>() {});

        assertThat(responses.size()).isGreaterThan(0).as("There are books in the list");
        for (BookSearchResponse response : responses) {
            assertThat(response.getBookSearchId()).isNotEmpty().as("The ID is populated");
            assertThat(response.getTitle()).isNotEmpty().as("The book title is populated");
            assertThat(response.getAuthor()).isNotEmpty().as("The book author is populated");
            assertThat(response.getGenre()).isEqualTo(genre).as("The book genre matches");
            assertThat(response.getNumPages()).isNotEmpty().as("The number of book pages is populated");
            assertThat(response.getIsbn13()).isNotEmpty().as("The book ISBN is populated");
            assertThat(response.getDescription()).isNotEmpty().as("The book description is populated");
            assertThat(response.getImageURL()).isNotEmpty().as("The book image url is populated");
        }
    }

    /** ------------------------------------------------------------------------
     *  Get Book
     *  ------------------------------------------------------------------------ **/

    @Test
    public void getBookById_Successful() throws Exception {
        String bookSearchId = "3032e0ed-1196-4acc-b7ec-4688934ebb2f";

        BookSearchResponse bookResponse = bookmarkService.getBook(bookSearchId);

        mvc.perform(get("/bookmarks/{id}/books", bookSearchId))
                .andExpect(jsonPath("bookSearchId")
                        .value(is(bookSearchId)))
                .andExpect(jsonPath("title")
                        .isString())
                .andExpect(jsonPath("author")
                        .isString())
                .andExpect(jsonPath("genre")
                        .isString())
                .andExpect(jsonPath("numPages")
                        .isString())
                .andExpect(jsonPath("isbn13")
                        .isString())
                .andExpect(jsonPath("description")
                        .isString())
                .andExpect(jsonPath("imageURL")
                        .isString())
                .andExpect(status().is2xxSuccessful());
    }
}
