package org.myproject.common.player;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.Message;
import org.myproject.common.io.IOService;


public class User extends Player {
    public User(int player_id, IOService ioService) {
        super(player_id, ioService);
        this.promptMessage = Message.PROMPT_FLAG + this.promptMessage; // Add prompt flag to the message
    }

    /**
     * User makes a decision by reading input from the IOService.
     * @param board the current state of the board.
     * @return the decision made by the user, or -1 if the user wants to quit.
     */
    @Override
    public int makeDecision(SquareBoard board) {
        while (true) {
            String input = ioService.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                ioService.println(Message.QUIT_MESSAGE);
                return -1; // User wants to quit
            }
            try {
                int decision = Integer.parseInt(input);
                if (board.isValidCell(decision)) {
                    // If cell id is in valid range and empty.
                    if (board.isCellEmpty(decision))
                        return decision;
                    ioService.println(Message.CELL_ALREADY_OCCUPIED_MESSAGE);
                } else {
                    ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
                }
            } catch (NumberFormatException e) {
                ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
            } catch (Exception e) {
                ioService.println("An unexpected error occurred: " + e.getMessage());
            }
            // Prompt the user again for input after handling invalid input or exceptions.
            ioService.println(promptMessage);
        }
    }

}

