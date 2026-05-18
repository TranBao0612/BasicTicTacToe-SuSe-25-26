package org.myproject.game;

import org.myproject.board.SquareBoard;
import org.myproject.constant.Message;
import org.myproject.io.IOService;

/**
 * A utility class to validate user input for the tic-tac-toe game. <br>
 * It checks if the user input is valid and prints appropriate messages if the input is invalid. <br>
 * The valid input can be either a string defined by VALID_INPUT_STRING_REGEX 
 *      or a number representing an empty cell on the board.
*/
public final class TictactoeInputValidator {
    private static final String VALID_INPUT_STRING_REGEX = "^q$";

    /**
     * Validate the user input. If the input is invalid, print an error message.
     * @param user_input the user input to validate
     * @param ioService the IOService instance to print messages to the user
     * @param board the current state of the board to validate the user input against
     * @return true if the user input is valid, false otherwise
     */
    public static boolean validate(String user_input, IOService ioService, SquareBoard board) {
        user_input = user_input.trim();
        if (user_input.matches(VALID_INPUT_STRING_REGEX)) {
            return true;
        } else {
            try {
                int decision = Integer.parseInt(user_input);
                if (board.isValidCell(decision)) {
                    // If cell id is in valid range and empty, return the decision.
                    if (board.isCellEmpty(decision))
                        return true;
                    // If cell id is in valid range but not empty, print the message and ask for input again.
                    ioService.println(Message.CELL_ALREADY_OCCUPIED_MESSAGE);
                } else {
                    // If cell id is not in valid range, print the message and ask for input again.
                    ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
                }
            } catch (NumberFormatException e) {
                // If user input is not a number, print the message and ask for input again.
                ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
            }
            // If user input is invalid, return false.
            return false;
        }
    }
}
