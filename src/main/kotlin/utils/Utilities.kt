package utils

import models.Play
import models.Game

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(games: List<Game>): String {
        val sb = StringBuilder()
        for (game in games) {
            sb.append(game.toString()).append("\n")
        }
        return sb.toString().trim()
    }

    @JvmStatic
    fun formatSetString(playsToFormat: Set<Play>): String =
        playsToFormat
            .joinToString(separator = "\n") { play -> "\t$play" }
}
