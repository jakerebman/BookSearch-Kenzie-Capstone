import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import CollectionClient from "../api/collectionPageClient";

var CURRENT_STATE;

/**
 * Logic needed for the view playlist page of the website.
 */
class BookmarkPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['onCreateCollection', 'onGetCollection', 'onDeleteCollection', 'confirmDeleteCollection', 'addItemsToTable', 'onCollectionPageDelete', 'onGetAllCollections','renderCollection'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        // document.getElementById
        document.getElementById('create-collection-form').addEventListener('submit', this.onCreateCollection);
        document.getElementById('search-collection').addEventListener('submit', this.onGetCollection);
        document.getElementById('delete-collection').addEventListener('click', this.onCollectionPageDelete);
        document.getElementById('collection-list').addEventListener('click', this.onGetAllCollections);
        // TODO: Add listeners for form-delete-btn + add-items btn

        this.client = new CollectionClient();

        this.dataStore.addChangeListener(this.renderCollection);
        //this.renderCollection();
    }

    // Render Methods --------------------------------------------------------------------------------------------------
    async renderCollection() {
        console.log("Entering render method...");
        let getState = this.dataStore.get(CURRENT_STATE);


        if (!(getState)) {
            console.log("ERROR: Unable to retrieve current state!");
        }

        if (getState === 'CREATE') {
            console.log("State = CREATE");
            let resultArea = document.getElementById('collection-result-info');

            const collection = this.dataStore.get("collection");
            console.log(collection);
            console.log(collection.collectionId);

            if (collection) {
                document.getElementById("create-collection-results").style.display = "flex";
                resultArea.innerHTML = `
            <div>${collection.collectionId}</div>
            `
            } else {
                resultArea.innerHTML = "Error Creating Collection! Try Again... ";
            }
        } else if (getState === 'GET') {
            console.log("State = GET");
            const getCollection = this.dataStore.get("getCollection");

            if (getCollection) {
                document.getElementById("collection-results-header").innerHTML = "COLLECTION ID:";
                console.log(getCollection);

                let collectionId = getCollection.collectionId;
                console.log(collectionId);
                // Save in dataStore or localStorage
                localStorage.setItem("collectionId", getCollection.collectionId);
                let collectionDate = getCollection.creationDate;
                console.log(collectionDate);
                let collectionName = getCollection.collectionName;
                console.log(collectionName);
                let collectionType = getCollection.type;
                console.log(collectionType);
                // Save in dataStore or localstorage
                localStorage.setItem("collectionType", getCollection.type);
                let collectionDesc = getCollection.description;
                console.log(collectionDesc);
                let collectionItems = getCollection.collectionItemNames;
                console.log(collectionItems);

                await this.generateTable(collectionId,
                    collectionDate,
                    collectionName,
                    collectionType,
                    collectionDesc,
                    collectionItems);

                document.getElementById('table-delete-btn').addEventListener('click', this.onDeleteCollection);
                document.getElementById('table-add-items-btn').addEventListener('click', this.addItemsToTable);
            } else {
                this.errorHandler("Error Getting Collection! Try Again... ");
                console.log("Error Getting Collection!");
            }

        } else if (getState === 'DELETE') {
            console.log("State = 'DELETE'");
            const deleteCollectionId = this.dataStore.get("deleteCollectionId");

            if (deleteCollectionId) {
                console.log(deleteCollectionId);
                this.showMessage(`Request submitted to delete: ${deleteCollectionId}`);
            } else {
                this.errorHandler(`Error Deleting Collection ID: ${deleteCollectionId}`);
                console.log("Error Deleting Collection ID...");
            }
        } else if (getState === 'GET_ALL') {
            console.log("State === 'GET_ALL'");
            let resultArea = document.getElementById('collection-result-info');
            resultArea.innerHTML = "";

            const getAllCollections = this.dataStore.get("getAllCollections");
            const convertCollections = Object.entries(getAllCollections);
            console.log(convertCollections);

            if (getAllCollections) {
                document.getElementById("create-collection-results").style.display = "flex";
                document.getElementById("collection-results-header").innerHTML = "ALL COLLECTIONS:";
                const ul = document.createElement("ul");
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
        } else {
            console.log("ERROR: Unable to process current state!");
        }
        // TODO: Add Get All collections - pass in the response from getAllCollections (reference 'create')
        // } else if (getState === 'DELETE') {
        //     // Do delete things
        // } else {
        //}
    }

    // } else {
    //     console.log("ERROR: Unable to retrieve state!");
    // }

    // } else if (endpoint === 'getCollection') {
    //     // Enter a collectionId in search bar
    //     // onClick, render a table with options
    //     // 'Delete Collection' button - onClick
    //     // renders a confirmation window similar to above
    //     // 'Add Items to Collection' button
    //     // If collection == card game, redirect to
    //     // card game page, with collectionId passed as well
    //     // if collection == board game, same behavior
    // }
    //
    // }


    // When render off search, generate html with buttons and bind to methods that does endpoint requests
    // Or use render to redirect to a new page
    // declare a global variable that has that id in it
    // all JS files will have access to that variable, so could create another global var
    // here for the collectionId
    // get global working and then focus on passing the collectionId automatically later

    // Event Handlers --------------------------------------------------------------------------------------------------
    async onCreateCollection(event) {
        console.log("Entering onCreateCollection method...");
        event.preventDefault();

        let collectionName = document.getElementById('collection-name').value;
        let collectionType = document.getElementById('collection-type').value;
        let collectionDescription = document.getElementById('collection-description').value;

        if (collectionName === null || collectionType === null) {
            this.errorHandler("Must enter valid data for Collection Name and Collection Type");
        }

        const createCollection = await this.client.createCollection(
            collectionName,
            collectionType,
            collectionDescription,
            this.errorHandler
        );

        if (createCollection) {
            this.showMessage('Collection Created!');
        } else {
            this.errorHandler("Error creating collection! Try again...");
            console.log("POST isn't working...");
        }

        // Clear form data - form.clear
        // setState with multiple
        this.dataStore.setState({
            [CURRENT_STATE]: "CREATE",
            ["collection"]: createCollection
        });
    }

    async onGetCollection(event) {

        /* Add to place where it's needed
        *   item.style.setProperty('--display', 'none');
        * */
        // TODO: Add some sort of id validation?
        console.log("Entering onGetCollection method...");

        // // Prevent the page from refreshing on form submit
        event.preventDefault();

        let collectionId = document.getElementById('search-input').value;
        console.log(collectionId);
        if (collectionId === '' || collectionId.trim().length === 0) {
            this.errorHandler("ERROR: Must enter valid Collection ID!");
            console.log("Collection ID: " + collectionId + " is empty");
        }

        //localStorage.setItem("collectionId", collectionId);
        try {
            const getCollection = await this.client.getCollectionById(collectionId, this.errorHandler);

            if (getCollection) {
                this.showMessage(`Found Collection: ${collectionId}`);
                this.dataStore.setState({
                    [CURRENT_STATE]: "GET",
                    ["getCollection"]: getCollection
                });
            } else {
                this.errorHandler("Error retrieving collection by ID: " + `${collectionId}` + "Try again with a valid collection ID");
                console.log("GET isn't working...");
            }
        } catch(e) {
            console.log(e);
        }
    }

    async onGetAllCollections(event) {
        // TODO: Make a call to getAllCollections endpoints, render table in render() method
        console.log("Entering onGetAllCollections method...");
        event.preventDefault();

        const allCollections = await this.client.getAllCollections(this.errorHandler);

        if (allCollections && allCollections.length > 0) {
            this.showMessage("Listing All Collections!");
            this.dataStore.setState({
                [CURRENT_STATE]: "GET_ALL",
                ["getAllCollections"]: allCollections
            });
        } else {
            this.errorHandler("Error retrieving all collections. Try again ... ");
            console.log("GET ALL isn't working...");
        }
    }

    async onDeleteCollection(event) {
        console.log("Entering onDeleteCollection method...");
        event.preventDefault();

        // Retrieve the collectionId
        let collectionId = document.getElementById('search-input').value;

        // Validate the collectionId
        if (collectionId === '' || collectionId.trim().length === 0) {
            this.errorHandler("ERROR: Must enter valid Collection ID!");
            console.log("Collection ID is empty" + " " + collectionId);
        }

        if (collectionId) {
            await this.confirmDeleteCollection(collectionId);
        } else {
            this.errorHandler("Error: Collection ID: " + `${collectionId}` + " Invalid!");
            console.log("Issue with Collection ID on DELETE...");
        }
    }

    // TODO: Is another delete method needed to handle 'Delete Collection' button?
    // TODO: CollectionPage 'Delete Collection' could use window.prompt()

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

    async confirmDeleteCollection(collectionId) {
        console.log("Entering the confirmDeleteCollection method...");

        var msg = prompt("Are you sure you want to delete this collection? Enter 'yes' or 'no'");
        var response = msg.toLowerCase();

        if (response === null || response === "") {
            this.errorHandler("ERROR: Must enter either yes or no!");
        }

        let deleteCollection;

        if (response === 'yes') {
            deleteCollection = await this.client.deleteCollectionById(collectionId, this.errorHandler);
            this.showMessage(`Collection: ${collectionId} - Deleted!`);
        } else if (response === 'no') {
            this.showMessage("Collection Not Deleted!");
        } else {
            this.errorHandler("ERROR: Must enter either yes or no!");
        }

        this.dataStore.setState({
            [CURRENT_STATE]: "DELETE",
            ["deleteCollection"]: deleteCollection,
            ["deleteCollectionId"]: collectionId
        });

        // Alternative method - potentially use for on-screen button functionality
        // Try with prompt first
        // if (confirm("Are you sure you want to delete this collection? Select 'OK' to confirm deletion.")) {
        //     Make API Call
        //     returnMsg = "Collection Deleted"
        // } else {
        //      returnMsg = "Collection Not Deleted!"
        // }
    }

    async generateTable(id, date, name, type, description, itemNames) {
        // Dynamically render HTML for getCollectionById results
        console.log("Entering generateTable method...");

        if (itemNames.size === 0) {
            itemNames = "null";
        }

        console.log(name);

        // Get reference for the body - if method used
        var tableDiv = document.getElementById("collection-table-results");

        // Create a table element
        var table = document.createElement("table");

        // Set table id
        table.setAttribute('id', 'get-collection-table');
        var tr = document.createElement("tr");

        // Add header rows
        const headerRowNames = [
            "Collection ID",
            "Collection Creation Date",
            "Collection Name",
            "Collection Type",
            "Collection Description",
            "Collection Item Names",
            "Delete Collection",
            "Add Items To Collection",
            "Close Table"
        ];

        for (var i = 0; i < headerRowNames.length; i++) {
            // Create column element
            var th = document.createElement("th");
            // Create cell element
            var text = document.createTextNode(headerRowNames[i]);
            th.appendChild(text);
            tr.appendChild(th);
        }
        table.appendChild(tr);



        var trData = document.createElement("tr");

        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        var td3 = document.createElement("td");
        var td4 = document.createElement("td");
        var td5 = document.createElement("td");
        var td6 = document.createElement("td");
        var td7 = document.createElement("td");
        var td8 = document.createElement("td");
        var td9 = document.createElement("td");

        var colId = document.createTextNode(id);
        var colCreationDate = document.createTextNode(date);
        var colName = document.createTextNode(name);
        var colType = document.createTextNode(type);
        var colDesc = document.createTextNode(description);
        var colItemNames = document.createTextNode(itemNames);

        td1.appendChild(colId);
        td2.appendChild(colCreationDate);
        td3.appendChild(colName);
        td4.appendChild(colType);
        td5.appendChild(colDesc);
        td6.appendChild(colItemNames);
        td7.innerHTML = "<button type='button' id='table-delete-btn'>DELETE</button>";
        td8.innerHTML = "<button type='button' id='table-add-items-btn'>Add To Collection</button>";
        td9.innerHTML = "<input type='button' id='close-table' value='CLOSE' onclick=document.getElementById(\"get-collection-table\").style.display='none'>";

        trData.appendChild(td1);
        trData.appendChild(td2);
        trData.appendChild(td3);
        trData.appendChild(td4);
        trData.appendChild(td5);
        trData.appendChild(td6);
        trData.appendChild(td7);
        trData.appendChild(td8);
        trData.appendChild(td9);

        table.appendChild(trData);

        //document.body.appendChild(table);
        tableDiv.appendChild(table);

        // table.setAttribute("border-collapse", "collapse");
        // td.setAttribute("border", "1px solid #cecfd5");
        // td.setAttribute("padding", "10px 15px");
    }

    async generateAllTable(collections) {
        // Dynamically render HTML for getCollectionById results
        console.log("Entering generateTable method...");

        // TODO: Extract each of the values from the get all request
        // TODO: This time the data append should be in a for loop

        // if (itemNames.size === 0) {
        //     itemNames = "null";
        // }

        console.log(name);

        // Get reference for the body - if method used
        //var tableDiv = document.getElementById('');

        // Create a table element
        var table = document.createElement("table");

        // Set table id
        table.setAttribute('id', 'get-collection-table');
        var tr = document.createElement("tr");

        // Add header rows
        const headerRowNames = [
            "Collection ID",
            "Collection Creation Date",
            "Collection Name",
            "Collection Type",
            "Collection Description",
            "Collection Item Names",
            "Delete Collection",
            "Add Items To Collection",
            "Close Table"
        ];

        for (var i = 0; i < headerRowNames.length; i++) {
            // Create column element
            var th = document.createElement("th");
            // Create cell element
            var text = document.createTextNode(headerRowNames[i]);
            th.appendChild(text);
            tr.appendChild(th);
        }
        table.appendChild(tr);

        collections.forEach(collection => {
            var trData = document.createElement("tr");

            var td1 = document.createElement("td");
            var td2 = document.createElement("td");
            var td3 = document.createElement("td");
            var td4 = document.createElement("td");
            var td5 = document.createElement("td");
            var td6 = document.createElement("td");
            var td7 = document.createElement("td");
            var td8 = document.createElement("td");
            var td9 = document.createElement("td");

            var colId = document.createTextNode();
            var colCreationDate = document.createTextNode();
            var colName = document.createTextNode();
            var colType = document.createTextNode();
            var colDesc = document.createTextNode();
            var colItemNames = document.createTextNode();

            td1.appendChild(colId);
            td2.appendChild(colCreationDate);
            td3.appendChild(colName);
            td4.appendChild(colType);
            td5.appendChild(colDesc);
            td6.appendChild(colItemNames);
            td7.innerHTML = "<button type='button' id='table-delete-btn'>DELETE</button>";
            td8.innerHTML = "<button type='button' id='table-add-items-btn'>Add To Collection</button>";
            td9.innerHTML = "<input type='button' id='close-table' value='CLOSE' onclick=document.getElementById(\"get-collection-table\").style.display='none'>";

            trData.appendChild(td1);
            trData.appendChild(td2);
            trData.appendChild(td3);
            trData.appendChild(td4);
            trData.appendChild(td5);
            trData.appendChild(td6);
            trData.appendChild(td7);
            trData.appendChild(td8);
            trData.appendChild(td9);

            table.appendChild(trData);
        })
        document.body.appendChild(table);
        // table.setAttribute("border-collapse", "collapse");
        // td.setAttribute("border", "1px solid #cecfd5");
        // td.setAttribute("padding", "10px 15px");
    }

    async addItemsToTable() {
        // TODO:
        // 1. Try to figure out how to pass or retrieve collection id and add to the form
        // 2. Update with the correct html page for the mtg card game page
        // Get CollectionId and CollectionType from localStorage
        //let collectionId = localStorage.getItem("collectionId");
        let collectionType = localStorage.getItem("collectionType");
        // If type = card game redirect to cardGamePage
        // else if type = board game redirect to board game page

        if (collectionType === '' || collectionType.trim().length === 0) {
            console.log("ERROR: Unable to retrieve the collection type from local Storage");
        } else {
            collectionType = collectionType.replaceAll(' ', '');
            console.log(collectionType);
        }

        if (collectionType.toLowerCase() === 'cardgame') {
            // TODO: Needs to be changed to w/e the card game page url will be
            window.location.href = `http://localhost:5001/index.html`
        } else if (collectionType.toLowerCase() === 'boardgame') {
            window.location.href = `http://localhost:5001/mtgPage.html`
        }


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