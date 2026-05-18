package org.myproject.player;

import org.myproject.io.IOService;
import org.myproject.board.SquareBoard;

/**
 * This class represents a player in the game.
 * Each player has a unique id.
 * 
 */
public abstract class Player {
    protected int player_id;
    protected IOService ioService;

    protected Player(int player_id, IOService ioService) {
        this.player_id = player_id;
        this.ioService = ioService;
    }

    /**
     * Get the id of the player.
     * @return the id of the player.
     */
    public int getPlayerId() {
        return player_id;
    }

    /**
     * Make a decision for the player based on the current state of the board.
     * @param board the current state of the board.
     * @return the decision made by the player.
     */
    public abstract String makeDecision(SquareBoard board);
}

