package cinema.model

import cinema.*

/**
 *  Data class representing a movie theatre
 *  @param theatreRows the number of rows in the theatre
 *  @param theatreSeats the number of seats in each row
 *  @param theatreCurrentIncome current income from sold tickets
 *  @param theatrePossibleIncome income if all tickets are sold
 *  @param availableSeats map of available seats in the theatre
 */
data class MovieTheatre(
    private var theatreRows: Int = ZERO,
    private var theatreSeats: Int = ZERO,
    private var theatreCurrentIncome: Int = ZERO,
    private var theatrePossibleIncome: Int = ZERO,
    private val availableSeats: MutableMap<Int, MutableList<String>> = mutableMapOf(),
) {
    /**
     *  prompts the user for a number within
     *  the range of 0 to 3
     *  @see MenuChoice
     *  @throws InvalidInputException
     *  @throws NumberFormatException
     */
    fun promptMenuChoice(): MenuChoice {
        println("""
            ${MenuChoice.SHOW_SEATS.option}
            ${MenuChoice.BUY_TICKET.option}
            ${MenuChoice.STATISTICS.option}
            ${MenuChoice.EXIT.option}
        """.trimIndent())
        return when (getNumberFromUser()) {
            ONE -> MenuChoice.SHOW_SEATS
            TWO -> MenuChoice.BUY_TICKET
            THREE -> MenuChoice.STATISTICS
            ZERO -> MenuChoice.EXIT
            else -> throw InvalidInputException(
                INVALID_RANGE.replace(ASTERISK, "$THREE")
                    .replace("$ONE", "$ZERO")
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
        println()
        while (true) {
            try {
                var row = ZERO; var seat = ZERO
                repeat(TWO) {
                    when (it) {
                        ZERO -> {   // prompt user for row number
                            println(SELECTION_ROW)
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

                // get ticket price
                val ticketPrice = getTicketPrice(row)

                // update available seats
                updateAvailableSeats(row, seat)

                // update theatre income
                updateTheatreIncome(ticketPrice)

                // print ticket price
                printTicketPrice(ticketPrice)

                break

            } catch (e: Exception) {
                println(
                    when (e) {  // handle exceptions
                        is InvalidInputException,
                        is SeatUnavailableException -> printCustomException(e)
                        else -> printException(e)
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
     *  updates availableSeats variable to reflect purchased seats
     *  @param row chosen row in theatre
     *  @param seat purchased seat in theatre
     *  @see availableSeats
     */
    private fun updateAvailableSeats(row: Int, seat: Int) {
        availableSeats[row]?.set(seat.dec(), B_STRING)
    }

    /**
     *  updates the theatre's accrued income from sold tickets
     *  @param ticketPrice price of ticket for chosen seat
     */
    private fun updateTheatreIncome(ticketPrice: Int) {
        theatreCurrentIncome += ticketPrice
    }

    /**
     *  prints the ticket price for the user's
     *  selected row/seat value
     *  @param ticketPrice price of ticket for chosen seat
     */
    private fun printTicketPrice(ticketPrice: Int) = println("Ticket price: $$ticketPrice\n")

    /**
     *  prints information regarding the number of
     *  tickets sold, its value as a percentage,
     *  as well as current and possible income
     *  @see getTicketsSold
     *  @see getTicketsSoldPercentage
     *  @see theatreCurrentIncome
     *  @see theatrePossibleIncome
     */
    fun printStatistics() {
        println()
        println("""
            Number of purchased tickets: ${getTicketsSold()}
            Percentage: ${DECIMAL_FORMAT.format(getTicketsSoldPercentage())}%
            Current income: $$theatreCurrentIncome
            Total income: $$theatrePossibleIncome
        """.trimIndent())
        println()
    }

    /**
     *  returns the number of tickets that have been purchased
     *  @see availableSeats
     */
    private fun getTicketsSold() = availableSeats.values.flatten().count { it == B_STRING }

    /**
     *  returns the number of tickets sold as a percentage
     *  @see getTicketsSold
     */
    private fun getTicketsSoldPercentage(): Double {
        val totalTickets = theatreRows * theatreSeats
        val ticketsSold = getTicketsSold().toDouble()
        return (ticketsSold * ONE_HUNDRED) / totalTickets
    }

    /**
     *  sets the size of the movie theatre
     *  by prompting the user for both
     *  rows & seats values. Used to
     *  update class' private properties.
     *  Loops until valid inputs are received,
     *  Then updates availableSeats variable
     *  with default values & sets the theatre's
     *  possible income for selling all tickets
     *  @see MovieTheatre
     *  @see setDefaultAvailableSeats
     *  @see setPossibleIncome
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

                // set theatre's total possible income
                setPossibleIncome()

                break

            } catch (e: Exception) {
                println(
                    when (e) {
                        is InvalidInputException -> printCustomException(e)
                        else -> printException(e)
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

    /**
     *  sets the total income the theatre is
     *  able to make if all tickets are sold
     *  @see theatrePossibleIncome
     */
    private fun setPossibleIncome() {
        val total = theatreRows * theatreSeats
        theatrePossibleIncome = when {
            total < SIXTY -> total * TEN
            else -> {
                val frontHalf = (theatreRows / TWO_DUB).toInt() // $10 each
                val backHalf = theatreRows - frontHalf          // $8 each

                val frontTotal = (frontHalf * theatreSeats) * TEN
                val backTotal = (backHalf * theatreSeats) * EIGHT

                frontTotal + backTotal
            }
        }
    }

    init {
        setTheatreSize()
    }
}