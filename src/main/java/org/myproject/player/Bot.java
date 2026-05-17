package org.myproject.player;

import java.io.PrintStream;

import org.myproject.board.SquareBoard;


/**
 * Class represent a Bot (computer player) with a player_id and method to make decision
 */
public class Bot extends Player {
    public Bot(int id, PrintStream printStream) {
        super(id, printStream);
    }
    public Bot(int id) {
        this(id, System.out);
    }


    /**
     * Bot (computer) make decision with basic strategy: choose 1st available cell
     * @param board
     * @return cell id of the cell on tic-tac-toe board
     */
    public int make_decision(SquareBoard board) {
        int decision = board.get_empty_cells().get(0);
        print_stream.println(decision_console_string + decision);
        return decision;
    }
}
