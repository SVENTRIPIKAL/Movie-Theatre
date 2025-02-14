package cinema.model

/**
 *  data class inheriting from Exception. Stores
 *  a String value for the cause of error when thrown.
 *  Used to report Invalid Entries within program
 */
data class InvalidInputException(val error: String): Exception(error)

/**
 *  data class inheriting from Exception. Stores
 *  a String value for the cause of error when thrown.
 *  Used to report Unavailable Seats when buying a ticket
 */
data class SeatUnavailableException(val error: String): Exception(error)