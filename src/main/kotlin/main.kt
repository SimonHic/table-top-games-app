import controllers.NoteAPI
import models.Item
import models.Note
import persistence.JSONSerializer
// import persistence.XMLSerializer
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess

/**Uncomment and Comment to alternate between the two*/
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> addItemToNote()
            7 -> updateItemContentsInNote()
            8 -> deleteAnItem()
            9 -> markItemStatus()
            10 -> searchNotes()
            15 -> searchItems()
            16 -> listToDoItems()
            19 -> save()
            20 -> load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  NOTE KEEPER APP                  |
         > -----------------------------------------------------  
         > | NOTE MENU                                         |
         > |   1) Add a note                                   |
         > |   2) List notes                                   |
         > |   3) Update a note                                |
         > |   4) Delete a note                                |
         > |   5) Archive a note                               |
         > -----------------------------------------------------  
         > | ITEM MENU                                         | 
         > |   6) Add item to a note                           |
         > |   7) Update item contents on a note               |
         > |   8) Delete item from a note                      |
         > |   9) Mark item as complete/todo                   | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR NOTES                             | 
         > |   10) Search for all notes (by note title)        |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR ITEMS                             |                                
         > |   15) Search for all items (by item description)  |
         > |   16) List TODO Items                             |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

// ------------------------------------
// NOTE MENU
// ------------------------------------
fun addNote() {
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle = noteTitle, notePriority = notePriority, noteCategory = noteCategory))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL notes          |
                  > |   2) View ACTIVE notes       |
                  > |   3) View ARCHIVED notes     |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No notes stored")
    }
}

fun listAllNotes() = println(noteAPI.listAllNotes())
fun listActiveNotes() = println(noteAPI.listActiveNotes())
fun listArchivedNotes() = println(noteAPI.listArchivedNotes())

fun updateNote() {
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        // only ask the user to choose the note if notes exist
        val id = readNextInt("Enter the id of the note to update: ")
        if (noteAPI.findNote(id) != null) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            // pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.update(id, Note(0, noteTitle, notePriority, noteCategory, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteNote() {
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        // only ask the user to choose the note to delete if notes exist
        val id = readNextInt("Enter the id of the note to delete: ")
        // pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.delete(id)
        if (noteToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val id = readNextInt("Enter the id of the note to archive: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(id)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

// -------------------------------------------
// ITEM MENU (only available for active notes)
// -------------------------------------------

private fun addItemToNote() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        if (note.addItem(Item(itemContents = readNextLine("\t Item Contents: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}

//

// ------------------------------------
// NOTE REPORTS MENU
// ------------------------------------
fun searchNotes() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchNotesByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

fun updateItemContentsInNote() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        val item: Item? = askUserToChooseItem(note)
        if (item != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (note.update(item.itemId, Item(itemContents = newContents))) {
                println("Item contents updated")
            } else {
                println("Item contents NOT updated")
            }
        } else {
            println("Invalid Item Id")
        }
    }
}

private fun askUserToChooseItem(note: Note): Item? {
    return if (note.numberOfItems() > 0) {
        print(note.listItems())
        note.findOne(readNextInt("\nEnter the id of the item: "))
    } else {
        println("No items for chosen note")
        null
    }
}

fun deleteAnItem() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        val item: Item? = askUserToChooseItem(note)
        if (item != null) {
            val isDeleted = note.delete(item.itemId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markItemStatus() {
    val note: Note? = askUserToChooseActiveNote()
    if (note != null) {
        val item: Item? = askUserToChooseItem(note)
        if (item != null) {
            val changeStatus: Char
            if (item.isItemComplete) {
                changeStatus = readNextChar("The item is currently complete...do you want to mark it as TODO?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    item.isItemComplete = false
            } else {
                changeStatus = readNextChar("The item is currently TODO...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    item.isItemComplete = true
            }
        }
    }
}

fun searchItems() {
    val searchContents = readNextLine("Enter the item contents to search by: ")
    val searchResults = noteAPI.searchItemByContents(searchContents)
    if (searchResults.isEmpty()) {
        println("No items found")
    } else {
        println(searchResults)
    }
}

// ------------------------------------
// Persistence
// ------------------------------------
fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun listToDoItems() {
    if (noteAPI.numberOfToDoItems() > 0) {
        println("Total TODO items: ${noteAPI.numberOfToDoItems()}")
    }
    println(noteAPI.listTodoItems())
}

// ------------------------------------
// ITEM REPORTS MENU
// ------------------------------------

//

// ------------------------------------
// Exit App
// ------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

// ------------------------------------
// HELPER FUNCTIONS
// ------------------------------------

private fun askUserToChooseActiveNote(): Note? {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        val note = noteAPI.findNote(readNextInt("\nEnter the id of the note: "))
        if (note != null) {
            if (note.isNoteArchived) {
                println("Note is NOT Active, it is Archived")
            } else {
                return note // chosen note is active
            }
        } else {
            println("Note id is not valid")
        }
    }
    return null // selected note is not active
}
