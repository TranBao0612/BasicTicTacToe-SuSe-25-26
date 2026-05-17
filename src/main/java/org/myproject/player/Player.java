package org.myproject.player;

import java.io.PrintStream;

import org.myproject.board.SquareBoard;

/** 
 * Class represent a Player (either user or bot) with a player_id and method to make a move on the board
*/
public abstract class Player {
    /**
     * Player id to identify the player, for example: 1 for user and 2 for bot
     */
    protected int player_id;
    /**
     * PrintStream to print messages to the console, default is System.out if not specified
     */
    protected PrintStream print_stream;
    /**
     * String to prompt the player to make a decision, for example: "Player#1's turn: " for user and "Player#2's turn: " for bot
     */
    protected String decision_console_string;

    protected Player(int id, PrintStream print_stream) {
        player_id = id;
        this.print_stream = print_stream;
        decision_console_string = "Player#" + player_id + "'s turn: ";
    }
    protected Player(int id) {
        this(id, System.out);
    }


    /**
     * Method to make a decision on the board, to be implemented by subclasses
     * @param board the board of current game state
     * @return the position where the player wants to place their mark
     */
    public abstract int make_decision(SquareBoard board);


    /**
     * Getter for player_id
     * @return the player_id
     */
    public int get_player_id() {
        return player_id;
    }


    /**
     * Method to make a move on the board, to be implemented by subclasses
     * @param board the board of current game state
     */
    public void move(SquareBoard board) {
        board.set_value(make_decision(board), player_id);
    };
}
