/**
 * This class provides utility methods for working with `Game` and `Play` objects.
 */
package utils

import models.Play
import models.Game

object Utilities {

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    /**
     * Returns a formatted string of a list of Game objects.
     *
     * @param games the list of Game objects to format.
     * @return a formatted string of the list of games.
     */
    @JvmStatic
    fun formatListString(games: List<Game>): String {
        val sb = StringBuilder()
        for (game in games) {
            sb.append(game.toString()).append("\n")
        }
        return sb.toString().trim()
    }

    /**
     * Returns a formatted string of a set of Play objects.
     *
     * @param playsToFormat the set of Play objects to format.
     * @return a formatted string of the set of plays.
     */
    @JvmStatic
    fun formatSetString(playsToFormat: Set<Play>): String =
        playsToFormat
            .joinToString(separator = "\n") { play -> "\t$play" }
}
