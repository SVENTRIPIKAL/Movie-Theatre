package cinema.model

const val ZERO = 0
const val ONE = 1
const val TWO = 2
const val THREE = 3
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
const val INVALID_RANGE = "Number must be in range $ONE to *"
const val SEAT_UNAVAILABLE = "Chosen seat is unavailable for purchase"
const val MENU_ONE = "$ONE. Show the seats"
const val MENU_TWO = "$TWO. Buy a ticket"
const val MENU_THREE = "$THREE. Exit"

/**
 *  Data class representing a movie theatre
 *  @param theatreRows the number of rows in the theatre
 *  @param theatreSeats the number of seats in each row
 *  @param availableSeats map of available seats in the theatre
 */
data class MovieTheatre(
    private var theatreRows: Int = ZERO,
    private var theatreSeats: Int = ZERO,
    private val availableSeats: MutableMap<Int, MutableList<String>> = mutableMapOf(),
) {
    /**
     *  prompts the user for a number within
     *  the range of 1 to 3
     *  @see MenuChoice
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    fun promptMenuChoice(): MenuChoice {
        println("""
            $MENU_ONE
            $MENU_TWO
            $MENU_THREE
        """.trimIndent())
        return when (getNumberFromUser()) {
            ONE -> MenuChoice.SHOW_SEATS
            TWO -> MenuChoice.BUY_TICKET
            THREE -> MenuChoice.EXIT
            else -> throw InvalidInputException(
                INVALID_RANGE.replace(ASTERISK, "$THREE")
            )
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
     */
    fun printTheatre() {
        // print title
        println("\n$CINEMA_STRING").also { print(EMPTY_SPACE.repeat(TWO)) }

        // print seat numbers
        println((ONE..theatreSeats).joinToString(EMPTY_SPACE))

        // print row numbers & each seat's availability status
        availableSeats.entries.forEach { (key, value) ->
            println("$key ${value.joinToString(EMPTY_SPACE)}")

        }.also { println() }
    }

    /**
     *  sets the user's row/seat number by
     *  prompting the user for both values.
     *  Used to update the class' private
     *  availableSeats value
     *  @see MovieTheatre
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    fun purchaseTicket() {
        while (true) {
            try {
                var row = ZERO; var seat = ZERO
                repeat(TWO) {
                    when (it) {
                        ZERO -> {   // prompt user for row number
                            println("\n$SELECTION_ROW")
                            val input = getNumberFromUser()

                            if (input in ONE..theatreRows) row = input

                            else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$theatreRows")
                            )
                        }
                        else -> {   // prompt user for seat number
                            println(SELECTION_SEAT)
                            val input = getNumberFromUser()

                            if (input in ONE..theatreSeats) {

                                // check seat availability & update seat variable
                                if (isSeatAvailable(row, input)) seat = input

                                else throw SeatUnavailableException(SEAT_UNAVAILABLE)

                            } else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$theatreSeats")
                            )
                        }
                    }
                }.also { println() }

                // update available seats
                updateAvailableSeats(row, seat)

                // print ticket price
                printTicketPrice(row)

                break

            } catch (e: Exception) {
                println(
                    when (e) {
                        is InvalidInputException,
                        is SeatUnavailableException -> "\n${e.javaClass.name}: \"${e.localizedMessage}\""
                        else -> "\n$e\n"
                    }
                )
            }
        }
    }

    /**
     *  reads & converts user input to integer
     *  @throws NumberFormatException
     */
    private fun getNumberFromUser() = readln().trim().toInt()

    /**
     *  returns true if chosen seat is available for purchase
     *  @param row chosen row in theatre
     *  @param input user input for chosen seat in theatre
     */
    private fun isSeatAvailable(row: Int, input: Int) = availableSeats[row]?.get(input.dec()).equals(S_STRING)

    /**
     *  updates availableSeats variable to reflect purchased seats
     *  @param row chosen row in theatre
     *  @param seat purchased seat in theatre
     *  @see availableSeats
     */
    private fun updateAvailableSeats(row: Int, seat: Int) {
        availableSeats[row]?.set(seat.dec(), B_STRING)
    }

    /**
     *  prints the ticket price for the user's
     *  selected row/seat value
     *  @param row chosen row in theatre
     */
    private fun printTicketPrice(row: Int) = println("Ticket price: $${getTicketPrice(row)}\n")

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
     *  @param row chosen row in theatre
     */
    private fun getTicketPrice(row: Int): Int {
        val total = theatreRows * theatreSeats
        return when {
            total < SIXTY -> TEN
            else -> {
                val frontHalf = (theatreRows / TWO_DUB).toInt()
                if (row <= frontHalf) TEN else EIGHT
            }
        }
    }

    /**
     *  sets the size of the movie theatre
     *  by prompting the user for both
     *  rows & seats values. Used to
     *  update class' private properties.
     *  Loops until valid inputs are received,
     *  Then updates availableSeats variable
     *  with default values
     *  @see MovieTheatre
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    private fun setTheatreSize() {
        while (true) {
            try {
                repeat(TWO) {
                    when (it) {
                        ZERO -> {   // prompt for rows
                            println(THEATRE_ROWS)
                            val input = getNumberFromUser()
                            if (input in ONE..NINE) theatreRows = input
                            else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$NINE")
                            )
                        }
                        else -> {   // prompt for seats in each row
                            println(THEATRE_SEATS)
                            val input = getNumberFromUser()
                            if (input in ONE..NINE) theatreSeats = input
                            else throw InvalidInputException(
                                INVALID_RANGE.replace(ASTERISK, "$NINE")
                            )
                        }
                    }
                }.also { println() }

                // set default values for available seats
                setDefaultAvailableSeats()

                break

            } catch (e: Exception) {
                println(
                    when (e) {
                        is InvalidInputException -> "\n${e.javaClass.name}: \"${e.localizedMessage}\"\n"
                        else -> "\n$e\n"
                    }
                )
            }
        }
    }

    /**
     *  sets default values for available seats
     */
    private fun setDefaultAvailableSeats() {
        // create temp map for theatre layout
        val tempMap = mutableMapOf<Int, MutableList<String>>()

        // update temp map for available seats using rows/seats variables
        for (row in ONE..theatreRows) {
            val tempList = (ONE..theatreSeats).map { S_STRING }.toMutableList()

            tempMap[row] = tempList
        }
        // update available seats variable
        availableSeats.putAll(tempMap)
    }

    init {
        setTheatreSize()
    }
}