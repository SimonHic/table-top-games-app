import controllers.GameAPI
import models.Play
import models.Game
import persistence.JSONSerializer
// import persistence.XMLSerializer
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import kotlin.system.exitProcess
import org.fusesource.jansi.Ansi.ansi

/**Uncomment and Comment to alternate between the two*/
// private val gameAPI = GameAPI(XMLSerializer(File("games.xml")))
private val gameAPI = GameAPI(JSONSerializer(File("games.json")))
fun main() = runMenu()

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
         > @|yellow |  4) Delete a game $trashCan                            |@  |
         > @|green | 5) Save a game for later $floppyDisk                     |@  |
         > @|cyan -----------------------------------------------------|@  
         > | Play MENU                                         | 
         > @|green | 6) Add play to a game $plusSign                        |@  |
         > @|yellow | 7) Update play contents on a game $pencil            |@  |
         > @|yellow | 8) Delete play from a game $trashCan                   |@  |
         > @|blue | 9) Mark play as played/to-be played $pencil          |@  | 
         > @|cyan -----------------------------------------------------|@  
         > | REPORT MENU FOR GAMES                             | 
         > @|blue |   10) Search for games $magnifyingGlass (by game name)       |@   |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > @|green -----------------------------------------------------|@  
         > | REPORT MENU FOR PLAYS                             |                                
         > @|blue |   15) Search for plays $magnifyingGlass (by play description)  |@ |
         > @|blue |   16) List TO-BE played Plays $listSymbol                 |@ |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > @|cyan -----------------------------------------------------|@ 
         > @|magenta |   19) Save $floppyDisk                                   |@  |
         > @|magenta |   20) Load                                      |@  |
         > @|cyan -----------------------------------------------------|@  
         > @|red |  0) Exit                                        |@  |
         > @|cyan -----------------------------------------------------|@  
         > ==>> """.trimMargin(">")).toString()
)
}

// ------------------------------------
// Game MENU
// ------------------------------------

/** Assigned unicode emojis to variables for easy use*/
const val starSymbol = "\u2B50" // Unicode for star emoji assigned as a variable
const val magnifyingGlass = "\uD83D\uDD0D"
const val floppyDisk = "\uD83D\uDCBE"
const val redX = "\u274C"
const val waveSymbol = "\uD83D\uDC4B"
const val pencil = "\u270F"
const val trashCan = "\uD83D\uDDD1️\uFE0F"
const val plusSign = "\u2795"
const val listSymbol = "\uD83D\uDCD3"

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

fun listAllGames() = println(gameAPI.listAllGames())
fun listRunningGames() = println(gameAPI.listRunningGames())
fun listSavedForLaterGames() = println(gameAPI.listSavedForLaterGames())

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
fun searchGames() {
    val findName = readNextLine("Enter the description to search by $magnifyingGlass: ")
    val searchResults = gameAPI.findGameByName(findName)
    if (searchResults.isEmpty()) {
        println("No games were found $redX$magnifyingGlass")
    } else {
        println(searchResults)
    }
}

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

private fun askUserToChoosePlay(game: Game): Play? {
    return if (game.amountOfPlays() > 0) {
        print(game.listPlays())
        game.findOne(readNextInt("\nEnter the id of the play: "))
    } else {
        println("No plays for chosen game $redX")
        null
    }
}

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
fun save() {
    try {
        gameAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file $redX: $e")
    }
}

fun load() = try {
    gameAPI.load()
    println("File Extraction Successful! Games and Plays have been added to the system!")
} catch (e: Exception) {
    System.err.println("Error reading from file $redX: $e")
}

fun listToBePlayedPlays() {
    if (gameAPI.amountOfToBePlayedPlays() > 0) {
        println("Total To-Be played plays: ${gameAPI.amountOfToBePlayedPlays()}")
    }
    println(gameAPI.listToBePlayedPlays())
}

// ------------------------------------
// PLAY REPORTS MENU
// ------------------------------------

//

// ------------------------------------
// Exit App
// ------------------------------------
fun exitApp() {
    println("Goodbye! $waveSymbol")
    exitProcess(0)
}

// ------------------------------------
// HELPER FUNCTIONS
// ------------------------------------

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
