package cinema.model

const val ZERO = 0
const val ONE = 1
const val TWO = 2
const val TWO_DUB = 2.0
const val EIGHT = 8
const val NINE = 9
const val TEN = 10
const val SIXTY = 60
const val ASTERISK = "*"
const val B_STRING = "B"
const val S_STRING = "S"
const val EMPTY_SPACE = " "
const val CINEMA_STRING = "Cinema:"
const val THEATRE_ROWS = "Enter the number of rows:"
const val SELECTION_ROW = "Enter a row number:"
const val THEATRE_SEATS = "Enter the number of seats in each row:"
const val SELECTION_SEAT = "Enter a seat number in that row:"
const val INVALID_RANGE = "Number must be in range 1 to *"

/**
 *  Data class representing a movie theatre
 *  @param theatreRows the number of rows in the theatre
 *  @param theatreSeats the number of seats in each row
 *  @param userSelection the user's chosen row/seat value
 */
data class MovieTheatre(
    private var theatreRows: Int = ZERO,
    private var theatreSeats: Int = ZERO,
    private val userSelection: MutableList<Int> = mutableListOf(ZERO, ZERO)
) {
    /**
     *  sets the size of the movie theatre
     *  by prompting the user for both
     *  rows & seats values. Used to
     *  update class' private properties
     *  @see MovieTheatre
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    fun setTheatreSize() {
        while (true) {
            try {
                repeat(TWO) {
                    when (it) {
                        ZERO -> {
                            println(THEATRE_ROWS)
                            val input = readln().toInt()
                            if (input in ONE..NINE) theatreRows = input
                            else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$NINE")
                            )
                        }
                        else -> {
                            println(THEATRE_SEATS)
                            val input = readln().toInt()
                            if (input in ONE..NINE) theatreSeats = input
                            else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$NINE")
                            )
                        }
                    }
                }.also { println() }
                break
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

    /**
     *  prints the movie theatre to screen
     *  using the class' rows & seats
     *  private properties. Both values
     *  must be in range 1 to 9. If user
     *  has chosen a row/seat value, the
     *  'S' at the specific coordinate will
     *  then be replaced with a 'B' instead
     *  @see MovieTheatre
     *  @see isTheatreSizeSet
     *  @see isUserSelectionSet
     */
    fun printTheatre() {
        if (isTheatreSizeSet()) {
            val cinema = mutableListOf<MutableList<String>>()
            for (row in ONE..theatreRows) {
                val tempList = mutableListOf<String>()
                for (seat in ONE..theatreSeats) {
                    tempList.add(S_STRING)
                }
                cinema.add(tempList)
            }

            if (isUserSelectionSet()) cinema[userSelection[ZERO].dec()][userSelection[ONE].dec()] = B_STRING

            println(CINEMA_STRING).also { print(EMPTY_SPACE.repeat(TWO)) }
            println((ONE..theatreSeats).joinToString(EMPTY_SPACE))
            repeat(theatreRows) { index ->
                println("${index.inc()} ${cinema[index].joinToString(EMPTY_SPACE)}")
            }.also { println() }
        }
    }

    /**
     *  sets the user's row/seat number by
     *  prompting the user for both values.
     *  Used to update the class' private
     *  userSelection value
     *  @see MovieTheatre
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    fun setSeatSelection() {
        if (isTheatreSizeSet()) {
            while (true) {
                try {
                    repeat(TWO) {
                        when (it) {
                            ZERO -> {
                                println(SELECTION_ROW)
                                val input = readln().toInt()
                                if (input in ONE..theatreRows) userSelection[ZERO] = input
                                else throw InvalidInputException(
                                    INVALID_RANGE.replace(ASTERISK, "$theatreRows")
                                )
                            }
                            else -> {
                                println(SELECTION_SEAT)
                                val input = readln().toInt()
                                if (input in ONE..theatreSeats) userSelection[ONE] = input
                                else throw InvalidInputException(
                                    INVALID_RANGE.replace(ASTERISK, "$theatreSeats")
                                )
                            }
                        }
                    }.also { println() }
                    break
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
    }

    /**
     *  prints the ticket price for the user's
     *  selected row/seat value
     */
    fun printTicketPrice() = println("Ticket price: $${getTicketPrice()}\n")

    /**
     *  returns true if theatre rows & seats set to numbers in range 1 to 9
     *  @see theatreRows
     *  @see theatreSeats
     */
    private fun isTheatreSizeSet() = theatreRows in ONE..NINE && theatreSeats in ONE..NINE

    /**
     *  returns true if userSelection row/seat is set to numbers in theatre size range
     *  @see theatreRows
     *  @see theatreSeats
     *  @see userSelection
     */
    private fun isUserSelectionSet() = userSelection[ZERO] in ONE..theatreRows &&
                                        userSelection[ONE] in ONE..theatreSeats

    /**
     *  returns the ticket price for the
     *  user's selected row/seat value.
     *  If total seats is less than 60, then
     *  each ticket is $10; else, the total
     *  size of the theatre is split in half,
     *  resulting in $10 front half tickets,
     *  and $8 back half tickets
     *  @see theatreRows
     *  @see theatreSeats
     *  @see userSelection
     */
    private fun getTicketPrice(): Int {
        val total = theatreRows * theatreSeats
        return when {
            total < SIXTY -> TEN
            else -> {
                val frontHalf = (theatreRows / TWO_DUB).toInt()
                if (userSelection[ZERO] <= frontHalf) TEN else EIGHT
            }
        }
    }
}