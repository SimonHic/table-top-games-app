package models

import utils.Utilities

data class Note(
    var noteId: Int = 0,
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var isNoteArchived: Boolean = false,
    var items: MutableSet<Item> = mutableSetOf()
) {

    // functions to manage the items set will go in here
    private var lastItemId = 0
    private fun getItemId() = lastItemId++

    fun addItem(item: Item): Boolean {
        item.itemId = getItemId()
        return items.add(item)
    }

    fun numberOfItems() = items.size

    fun findOne(id: Int): Item? {
        return items.find { item -> item.itemId == id }
    }

    fun delete(id: Int): Boolean {
        return items.removeIf { item -> item.itemId == id }
    }

    fun update(id: Int, newItem: Item): Boolean {
        val foundItem = findOne(id)

        // if the object exists, use the details passed in the newItem parameter to
        // update the found object in the Set
        if (foundItem != null) {
            foundItem.itemContents = newItem.itemContents
            foundItem.isItemComplete = newItem.isItemComplete
            return true
        }

        // if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listItems() =
        if (items.isEmpty()) "\tNO ITEMS ADDED"
        else Utilities.formatSetString(items)

    override fun toString(): String {
        val archived = if (isNoteArchived) 'Y' else 'N'
        return "$noteId: $noteTitle, Priority($notePriority), Category($noteCategory), Archived($archived) \n${listItems()}"
    }

/*    fun checkNoteCompletionStatus(): Boolean {
        if (items.isNotEmpty()) {
            for (item in items) {
                if (!item.isItemComplete) {
                    return false
                }
            }
        }
        return true // a note with empty items can be archived, or all items are complete
    }*/
}
