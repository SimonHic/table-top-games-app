/**
 * Welcome to the Table-Top-Games App!
 *
 * This class contains the main function, which is the entry point of this application.
 * The main function is responsible for setting up the application, such as the initialization,
 * any dependencies or configurations synced with Gradle, and starting up the overall App!
 *
 * The Table-Top-Games App interacts with the GameAPI and uses Serializer objects to save, store
 * and load the Game and Play data. It's main function allows user to interact with the system through its many menu options
 * such as adding, listing, updating, deleting, specific searching, saving, and loading the games and plays (just to name a few!).
 *
 *
 * @author:   SimonHic
 * @author:   sdrohan (skeleton code)
 * @version:  V1.0
 *
 */
// import persistence.XMLSerializer
import controllers.GameAPI
import models.Game
import models.Play
import org.fusesource.jansi.Ansi.ansi
import persistence.JSONSerializer
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess

/**Uncomment and Comment to alternate between the two*/
// private val gameAPI = GameAPI(XMLSerializer(File("games.xml")))
private val gameAPI = GameAPI(JSONSerializer(File("games.json")))

/**
 * The main entry point for the Table-Top-Games application.
 * Initializes the GameAPI with JSONSerializer and presents a menu to the user for interacting with the app.
 */
fun main() = runMenu()

/**
 * This method gives all the options to the user.
 * It calls all the options from its other respected methods.
 */
fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addGame()
            2 -> listGames()
            3 -> updateGame()
            4 -> deleteGame()
            5 -> saveGameForLater()
            6 -> addPlayToGame()
            7 -> updatePlayContentsInGame()
            8 -> deleteAPlay()
            9 -> markPlayDesc()
            10 -> searchGames()
            15 -> searchPlays()
            16 -> listToBePlayedPlays()
            19 -> save()
            20 -> load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

/**
 * Unlike the runMenu() method, this method displays all the options to the user in the form of a menu.
 * It also calls some other variables to display emojis, and it uses jansi for color.
 * It converts it all to a String using toString()
 */
fun mainMenu(): Int {
    return readNextInt(
        ansi().render(
            """ 
         > @|cyan ------------------------------------------------------|@  
         > |@|white,bold              Table-Top-Games App|@                   |
         > @|cyan ------------------------------------------------------|@  
         > | Game MENU                                         |
         > @|green | 1) Add a game $plusSign                                |@  |
         > @|green | 2) List games $listSymbol                                |@  |
         > @|yellow |  3) Update a game $pencil                            |@  |
         > @|yellow |  4) Delete a game $trashCan                          |@    |
         > @|green | 5) Save a game for later $floppyDisk                     |@  |
         > @|cyan -----------------------------------------------------|@  
         > | Play MENU                                         | 
         > @|green | 6) Add play to a game $plusSign                        |@  |
         > @|yellow | 7) Update play contents on a game $pencil            |@  |
         > @|yellow | 8) Delete play from a game $trashCan               |@      |
         > @|blue | 9) Mark play as played/to-be played $pencil          |@  | 
         > @|cyan -----------------------------------------------------|@  
         > | REPORT MENU FOR GAMES                             | 
         > @|blue |   10) Search for games $magnifyingGlass (by game name)        |@  |
         > @|green -----------------------------------------------------|@  
         > | REPORT MENU FOR PLAYS                             |                                
         > @|blue |   15) Search for plays $magnifyingGlass (by play description)  |@ |
         > @|blue |   16) List TO-BE played Plays $listSymbol                 |@ |
         > @|cyan -----------------------------------------------------|@ 
         > @|magenta |   19) Save $floppyDisk                                  |@   |
         > @|magenta |   20) Load                                      |@  |
         > @|cyan -----------------------------------------------------|@  
         > @|red |  0) Exit                                        |@  |
         > @|cyan -----------------------------------------------------|@  
         > ==>> """.trimMargin(">")
        ).toString()
    )
}

// ------------------------------------
// Game MENU
// ------------------------------------

/**
 * Assigned unicode emojis to variables for easy use
 */
const val starSymbol = "\u2B50" // Unicode for star emoji assigned as a variable
const val magnifyingGlass = "\uD83D\uDD0D"
const val floppyDisk = "\uD83D\uDCBE"
const val redX = "\u274C"
const val waveSymbol = "\uD83D\uDC4B"
const val pencil = "\u270F"
const val trashCan = "\uD83D\uDDD1️\uFE0F"
const val plusSign = "\u2795"
const val listSymbol = "\uD83D\uDCD3"

/**
 * This method allows a User to create a game by being prompted with a series of questions
 * such as what to input for the title, star rating, and brand of a game.
 * It displays a message depending on whether it was successful or not. It also calls some unicode variables for emojis
 * to show a better UX design.
 */
fun addGame() {
    val gameTitle = readNextLine("Enter a title for the game: ")
    val gameRating = readNextInt("Enter a star rating (1 $starSymbol, 2 $starSymbol$starSymbol, 3 $starSymbol$starSymbol$starSymbol, 4 $starSymbol$starSymbol$starSymbol$starSymbol, 5 $starSymbol$starSymbol$starSymbol$starSymbol$starSymbol): ")
    val gameBrand = readNextLine("Enter a brand for the game: ")
    val isAdded = gameAPI.add(Game(gameName = gameTitle, gameRating = gameRating, gameBrand = gameBrand))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed $redX")
    }
}

