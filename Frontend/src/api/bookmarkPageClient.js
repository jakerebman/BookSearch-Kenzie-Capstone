import BaseClass from "../util/baseClass";
import axios from "axios";

export default class BookmarkPageClient extends BaseClass {
    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded',
                                'createNewBookmark',
                                'updateBookmarkStatusById',
                                'deleteBookmarkById',
                                'getAllBookmarksByStatus',
                                'getBookmarkById',
                                'getBooksByAuthor',
                                'getBooksByGenre',
                                'getBookById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }


    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async createNewBookmark(bookmarkId,
                            creationDate,
                            title,
                            author,
                            genre,
                            numPages,
                            description,
                            imageUrl,
                            isbn13,
                            readStatus,
                            errorCallback=console.error) {
        try {
            const response = await this.client.post(`/bookmarks`, {
                // JSON object
                "Bookmark_Id": bookmarkId,
                "Bookmark_Creation_Date": creationDate,
                "Title": title,
                "Author": author,
                "Genre": genre,
                "Num_Pages": numPages,
                "Description": description,
                "Image_URL": imageUrl,
                "ISBN13": isbn13,
                "Read_Status": readStatus,
            });
            return response.data;
        } catch (error) {
            this.handleError("createNewBookmark", error, errorCallback);
        }
    }

    async updateBookmarkStatusById(id, errorCallback) {
        try {
            const response = await this.client.put(`/bookmarks/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("updateBookmarkStatusById", error, errorCallback);
        }
    }

    async deleteBookmarkById(id, errorCallback) {
        try {
            const response = await this.client.delete(`/bookmarks/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteBookmarkById", error, errorCallback)
        }
    }

    async getAllBookmarksByStatus(errorCallback) {
        try {
            const response = await this.client.get(`/bookmarks`);
            return response.data;
        } catch (error) {
            this.handleError("getAllBookmarksByStatus", error, errorCallback);
        }
    }

    async getBookmarkById(bookmarkId, errorCallback) {
        try {
            const response = await this.client.get(`/bookmarks/${bookmarkId}`);
            return response.data;
        } catch (error) {
            this.handleError("getBookmarkById", error, errorCallback)
        }
    }

    async getBooksByAuthor(author, errorCallback) {
        try {
            const response = await this.client.get(`/bookmarks/books/${author}`);
            return response.data;
        } catch (error) {
            this.handleError("getBooksByAuthor", error, errorCallback)
        }
    }

    async getBooksByGenre(genre, errorCallback) {
        try {
            const response = await this.client.get(`/bookmarks/books/${genre}`);
            return response.data;
        } catch (error) {
            this.handleError("getBooksByGenre", error, errorCallback)
        }
    }

    async getBookById(bookSearchId, errorCallback) {
        try {
            const response = await this.client.get(`/bookmarks/${bookSearchId}`);
            return response.data;
        } catch (error) {
            this.handleError("getBookById", error, errorCallback)
        }
    }

    /**
     * Get collection for a given ID
     * @param name Unique identifier for a collection
     * @param type
     * @param description
     * @param errorCallback (Optional) A function to execute if the call fails
     * @returns The collection
     */
    async createCollection(name, type, description, errorCallback=console.error) {
        try {
            const response = await this.client.post(`/collections`, {
                // JSON object
                "collectionName": name,
                "type": type,
                "description": description
            });
            return response.data;
        } catch (error) {
            this.handleError("createCollection", error, errorCallback);
        }
    }





    /**
     * Get all collections
     * @param errorCallback (Optional) A function to execute if the call fails
     * @returns An array of collections
     */
    async getAllCollections(errorCallback) {
        try {
            const response = await this.client.get(`/collections`);
            return response.data;
        } catch (error) {
            this.handleError("getAllCollections", error, errorCallback);
        }
    }

    /**
     * Get collection for a given ID
     * @param id Unique identifier for a collection
     * @param errorCallback (Optional) A function to execute if the call fails
     * @returns The collection
     */
    async deleteCollectionById(id, errorCallback) {
        try {
            const response = await this.client.delete(`/collections/${id}`);
            return response.data;
        } catch (error) {
            this.handleError("deleteCollectionById", error, errorCallback)
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param method
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
            console.log(method + " failed - " + error);
        }
    }
}