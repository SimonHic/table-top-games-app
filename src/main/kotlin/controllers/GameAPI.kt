/**
 * This class provides the API for managing a list of Games and their Plays.
 * It grants a wide range of functionality to add, delete, update, save for later, and list Games with their respected Plays.
 * As well as this it allows Users who interact with the App to utilize a multitude of alternative operations such as searching by word specifications,
 * list running Games, and updating the status of Games to display if they have been played.
 * This API interacts with the Serializer to assist in storing the Game data to a file of either persistence method.
 *
 * @author:   SimonHic
 * @author:   sdrohan (skeleton code)
 * @version:  V1.0
 * */
package controllers

import models.Game
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

/**
 * Manages Game data and allows the use of its methods to handle Game information that can be processed.
 */
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
    /**
     * Adds a new Game object to the ArrayList and assigns it a unique ID to differentiate it from the other objects.
     *
     * @param game the Game object to be added to the ArrayList
     * @return true to say whether the addition of the object to the ArrayList was successful or not.
     */
    fun add(game: Game): Boolean {
        game.gameId = getId()
        return games.add(game)
    }

    /**
     * Deletes a Game object from the ArrayList by utilizing its unique ID to differentiate it from the other objects.
     *
     * @param id the unique identifier of the game object to be deleted
     * @return true to say whether the subtraction of the object from the ArrayList was successful or not.
     */
    fun delete(id: Int) = games.removeIf { game -> game.gameId == id }

    /**
     * Updates a Game object from the ArrayList by utilizing its unique ID to differentiate it from the other objects.
     *
     * @param game the Game object to be updated in the ArrayList
     * @param id the unique identifier of the game object to be updated
     * @return a Boolean to say whether the change of the object from the ArrayList was successful or not.
     */
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

    /**
     * Changes the state of a Game object from the ArrayList by utilizing its unique ID to differentiate it from the other objects.
     * Once the correct ID has been selected it will change the state by using the Boolean value.
     *
     * @param id the unique identifier of the game object to be saved for later
     * @return True to say whether the change of the object from the ArrayList was successful or not.
     */
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

    /**
     * List all the Games in a String format
     *
     * @return If successful, a formatted String a formatted String that has a list of all the Games. If unsuccessful, a message that says "No games stored" in the system.
     */
    fun listAllGames() =
        if (games.isEmpty()) "No games stored"
        else formatListString(games)

    /**
     * List all the games that have the running Boolean status set to true.#
     *
     * @return all the running Games in a String format.
     */
    fun listRunningGames() =
        if (amountOfRunningGames() == 0) "No running games stored"
        else formatListString(games.filter { game -> !game.isGameSavedForLater })

    /**
     * List all the games that have the savedForLater Boolean status set to true.
     *
     * @return all the saved for later Games in a String format. Or an error message stating how "No games were found as saved for later"
     */
    fun listSavedForLaterGames() =
        if (amountOfSavedForLaterGames() == 0) "No games were found as saved for later"
        else formatListString(games.filter { game -> game.isGameSavedForLater })

    // ----------------------------------------------
    //  COUNTING METHODS FOR Game ArrayList
    // ----------------------------------------------

    /**
     * Counts the amount of Games stored in the system.
     *
     * @return the total amount of Games.
     */
    fun amountOfGames() = games.size

    /**
     * Counts the amount of Games stored in the system that have savedForLater set to true.
     *
     * @return the total amount of Games with saved for later.
     */
    private fun amountOfSavedForLaterGames(): Int = games.count { game: Game -> game.isGameSavedForLater }

    /**
     * Counts the amount of Games stored in the system that are running.
     *
     * @return the total amount of Games that are running.
     */
    fun amountOfRunningGames(): Int = games.count { game: Game -> !game.isGameSavedForLater }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------

    /**
     * Finds a specific Game using it's unique ID.
     *
     * @param gameId the ID of the Game object to find.
     * @return the Game Object at the specified ID, or null if nothing can be found.
     */
    fun findGame(gameId: Int) = games.find { game -> game.gameId == gameId }

    /**
     * Finds a specific Game using its name.
     *
     * @param searchString the String to find specific game names.
     * @return the Game Object with the matching information.
     */
    fun findGameByName(searchString: String) =
        formatListString(games.filter { game -> game.gameName.contains(searchString, ignoreCase = true) })

    // ----------------------------------------------
    //  LISTING METHODS FOR Plays
    // ----------------------------------------------

    /**
     * Lists all the Games that have not been Played in the system.
     * It will print out a message stating that there is "No Games stored" with the ToBePlayed set to true.
     *
     * @return the Games that have not been played.
     */
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

    /**
     * Counts the amount of Games that have been not Played.
     *
     * @return the amount that have not been played yet.
     */
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

    /**
     * Searches the system for specific Plays by their descriptions.
     *
     * @return the specific Game along with its corresponding Play description.
     */
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

    /**
     * Loads the Games from a file using the Serializer
     *
     * @throws Exception if there is an error in this read process.
     */
    @Throws(Exception::class)
    fun load() {
        val loadedGames = serializer.read()
        if (loadedGames is ArrayList<*>) {
            games = loadedGames.filterIsInstance<Game>() as ArrayList<Game>
        } else {
            throw Exception("Unable to load the games with the included plays")
        }
    }

    /**
     * Stores the Games by writing them to a file using the Serializer.
     *
     * @throws Exception if there is an error in this write process.
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(games)
    }
}
