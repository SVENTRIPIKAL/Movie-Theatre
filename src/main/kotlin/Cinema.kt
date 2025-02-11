package cinema

import cinema.model.InvalidInputException
import cinema.model.MenuChoice
import cinema.model.MovieTheatre

// TODO - create MovieTheatreLayout property for available seating locations
// TODO - update setSeatSelection to update MovieTheatreLayout availability

fun main() {
    // initialize movie theatre
    val theatre = MovieTheatre()

    // prompt user for choices
    while (true) {
        try {
            when (theatre.promptMenuChoice()) {
                MenuChoice.SHOW_SEATS -> theatre.printTheatre()         // show seats
                MenuChoice.BUY_TICKET -> theatre.setSeatSelection()     // buy ticket
                MenuChoice.EXIT -> break                                // exit
            }
        } catch (e: Exception) {
            println(
                when (e) {
                    is InvalidInputException -> "\n${e.javaClass.name}: \"${e.error}\"\n"
                    else -> "\n$e\n"
                }
            )
        }
    }
}