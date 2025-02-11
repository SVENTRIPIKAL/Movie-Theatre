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
const val MENU_ONE = "$ONE. Show the seats"
const val MENU_TWO = "$TWO. Buy a ticket"
const val MENU_THREE = "$THREE. Exit"

/**
 *  Data class representing a movie theatre
 *  @param theatreRows the number of rows in the theatre
 *  @param theatreSeats the number of seats in each row
 *  @param userSelection the user's chosen row/seat value
 *  @param availableSeats map of available seats in the theatre
 */
data class MovieTheatre(
    private var theatreRows: Int = ZERO,
    private var theatreSeats: Int = ZERO,
    private val userSelection: MutableList<Int> = mutableListOf(ZERO, ZERO),
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
     *  @see isTheatreSizeSet
     *  @see isUserSelectionSet
     */
    fun printTheatre() {
        if (isTheatreSizeSet()) {
            // print title
            println("\n$CINEMA_STRING").also { print(EMPTY_SPACE.repeat(TWO)) }

            // print seat numbers
            println((ONE..theatreSeats).joinToString(EMPTY_SPACE))

            // print row numbers & each seat's availability status
            availableSeats.entries.forEach { (key, value) ->
                println("$key ${value.joinToString(EMPTY_SPACE)}")

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
    fun purchaseTicket() {
        if (isTheatreSizeSet()) {
            while (true) {
                try {
                    repeat(TWO) {
                        when (it) {
                            ZERO -> {
                                // TODO - receive user input
                                // TODO - check if input in bought seats
                                // TODO - update available seats
                                println(SELECTION_ROW)
                                val input = getNumberFromUser()
                                if (input in ONE..theatreRows) userSelection[ZERO] = input
                                else throw InvalidInputException(
                                    INVALID_RANGE.replace(ASTERISK, "$theatreRows")
                                )
                            }
                            else -> {
                                println(SELECTION_SEAT)
                                val input = getNumberFromUser()
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
    private fun printTicketPrice() = println("Ticket price: $${getTicketPrice()}\n")

    /**
     *  reads & converts user input to integer
     *  @throws NumberFormatException
     */
    private fun getNumberFromUser() = readln().trim().toInt()

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
                        is InvalidInputException -> "\n${e.javaClass.name}: \"${e.error}\"\n"
                        else -> "\n$e\n"
                    }
                )
            }
        }
    }

    init {
        setTheatreSize()
    }
}