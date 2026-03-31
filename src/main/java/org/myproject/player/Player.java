package org.myproject.player;

import org.myproject.board.Board;

/** 
 * Class to be polymorphism
*/
public class Player {
    protected int player_id;

    /**
     * Constructor is set to protected to prevent initialization
     */
    protected Player(int id) {
        player_id = id;
    }

    public void move(Board board) {}

    protected int make_decision(Board board) {
        return -1;
    };
}
