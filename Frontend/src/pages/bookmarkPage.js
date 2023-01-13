import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import BookmarkPageClient from "../api/bookmarkPageClient";

var CURRENT_STATE;

/**
 * Logic needed for the view playlist page of the website.
 */
class BookmarkPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreateBookmark', 'onGetByAuthor', 'onGetByGenre', 'addBookDetails', 'onBookmarkDelete', 'onUpdateBookmark', 'renderCollection', 'renderBookmarks'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        // document.getElementById
//        document.getElementById('create-collection-form').addEventListener('click', this.onCreateBookmark);
        document.getElementById('search-input').addEventListener('submit', this.onGetByAuthor);
        document.getElementById('genre-select-dropdown').addEventListener('click', this.onGetByGenre);
        document.getElementById('delete-bookmark').addEventListener('click', this.onBookmarkDelete);
        document.getElementById('read-status').addEventListener('click', this.onUpdateBookmark);
        // TODO: Add listeners for form-delete-btn + add-items btn

        this.client = new BookmarkPageClient();

        let result = await this.client.getAllBookmarksByStatus(this.errorHandler);

        this.dataStore.set("allBookmarkdBooks", result);

        await this.renderBookmarks();
        // this.dataStore.addChangeListener(this.renderBookmarks);
        this.dataStore.addChangeListener(this.renderCollection);
        //this.renderCollection();
    }

    // Render Methods --------------------------------------------------------------------------------------------------
    async renderBookmarks() {
        console.log("Entering Render Bookmarks method...")
        let resultArea = document.getElementById("bookmark-info");

        resultArea.innerHTML = ""

        const allBookmarkdBooks = this.dataStore.get("allBookmarkdBooks");

        if (allBookmarkdBooks) {
            for (let i = 0; i < allBookmarkdBooks.length; i++) {
                console.log("Inside Loop to Parse Data")
                let bookmarkId = allBookmarkdBooks[i].Bookmark_Id;

                this.dataStore.set(bookmarkId, allBookmarkdBooks[i]);
            }
        }

        if (allBookmarkdBooks) {
            console.log(allBookmarkdBooks)

            const ul = document.createElement("ul");
            ul.style.listStyle= 'none';

            ul.addEventListener("click", this.addBookImage)
            ul.addEventListener("click", this.addBookDetails)

            ul.id = "bookmarks-status";
            for (let i = 0; i < allBookmarkdBooks.length; i++) {
                const li = document.createElement("li");
                li.innerHTML = ""
                console.log("inside the for loop " + allBookmarkdBooks[i]);

                const divId = `${allBookmarkdBooks[i].Bookmark_Id}`;

                const bookInfo = `${allBookmarkdBooks[i].Description}`

                li.innerHTML = `
                <div id=${divId} imgPath=${allBookmarkdBooks[i].Image_URL}
                                bookTitle="${allBookmarkdBooks[i].Title}"
                                bookAuthor="${allBookmarkdBooks[i].Author}"
                                bookDescription="${bookInfo}"
                                bookGenre="${allBookmarkdBooks[i].Genre}"
                                bookPages="${allBookmarkdBooks[i].Num_Pages}"
                                bookIsbn="${allBookmarkdBooks[i].ISBN13}"
                                bookReadStatus="${allBookmarkdBooks[i].Read_Status}"
                                style="cursor: pointer;">Title: ${allBookmarkdBooks[i].Title}</div>
                <div>Author: ${allBookmarkdBooks[i].Author}</div>
                <div>Status: ${allBookmarkdBooks[i].Read_Status}</div>
                <br>`;

                ul.append(li);
            }
           resultArea.append(ul);
        } else {
            resultArea.innerHTML = "Error Printing Bookmark results...";
        }
    }

    async renderCollection() {
        console.log("Entering render method...");
        let getState = this.dataStore.get(CURRENT_STATE);


        if (!(getState)) {
            console.log("ERROR: Unable to retrieve current state!");
        }

        if (getState === 'CREATE') {
            console.log("State === 'CREATE'");
            let resultArea = document.getElementById('book-info');
            let imageArea = document.getElementById('book-image');
            resultArea.innerHTML = "";
            imageArea.innerHTML = "";

            const createBookmark = this.dataStore.get("createBookmark");
            const convertCreateBookmark = Object.entries(createBookmark);
            console.log(convertCreateBookmark);

            if (createBookmark) {
                document.getElementById("book-image").style.display = "flex";
                document.getElementById("book-info").style.display = "flex";

                const ul = document.createElement("ul");
                //TODO: Pull info from both image and info to save.
                for (let i = 0; i < getAllCollections.length; i++) {
                    const li = document.createElement("li");
                    console.log("inside the for loop " + getAllCollections[i]);
                    li.innerHTML += `
                    <div>Collection Name: ${getAllCollections[i].collectionName}</div>
                    <div>Collection ID: ${getAllCollections[i].collectionId}</div>`;
                    ul.append(li);
                }
                resultArea.append(ul);
            } else {
                resultArea.innerHTML = "Error Printing Collections...";
            }
        } else if (getState === 'AUTHOR') {
            console.log("State = AUTHOR");
            let resultArea = document.getElementById('result-info')
            resultArea.innerHTML = "";

            const getByAuthor = this.dataStore.get("getByAuthor");
            const convertBooks = Object.entries(getByAuthor);
            console.log(convertBooks);

            if (getByAuthor) {
                const ul = document.createElement("ul");
                for (let i = 0; i < getByAuthor.length; i++) {
                    const li = document.createElement("li");
                    console.log("inside the for loop " + getByAuthor[i]);
                    li.innerHTML += `
                    <div>Title: ${getByAuthor[i].Title}</div>
                    <div>Author: ${getByAuthor[i].Author}</div>`;
                    ul.append(li);
                }
                resultArea.append(ul);
            } else {
                resultArea.innerHTML = "Error Printing Search results...";
            }
        } else if (getState === 'GENRE') {
            console.log("State = GENRE");
            let resultArea = document.getElementById('result-info')
            resultArea.innerHTML = "";

            const getByGenre = this.dataStore.get("getByGenre");
            const convertBooks = Object.entries(getByGenre);
            console.log(convertBooks);
            if (getByGenre) {
               const ul = document.createElement("ul");
               for (let i = 0; i < getByGenre.length; i++) {
                   const li = document.createElement("li");
                   console.log("inside the for loop " + getByGenre[i]);
                   li.innerHTML += `
                   <div>Title: ${getByGenre[i].Title}</div>
                   <div>Author: ${getByGenre[i].Author}</div>`;
                   ul.append(li);
               }
               resultArea.append(ul);
            } else {
                resultArea.innerHTML = "Error Printing Search results...";
            }
        } else if (getState === 'UPDATE') {
            console.log("State = UPDATE");
            const updatedBookmarkStatus = this.dataStore.get("updateBookmarkStatus");
            console.log("Updated Bookmark Status Results: " + updatedBookmarkStatus);

            const updatedBookTitle = updatedBookmarkStatus.Title;
            console.log("Updated Bookmark Status Results: " + updatedBookTitle);

            const updatedReadStatus = updatedBookmarkStatus.Read_Status;
            console.log("Updated Bookmark Status Results: " + updatedBookmarkStatus);

            if (updatedBookmarkStatus) {
                this.showMessage(`Book: ${updatedBookTitle} Status changed to ${updatedReadStatus}`);
            } else {
                this.errorHandler(`ERROR Updating Book: ${updatedBookTitle}`);
                console.log("ERROR Updating Bookmark...");
            }
        } else if (getState === 'DELETE') {
            console.log("State = 'DELETE'");
            const deleteBookmarkId = this.dataStore.get("deleteBookmarkId");
            console.log("[STATE] Bookmark ID: " + deleteBookmarkId);
            const deleteBookTitle = this.dataStore.get("deleteBookTitle");
            console.log("[STATE] Book Title: " + deleteBookTitle);

            if (deleteBookmarkId) {
                this.showMessage(`Request submitted to delete: ${deleteBookTitle}`);
            } else {
                this.errorHandler(`Error Deleting Book: ${deleteBookTitle}`);
                console.log("Error Deleting Bookmark...");
            }
        } else {
            console.log("ERROR: Unable to process current state!")
        }
    }


    // When render off search, generate html with buttons and bind to methods that does endpoint requests
    // Or use render to redirect to a new page
    // declare a global variable that has that id in it
    // all JS files will have access to that variable, so could create another global var
    // here for the collectionId
    // get global working and then focus on passing the collectionId automatically later

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onBookmarkDelete(event) {
        console.log("Entering onBookmarkDelete method...");

        event.preventDefault();
        console.log("Delete Event: " + event)

        // BookmarkId
        const bookmarkId = document.getElementById("bookmark-id").innerText;
        console.log(bookmarkId);

        if (bookmarkId === null) {
            this.errorHandler("ERROR: Bookmark ID is empty");
        }

        if (bookmarkId) {
            await this.confirmDeleteBookmark(bookmarkId);
        } else {
            this.errorHandler(`ERROR: Bookmark ID: ${bookmarkId} is Invalid!`);
        }

        this.dataStore.setState({
            [CURRENT_STATE]: "DELETE",
            ["deleteBookmarkId"]: bookmarkId
        });
    }

    async onUpdateBookmark(event) {
        console.log("Entering onUpdateBookmark method...");

        event.preventDefault();
        console.log("Update Event: " + event);

        const bookmarkId = document.getElementById("bookmark-id").innerText;
        console.log(bookmarkId);
        const bookTitle = document.getElementById("bookmark-title").innerText;
        console.log("Book Title: " + bookTitle);
        const bookmarkStatus = "Read";


        if (bookmarkId == null) {
            this.errorHandler("ERROR: Bookmark ID is empty");
        }

        try {
            let result = await this.client.updateBookmarkStatusById(bookmarkId, bookmarkStatus);

            if (result) {
                this.showMessage(`Bookmark Status Updated for Book: ${bookTitle}`);
                this.dataStore.setState({
                    [CURRENT_STATE]: "UPDATE",
                    ["updateBookmarkStatus"]: result
                });
            } else {
                this.errorHandler("ERROR: Unable to update status for Book: " + `${bookTitle}`);
                console.log("UPDATE ain't working...");
            }
        } catch (e) {
            console.log(e);
        }
    }


    async onCreateBookmark(event) {

           console.log("Entering onCreateBookmark method.....");

           event.preventDefault();

           // let bookmark = this.datastore.


    }

    async onGetByAuthor(event) {

        console.log("Entering onGetByAuthor method...");

        // // Prevent the page from refreshing on form submit
        event.preventDefault();

        let authorName = document.getElementById('search-input').value;
        console.log(authorName);
        if (authorName === '' || authorName.trim().length === 0) {
            this.errorHandler("ERROR: Must enter valid Author name!");
            console.log("Author name: " + authorName + " is empty");
        }

        //localStorage.setItem("collectionId", collectionId);
        try {
            const getByAuthor = await this.client.getBooksByAuthor(authorName, this.errorHandler);

            if (getByAuthor) {
                this.showMessage(`Found Author: ${authorName}`);
                this.dataStore.setState({
                    [CURRENT_STATE]: "AUTHOR",
                    ["getByAuthor"]: getByAuthor
                });
            } else {
                this.errorHandler("Error finding Author by Author name: " + `${authorName}` + "Try again with a valid Author name!");
                console.log("AUTHOR isn't working...");
            }
        } catch(e) {
            console.log(e);
        }
    }

    async onGetByGenre(event) {

        console.log("Entering onGetByGenre method...");

        // // Prevent the page from refreshing on form submit
        event.preventDefault();

        let genre = document.getElementById('genre-type').value;
        console.log(genre);
        if (genre === '' || genre.trim().length === 0) {
            this.errorHandler("ERROR: Must enter valid Genre!");
            console.log("Genre: " + genre + " is empty");
        }

        //localStorage.setItem("collectionId", collectionId);
        try {
            const getByGenre = await this.client.getBooksByGenre(genre, this.errorHandler);

            if (getByGenre) {
                this.showMessage(`Found Genre: ${genre}`);
                this.dataStore.setState({
                    [CURRENT_STATE]: "GENRE",
                    ["getByGenre"]: getByGenre
                });
            } else {
                this.errorHandler("Error finding Genre: " + `${genre}` + "Try again with a valid Genre!");
                console.log("GENRE isn't working...");
            }
        } catch(e) {
            console.log(e);
        }
    }

    async addBookImage(event) {

        console.log(event.target.innerText)
        console.log(event.target.id) // this should be your book id maybe?
        console.log(event.target.innerHTML)
        console.log(event.target.getAttribute("imgPath"))
        console.log("Entering Book Image Method");

        const image = document.querySelector(".image")

        image.src=event.target.getAttribute("imgPath")
        image.alt= "book image"
    }

    async addBookDetails(event) {
        console.log("Entering Book Details Method");
        console.log(event.target.getAttribute("bookTitle"))
        console.log(event.target.getAttribute("bookDescription"))

        let resultArea = document.getElementById('book-info')
        resultArea.innerHTML = ""

        const ul = document.createElement("ul")
        ul.style.listStyle= 'none';

        const li = document.createElement("li")
        li.innerHTML = ""
        li.innerHTML = `
        <div id="bookmark-id" style="display: none">${event.target.id}</div>
        <div id="bookmark-title">Title: ${event.target.getAttribute("bookTitle")}</div>
        <div>Author: ${event.target.getAttribute("bookAuthor")}</div>
        <div>Description: ${event.target.getAttribute("bookDescription")}</div>
        <div>Genre: ${event.target.getAttribute("bookGenre")}</div>
        <div>Number of Pages: ${event.target.getAttribute("bookPages")}</div>
        <div>ISBN-13: ${event.target.getAttribute("bookIsbn")}</div>
        <div id="bookmark-status" style="display: none">${event.target.getAttribute("bookReadStatus")}</div>`;

        ul.append(li)
        resultArea.append(ul)
    }

    async confirmDeleteBookmark(bookmarkId) {
        console.log("Entering the confirmDeleteBookmark method...");
        console.log("Bookmark ID: " + bookmarkId)

        var msg = prompt("Are you sure you want to delete this book? Enter 'yes' or 'no'");
        var response = msg.toLowerCase();

        if (response === null || response === "") {
            this.errorHandler("ERROR: Must enter either yes or no!");
        }

        let deleteBookmark;
        const bookTitle = document.getElementById("bookmark-title").innerText;
        console.log("Book Title: " + bookTitle);

        if (response === 'yes') {
            deleteBookmark = await this.client.deleteBookmarkById(bookmarkId, this.errorHandler);
            this.showMessage(`Book: ${bookTitle} - Deleted!`);
        } else if (response === 'no') {
            this.showMessage(`Book: ${bookTitle} - Not Deleted!`);
        } else {
            this.errorHandler("ERROR: Must enter either yes or no!");
        }

        this.dataStore.setState({
            [CURRENT_STATE]: "DELETE",
            ["deleteBookmark"]: deleteBookmark,
            ["deleteBookTitle"]: bookTitle,
            ["deleteBookmarkId"]: bookmarkId
        });
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const bookmarkPage = new BookmarkPage();
    bookmarkPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
