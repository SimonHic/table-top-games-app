package utils

import models.Item
import models.Note

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notes: List<Note>): String {
        val sb = StringBuilder()
        for (note in notes) {
            sb.append(note.toString()).append("\n")
        }
        return sb.toString().trim()
    }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Item>): String =
        itemsToFormat
            .joinToString(separator = "\n") { item -> "\t$item" }
}
