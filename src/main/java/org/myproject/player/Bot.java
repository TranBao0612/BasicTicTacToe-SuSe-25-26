package org.myproject.player;

import org.myproject.io.IOService;
import org.myproject.board.SquareBoard;

/**
 * This class represents a bot player in a game. 
 */
public class Bot extends Player {
    public Bot(int player_id, IOService ioService) {
        super(player_id, ioService);
    }

    public Bot(int player_id) {
        super(player_id);
    }

    /**
     * Bot (computer player) makes a decision by selecting the first available empty cell on the board.
     * Prints the decision to the IOService if it is not null.
     * @param board the current state of the board.
     * @return the decision made by the bot, which is the index of the first empty cell on the board.
     */
    @Override
    public int makeDecision(SquareBoard board) {
        int decision = board.getEmptyCells().get(0);
        return decision;
    }
}

