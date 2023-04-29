package controllers

import models.Game
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

class GameAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var games = ArrayList<Game>()

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0
    private fun getId() = lastId++

    // ----------------------------------------------
    //  CRUD METHODS FOR GAME's ArrayList
    // ----------------------------------------------
    fun add(game: Game): Boolean {
        game.gameId = getId()
        return games.add(game)
    }

    fun delete(id: Int) = games.removeIf { game -> game.gameId == id }

    fun update(id: Int, game: Game?): Boolean {
        // find the game object by the index number
        val foundGame = findGame(id)

        // if the game exists, use the game details passed as parameters to update the found game in the belonged ArrayList.
        if ((foundGame != null) && (game != null)) {
            foundGame.gameName = game.gameName
            foundGame.gameRating = game.gameRating
            foundGame.gameBrand = game.gameBrand
            return true
        }

        // if the game was not found, return false, indicating that the update was not successful
        return false
    }

    fun saveForLaterGame(id: Int): Boolean {
        val foundGame = findGame(id)
        if ((foundGame != null) && (!foundGame.isGameSavedForLater)
            //  && ( foundGame.checkGameCompletionStatus())
        ) {
            foundGame.isGameSavedForLater = true
            return true
        }
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun listAllGames() =
        if (games.isEmpty()) "No gaames stored"
        else formatListString(games)

    fun listRunningGames() =
        if (amountOfRunningGames() == 0) "No running games stored"
        else formatListString(games.filter { game -> !game.isGameSavedForLater })

    fun listSavedForLaterGames () =
        if (amountOfSavedForLaterGames() == 0) "No games were found as saved for later"
        else formatListString(games.filter { game -> game.isGameSavedForLater })

    // ----------------------------------------------
    //  COUNTING METHODS FOR Game ArrayList
    // ----------------------------------------------
    fun amountOfGames() = games.size
    private fun amountOfSavedForLaterGames(): Int = games.count { game: Game -> game.isGameSavedForLater }
    fun amountOfRunningGames(): Int = games.count { game: Game -> !game.isGameSavedForLater }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findGame(gameId: Int) = games.find { game -> game.gameId == gameId }

    fun findGameByName(searchString: String) =
        formatListString(games.filter { game -> game.gameName.contains(searchString, ignoreCase = true) })

    // ----------------------------------------------
    //  LISTING METHODS FOR Plays
    // ----------------------------------------------
    fun listToBePlayedPlays(): String =
        if (amountOfGames() == 0) "No games stored"
        else {
            var listOfToBePlayedPlays = ""
            for (game in games) {
                for (play in game.plays) {
                    if (!play.isPlayComplete) {
                        listOfToBePlayedPlays += game.gameName + ": " + play.playContents + "\n"
                    }
                }
            }
            listOfToBePlayedPlays
        }
// ----------------------------------------------
//  COUNTING METHODS FOR PLAYS
// ----------------------------------------------
    fun amountOfToBePlayedPlays(): Int {
        var amountOfToBePlayedPlays = 0
        for (game in games) {
            for (play in game.plays) {
                if (!play.isPlayComplete) {
                    amountOfToBePlayedPlays++
                }
            }
        }
        return amountOfToBePlayedPlays
    }

    fun findPlayByDesc(searchString: String): String {
        return if (amountOfGames() == 0) "No games stored in the system!"
        else {
            var listOfGames = ""
            for (game in games) {
                for (play in game.plays) {
                    if (play.playContents.contains(searchString, ignoreCase = true)) {
                        listOfGames += "${game.gameId}: ${game.gameName} \n\t${play}\n"
                    }
                }
            }
            if (listOfGames == "") "No plays found for: $searchString"
            else listOfGames
        }
    }

    @Throws(Exception::class)
    fun load() {
        val loadedGames = serializer.read()
        if (loadedGames is ArrayList<*>) {
            games = loadedGames.filterIsInstance<Game>() as ArrayList<Game>
        } else {
            throw Exception("Unable to load the games with the included plays")
        }
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(games)
    }
}
