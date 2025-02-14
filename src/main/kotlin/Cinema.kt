package cinema

import cinema.model.InvalidInputException
import cinema.model.MenuChoice
import cinema.model.MovieTheatre

// TODO - update exception messages
// That ticket has already been purchased!
// Wrong input!

fun main() {
    // initialize movie theatre
    val theatre = MovieTheatre()

    while (true) {
        try {       // prompt user for choices
            when (theatre.promptMenuChoice()) {
                MenuChoice.SHOW_SEATS -> theatre.printTheatre()         // show seats
                MenuChoice.BUY_TICKET -> theatre.purchaseTicket()       // buy ticket
                MenuChoice.STATISTICS -> theatre.printStatistics()      // show data
                MenuChoice.EXIT -> break                                // exit
            }
        } catch (e: Exception) {
            println(
                when (e) {  // print exception messages to screen
                    is InvalidInputException -> printCustomException(e)
                    else -> printException(e)
                }
            )
        }
    }
}

/**
 *  prints exception to screen
 *  @param e exception
 */
fun printException(e: Exception) = "\n$e\n"

/**
 *  prints custom exception class name & message to screen
 *  @param e exception
 */
fun printCustomException(e: Exception) = "\n${e.javaClass.name}: \"${e.localizedMessage}\"\n"