package models

import utils.Utilities

data class Game(
    var gameId: Int = 0,
    var gameName: String,
    var gameRating: Int,
    var gameBrand: String,
    var isGameSavedForLater: Boolean = false,
    var plays: MutableSet<Play> = mutableSetOf()
) {

    // functions to manage the plays set will go in here
    private var lastPlayId = 0
    private fun getPlayId() = lastPlayId++

    fun addPlay(play: Play): Boolean {
        play.playId = getPlayId()
        return plays.add(play)
    }

    fun amountOfPlays() = plays.size

    fun findOne(id: Int): Play? {
        return plays.find { play -> play.playId == id }
    }

    fun delete(id: Int): Boolean {
        return plays.removeIf { play -> play.playId == id }
    }

    fun update(id: Int, newPlay: Play): Boolean {
        val foundPlay = findOne(id)

        // if the object exists, use the details passed in the newPlay parameter to
        // update the found object in the Set
        if (foundPlay != null) {
            foundPlay.playContents = newPlay.playContents
            foundPlay.isPlayComplete = newPlay.isPlayComplete
            return true
        }

        // if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listPlays() =
        if (plays.isEmpty()) "\tNO PLAYS WERE ADDED"
        else Utilities.formatSetString(plays)

    override fun toString(): String {
        val archived = if (isGameSavedForLater) 'Y' else 'N'
        return "$gameId: $gameName, Rating($gameRating), Brand($gameBrand), Saved For Later($isGameSavedForLater) \n${listPlays()}"
    }

/*    fun checkGameCompletionStatus(): Boolean {
        if (plays.isNotEmpty()) {
            for (item in plays) {
                if (!item.isItemComplete) {
                    return false
                }
            }
        }
        return true // a game with empty plays can be saved for later, or all plays are complete
    }*/
}
