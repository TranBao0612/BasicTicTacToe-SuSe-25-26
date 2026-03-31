package org.myproject.board;

import java.lang.Exception;


/**
 * This represent each cell in the Tic-tac-toe board, 
 *      with id for each cell and state of the cell (0 = unchecked, others integer -> checked by players)
 */
public class Cell {
    private int state = 0;
    private int id;

    public Cell(int id) {
        this.id = id;
    }

    public int get_state() {
        return state;
    }

    public int get_id() {
        return id;
    }

    /**
     * To update cell's state
     * @param player_id
     * @throws Exception If cell has been occupied, this method will raised an error
     */
    public void checked_by_player(int player_id) throws Exception {
        if(!is_available()) {
            throw new Exception("Cell has been occupied!");
        }
        this.state = player_id;
    }

    /**
     * To check availability of the cell
     * @return true if cell state = 0
     */
    public boolean is_available() {
        return this.state == 0? true:false;
    }

    /**
     * Convert cell to string
     */
    public String toString() {
        return String.valueOf(state);
    }
}
