package cinema.model

/**
 *  enum class replacing user input for MenuChoice
 *  @property SHOW_SEATS print theatre to screen
 *  @property BUY_TICKET update available seats
 *  @property EXIT end program
 */
enum class MenuChoice {
    SHOW_SEATS,
    BUY_TICKET,
    EXIT
}