/**
 * This method prompts a User with a menu with three more options. It allows a user to select any of these three to either
 * View all games, View running Games, or view saved Games. It also calls some unicode variables for emojis
 * to show a better UX design.
 */
fun listGames() {
    if (gameAPI.amountOfGames() > 0) {
        val option = readNextInt(
            """
                  > -----------------------------------
                  > |   1) View ALL games              |
                  > |   2) View RUNNING games          |
                  > |   3) View SAVED $floppyDisk FOR LATER games  |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllGames()
            2 -> listRunningGames()
            3 -> listSavedForLaterGames()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No games stored in the system $redX$floppyDisk")
    }
}

/**
 * This method lists all the Games stored in the system. It is one of the three
 * options for the listGames() method.
 */
fun listAllGames() = println(gameAPI.listAllGames())

/** This method lists all the running Games stored in the system. It is one of the three
 * options for the listGames() method.
 */
fun listRunningGames() = println(gameAPI.listRunningGames())

/**
 * This method lists all the saved for later Games stored in the system. It is one of the three
 * options for the listGames() method.
 */
fun listSavedForLaterGames() = println(gameAPI.listSavedForLaterGames())

/**
 * This method lists all the games and prompts the User to enter the desired index to allow
 * them to change the details of a Game. It also calls some unicode variables for emojis
 * to show a better UX design.
 */
fun updateGame() {
    listGames()
    if (gameAPI.amountOfGames() > 0) {
        // only ask the user to choose the game if games exist
        val id = readNextInt("Enter the id of the game to update it: ")
        if (gameAPI.findGame(id) != null) {
            val gameTitle = readNextLine("Enter the name for the game: ")
            val gameRating = readNextInt("Enter the rating (1 $starSymbol, 2 $starSymbol$starSymbol, 3 $starSymbol$starSymbol$starSymbol, 4 $starSymbol$starSymbol$starSymbol$starSymbol, 5 $starSymbol$starSymbol$starSymbol$starSymbol$starSymbol): ")
            val gameBrand = readNextLine("Enter the Brand name of the game: ")

            // pass the index of the game and the new game details to GameAPI for updating and check for success.
            if (gameAPI.update(id, Game(0, gameTitle, gameRating, gameBrand, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no games for this index number $redX")
        }
    }
}

/**
 * This method lists all the games and prompts the User to enter the desired index to allow
 * them to delete a Game of their choosing. It also calls some unicode variables for emojis
 * to show a better UX design.
 */
fun deleteGame() {
    listGames()
    if (gameAPI.amountOfGames() > 0) {
        // only ask the user to choose the game to delete if games exist
        val id = readNextInt("Enter the id of the game to delete: ")
        // pass the index of the game to GameAPI for deleting and check for success.
        val gameToDelete = gameAPI.delete(id)
        if (gameToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete wasn't Successful!!! $redX")
        }
    }
}

/**
 * This method lists all the games and prompts the User to enter the desired index to allow
 * them to change the state of a Game to be Saved for Later. It displays a success or error message depending on the
 * outcome. It also calls some unicode variables for emojis to show a better UX design.
 */
fun saveGameForLater() {
    listRunningGames()
    if (gameAPI.amountOfRunningGames() > 0) {
        // only ask the user to choose the game to save for later if running games exist
        val id = readNextInt("Enter the id of the game to save for later: ")
        // pass the index of the game to GameAPI for archiving and check for success.
        if (gameAPI.saveForLaterGame(id)) {
            println("Archive Successful!")
        } else {
            println("Archive wasn't Successful!!! $redX")
        }
    }
}

// -------------------------------------------
// Play MENU (only available for running games)
// -------------------------------------------

/**
 * Prompts the user to allow them to select a Running Game and if it is not invalid it will
 * add a Play to a Game. It displays a success or a failure message depending on the outcome.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
private fun addPlayToGame() {
    val game: Game? = askUserToChooseRunningGame()
    if (game != null) {
        if (game.addPlay(Play(playContents = readNextLine("\t Play Contents: "))))
            println("Add Successful!")
        else println("Add wasn't Successful!!! $redX")
    }
}

//

// ------------------------------------
// GAME REPORTS MENU
// ------------------------------------

/**
 * This will ask the User to enter some search terms for a specific Game they want to search by.
 * It provides feedback by displaying a success or error message depending on the outcome.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun searchGames() {
    val findName = readNextLine("Enter the description to search by $magnifyingGlass: ")
    val searchResults = gameAPI.findGameByName(findName)
    if (searchResults.isEmpty()) {
        println("No games were found $redX$magnifyingGlass")
    } else {
        println(searchResults)
    }
}

/**
 * Questions the User on a specific running Game and then Play from the stored list.
 * It asks for new content to submit for the Play and will thus update it if it's found in the ArrayList.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun updatePlayContentsInGame() {
    val game: Game? = askUserToChooseRunningGame()
    if (game != null) {
        val play: Play? = askUserToChoosePlay(game)
        if (play != null) {
            val newContents = readNextLine("Enter new contents: ")
            if (game.update(play.playId, Play(playContents = newContents))) {
                println("IPlay contents successfully updated!")
            } else {
                println("Play contents weren't updated!!! $redX")
            }
        } else {
            println("Invalid Play Id $redX")
        }
    }
}

/**
 * Queries the User to select a specific play from the list of stored Games.
 * It then prints a list of Plays for the Game that the User specified and asks for the id of the desired play.
 * Once completed it either returns an error message or the specific contentsIt also calls some unicode variables for emojis
 * to show a better UX design.
 */
private fun askUserToChoosePlay(game: Game): Play? {
    return if (game.amountOfPlays() > 0) {
        print(game.listPlays())
        game.findOne(readNextInt("\nEnter the id of the play: "))
    } else {
        println("No plays for chosen game $redX")
        null
    }
}

/**
 * Deletes a Play at a specified index that the User asks. It prints a success message
 * once the correct Play was found and was deleted from the Game object or an error message. It also calls some unicode variables for emojis
 * to show a better UX design.
 */
fun deleteAPlay() {
    val game: Game? = askUserToChooseRunningGame()
    if (game != null) {
        val play: Play? = askUserToChoosePlay(game)
        if (play != null) {
            val isDeleted = game.delete(play.playId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete wasn't Successful!!! $redX")
            }
        }
    }
}

/**
 * Prompts a User to change the status of whether a Game has been played yet through the stored list.
 * It checks to make sure that the Game is indeed running before the process concludes. If the Game is not in it's
 * running state, the system returns a null value.
 */
fun markPlayDesc() {
    val game: Game? = askUserToChooseRunningGame()
    if (game != null) {
        val play: Play? = askUserToChoosePlay(game)
        if (play != null) {
            val changeStatus: Char
            if (play.isPlayComplete) {
                changeStatus = readNextChar("The play is currently complete...do you want to mark it as TO-Be played?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    play.isPlayComplete = false
            } else {
                changeStatus = readNextChar("The play is currently TO-Be Played...do you want to mark it as Complete?")
                if ((changeStatus == 'Y') || (changeStatus == 'y'))
                    play.isPlayComplete = true
            }
        }
    }
}

/**
 * Searches through all the stored Plays by specific content that the User inputs into the console.
 * If Successful it returns the desired Play along with it's assigned Game.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun searchPlays() {
    val searchContents = readNextLine("Enter the play contents to search by: ")
    val searchResults = gameAPI.findPlayByDesc(searchContents)
    if (searchResults.isEmpty()) {
        println("No plays found $redX$magnifyingGlass")
    } else {
        println(searchResults)
    }
}

// ------------------------------------
// Persistence
// ------------------------------------

/**
 * Saves all that has been added to be stored through the specified persistence options.
 * It also catches exceptions that might interfere with the saving process.
 * If successful, it will save to a file with a designated name.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun save() {
    try {
        gameAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file $redX: $e")
    }
}

/**
 * If there is a previously saved file in the system it will attempt to access its contents and load them to the system.
 * If successful, display a success message to let the user know that the file has been imported into the system.
 * If it fails, it will display an error message letting the User know that there has been an issue trying to import the file.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun load() = try {
    gameAPI.load()
    println("File Extraction Successful! Games and Plays have been added to the system!")
} catch (e: Exception) {
    System.err.println("Error reading from file $redX: $e")
}

/**
 * Lists all the Games that have yet to be played in the system.
 * If the amount of Games is greater than 0, it will display the total amount of games to be played
 * left in the system.
 */
fun listToBePlayedPlays() {
    if (gameAPI.amountOfToBePlayedPlays() > 0) {
        println("Total To-Be played plays: ${gameAPI.amountOfToBePlayedPlays()}")
    }
    println(gameAPI.listToBePlayedPlays())
}

// ------------------------------------
// Exit App
// ------------------------------------

/**
 * When selected by the User, it will stop the program and close off the system.
 * As well as this, it will print a goodbye message to the console to see the User off.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
fun exitApp() {
    println("Goodbye! $waveSymbol")
    exitProcess(0)
}

// ------------------------------------
// HELPER FUNCTIONS
// ------------------------------------

/**
 * Asks the User to choose a running Game from the stored list on the system.
 * It will then ask for the ID of the Game that they want to specifically select, once selected,
 * it will verify that the Game is running. If it finds it to be running, it will then return the Game object.
 * It also calls some unicode variables for emojis to show a better UX design.
 */
private fun askUserToChooseRunningGame(): Game? {
    listRunningGames()
    if (gameAPI.amountOfRunningGames() > 0) {
        val game = gameAPI.findGame(readNextInt("\nEnter the id of the game: "))
        if (game != null) {
            if (game.isGameSavedForLater) {
                println("Game is NOT Running, it is Saved For Later")
            } else {
                return game // chosen game is running
            }
        } else {
            println("Game id is not valid $redX")
        }
    }
    return null // selected game is not running
}
