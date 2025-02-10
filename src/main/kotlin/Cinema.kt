package cinema

import cinema.model.MovieTheatre

fun main() {
    // create movie theatre
    val theatre = MovieTheatre()
    // set rows/seats values
    theatre.setTheatreSize()
    // print theatre to screen
    theatre.printTheatre()
    // select seat in theatre
    theatre.setSeatSelection()
    // print ticket price
    theatre.printTicketPrice()
    // print theatre to screen
    theatre.printTheatre()
}