package cinema

import cinema.model.InvalidInputException
import cinema.model.MovieTheatre
import cinema.model.ONE
import cinema.model.TWO

// TODO - create ENUM for menu prompt choices (1,2,3)
// TODO - promptMenuChoice function return ENUM
// TODO - create MovieTheatreLayout property for available seating locations
// TODO - update setSeatSelection to update MovieTheatreLayout availability

fun main() {
    // initialize movie theatre
    val theatre = MovieTheatre()

    // prompt user for choices
    while (true) {
        try {
            when (theatre.promptMenuChoice()) {
                ONE -> theatre.printTheatre()       // show seats
                TWO -> theatre.setSeatSelection()   // buy ticket
                else -> break                       // exit
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