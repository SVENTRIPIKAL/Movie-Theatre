package cinema.model

import cinema.ONE
import cinema.TWO
import cinema.ZERO

private const val MENU_ONE = "$ONE. Show the seats"
private const val MENU_TWO = "$TWO. Buy a ticket"
private const val MENU_ZERO = "$ZERO. Exit"

/**
 *  enum class replacing user input for MenuChoice
 *  @property SHOW_SEATS print theatre to screen
 *  @property BUY_TICKET update available seats
 *  @property EXIT end program
 */
enum class MenuChoice(val option: String) {
    SHOW_SEATS(MENU_ONE),
    BUY_TICKET(MENU_TWO),
    EXIT(MENU_ZERO)
}