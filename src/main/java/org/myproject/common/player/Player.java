package org.myproject.common.player;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.Message;
import org.myproject.common.io.IOService;

/**
 * This class represents a player in the game.
 * Each player has a unique id.
 * 
 */
public abstract class Player {
    protected int player_id;
    protected IOService ioService;
    protected String promptMessage;

    protected Player(int player_id, IOService ioService) {
        this.player_id = player_id;
        this.ioService = ioService;
        this.promptMessage = Message.getPlayersTurnMessage(player_id);
    }

    protected Player(int player_id) {
        this(player_id, null);
    }

    /**
     * Get the id of the player.
     * @return the id of the player.
     */
    public int getPlayerId() {
        return player_id;
    }

    /**
     * Make a move for the player based on the current state of the board.
     * @param board the current state of the board.
     * @return the move made by the player.
     */
    public abstract int makeDecision(SquareBoard board, boolean withTag);
    public int makeDecision(SquareBoard board) {
        return makeDecision(board, false);
    }
}

