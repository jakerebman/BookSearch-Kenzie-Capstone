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
//        this.bindClassMethods(['onCreateBookmark', 'onGetByAuthor', 'onGetByGenre', 'onGetBook', 'onGetBookmarksByStatus','renderCollection'], this);
        this.bindClassMethods(['onCreateBookmark', 'onGetByAuthor', 'onGetByGenre', 'addBookDetails', 'renderCollection', 'renderBookmarks'], this);
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
//        document.getElementById('collection-list').addEventListener('click', this.onGetAllCollections);
        // TODO: Add listeners for form-delete-btn + add-items btn

        this.client = new BookmarkPageClient();

        let result = await this.client.getAllBookmarksByStatus(this.errorHandler);

        this.dataStore.set("allBookmarkdBooks", result);

        await this.renderBookmarks();
        this.dataStore.addChangeListener(this.renderCollection);
        this.dataStore.addChangeListener(this.renderBookmarks);
        //this.renderCollection();
    }

    // Render Methods --------------------------------------------------------------------------------------------------
    async renderBookmarks() {
        let resultArea = document.getElementById("bookmark-info");

        resultArea.innerHTML = ""

        const allBookmarkdBooks = this.dataStore.get("allBookmarkdBooks");
        console.log("Before first if statement.")
        if (allBookmarkdBooks) {
            for (let i = 0; i < allBookmarkdBooks.length; i++) {
                console.log("Inside Loop to Parse Data")
                let bookmarkId = allBookmarkdBooks[i].Bookmark_Id;

                this.dataStore.set(bookmarkId, allBookmarkdBooks[i]);
            }
        }

        console.log("After the first if statement.")
        if (allBookmarkdBooks) {
            console.log(allBookmarkdBooks)

            const ul = document.createElement("ul");

            ul.addEventListener("click", this.addBookImage)

            ul.id = "bookmarks-status";
            for (let i = 0; i < allBookmarkdBooks.length; i++) {
                //let bookmarkId = allBookmarkdBooks[i].Bookmark_Id;
                const li = document.createElement("li");
                li.innerHTML = ""
                console.log("inside the for loop " + allBookmarkdBooks[i]);

                const divId = `${allBookmarkdBooks[i].Bookmark_Id}`;

                li.innerHTML = `
                <div id=${divId} imgPath=${allBookmarkdBooks[i].Image_URL} style="cursor: pointer;">Title: ${allBookmarkdBooks[i].Title}</div>  
                <div>Author: ${allBookmarkdBooks[i].Author}</div>`;
                //li.setAttribute("imgPath",allBookmarkdBooks[i].Image_URL)

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
        } else if (getState === 'GET') {

        }
    }


    // When render off search, generate html with buttons and bind to methods that does endpoint requests
    // Or use render to redirect to a new page
    // declare a global variable that has that id in it
    // all JS files will have access to that variable, so could create another global var
    // here for the collectionId
    // get global working and then focus on passing the collectionId automatically later

    // Event Handlers --------------------------------------------------------------------------------------------------
    async getBook() {
        alert("It works!!!");
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



    // TODO: This one for Capstone.

    async onCollectionPageDelete(event) {
        // TODO: Workflow - Is this needed? State needs to be different from table delete button!
        // 1. When user clicks the 'DeleteCollection' button, this method is called
        // 2. Prompt user to enter a collection id
        // 3. Validate collection id entered
        // 4. If collection Id valid, save the collection id to the dataStore
        // 5. Call the delete confirmation method
        console.log("Entering onCollectionPageDelete method...");

        event.preventDefault();

        var deleteCollectionId = prompt("Enter Collection ID for Collection to Delete");
        console.log(deleteCollectionId);

        if (deleteCollectionId === null || deleteCollectionId=== "") {
            this.errorHandler("ERROR: Must enter a valid Collection ID");
        }

        if (deleteCollectionId) {
            await this.confirmDeleteCollection(deleteCollectionId);
        } else {
            this.errorHandler(`ERROR: Collection ID: ${deleteCollectionId} is Invalid!`);
        }

        this.dataStore.setState({
            [CURRENT_STATE]: "DELETE",
            ["deleteCollection"]: deleteCollection,
            ["deleteCollectionId"]: deleteCollectionId
        });
    }

    async addBookImage(event) {

        console.log(event.target.innerText)
        console.log(event.target.id) // this should be your book id maybe?
        console.log(event.target.innerHTML)
        console.log(event.target.getAttribute("imgPath"))
        console.log("Entering Book Details Method");

        const image = document.querySelector(".image")
        // imageArea.remove() - not this one.
        // img.src=""
        // img.alt=""
        // img.setAttribute('src', '')
        // img.setAttribute('alt', '')
        image.src=event.target.getAttribute("imgPath")
        image.alt= "book image"
    }

    async addBookDetails(bookmarkId) {
        console.log("Entering Book Details Method");
        const getBookById = this.dataStore.get(bookmarkId);
        console.log(getBookById);

        let resultArea = document.getElementById('book-info');
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
