package cinema.model

/**
 *  data class inheriting from Exception. Stores
 *  a String value for the cause of error when thrown
 */
data class InvalidInputException(val error: String): Exception(error)