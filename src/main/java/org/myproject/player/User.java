package org.myproject.player;

import org.myproject.io.IOService;
import org.myproject.board.SquareBoard;


public class User extends Player {
    public User(int player_id, IOService ioService) {
        super(player_id, ioService);
    }

    /**
     * User makes a decision by reading input from the IOService.
     * @param board the current state of the board.
     * @return the decision made by the user, which is the string input by the user.
     */
    @Override
    public String makeDecision(SquareBoard board) {
        String decision = ioService.nextLine();
        return decision;
    }
    
}

