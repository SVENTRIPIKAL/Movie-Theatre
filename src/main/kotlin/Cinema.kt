package cinema

import cinema.model.InvalidInputException
import cinema.model.MenuChoice
import cinema.model.MovieTheatre

fun main() {
    // initialize movie theatre
    val theatre = MovieTheatre()

    // prompt user for choices
    while (true) {
        try {
            when (theatre.promptMenuChoice()) {
                MenuChoice.SHOW_SEATS -> theatre.printTheatre()         // show seats
                MenuChoice.BUY_TICKET -> theatre.purchaseTicket()       // buy ticket
                MenuChoice.EXIT -> break                                // exit
            }
        } catch (e: Exception) {
            println(
                when (e) {  // print exception messages to screen
                    is InvalidInputException -> "\n${e.javaClass.name}: \"${e.error}\"\n"
                    else -> "\n$e\n"
                }
            )
        }
    }
}