package org.myproject.player;

import org.myproject.board.Board;


/**
 * Class represent a Bot (computer player) with a player_id and method to make decision
 */
public class Bot extends Player {
    public Bot() {
        super(2);
    }


    /**
     * Bot (computer) move with basic strategy: choose 1st available cell
     * @param board
     */
    public void move(Board board) {
        try {
            board.player_choose(player_id, make_decision(board));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Bot (computer) make decision with basic strategy: choose 1st available cell
     * @param board
     * @return cell id of the cell on tic-tac-toe board
     */
    protected int make_decision(Board board) {
        int cell_id = board.available_cells().get(0);
        System.out.printf("Player %d choose %d.\n", player_id, cell_id);
        return cell_id;
    }
}
