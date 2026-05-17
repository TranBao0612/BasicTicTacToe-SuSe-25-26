package org.myproject.board;

import java.util.ArrayList;
import java.io.PrintStream;

/**
 * This class represents a square board for a game. <br>
 *    Cell id starts from 1 and increase row-wise from top-left to bottom-right. <br>
 *    For setter and getter methods by row and column, row and column indices also start from 1. <br>
 *    The value of a cell can be set to represent the player occupying it, or a special value to represent an empty cell. <br>
 * The actual implementation of the board is left to subclasses, this class itself is abstract and cannot be instantiated directly.   
 */
public abstract class SquareBoard {
    /**
     * The value that represents an empty cell. <br>
     * This value can be set by the constructor as 0 by default if not specified.
     */
    protected int EMPTY_CELL_VALUE;
    /**
     * The size of the board (number of rows and columns).
     */
    protected int size;
    /**
     * The PrintStream to be used for printing the board. <br>
     * This can be set by the constructor as System.out by default if not specified.
     */
    protected PrintStream print_stream;


    // ---------Constructor---------
    public SquareBoard(int size, int empty_cell_value, PrintStream print_stream) {
        this.size = size;
        this.EMPTY_CELL_VALUE = empty_cell_value;
        this.print_stream = print_stream;
    }
    public SquareBoard(int size, int empty_cell_value) {
        this(size, empty_cell_value, System.out);
    }





    // ---------Abstract methods---------
    /**
     * Get the value of a cell by its id (starting from 1).
     * @param cell_id the id of the cell (starting from 1)
     * @return the value of the cell
     */
    public abstract int get_value(int cell_id);
    /**
     * Set the value of a cell by its id (starting from 1) to represent the player occupying it.
     * @param cell_id the id of the cell (starting from 1)
     * @param player_id the id of the player occupying the cell
     */
    public abstract void set_value(int cell_id, int player_id);
    /**
     * Get the value of a cell by its row and column indices (both starting from 1).
     * @param row the row index of the cell (starting from 1)
     * @param col the column index of the cell (starting from 1)
     * @return the value of the cell
     */
    public abstract int get_value(int row, int col);
    /**
     * Set the value of a cell by its row and column indices (both starting from 1) to represent the player occupying it.
     * @param row the row index of the cell (starting from 1)
     * @param col the column index of the cell (starting from 1)
     * @param player_id the id of the player occupying the cell
     */
    public abstract void set_value(int row, int col, int player_id);





    // ---------Concrete methods---------
    public int get_size() {
        return size;
    }
    public int get_empty_cell_value() {
        return EMPTY_CELL_VALUE;
    }
    public int get_number_of_cells() {
        return size * size;
    }

    /**
     * Get a list of all empty cell ids on the board.
     * @return an ArrayList of empty cell ids
     */
    public ArrayList<Integer> get_empty_cells() {
        ArrayList<Integer> empty_cells = new ArrayList<>();
        for (int cell_id = 1; cell_id <= size * size; cell_id++) {
            if (is_empty(cell_id))
                empty_cells.add(cell_id);
        }
        return empty_cells;
    }



    /**
     * Check if a cell is empty by its id (starting from 1).
     * @param cell_id the id of the cell (starting from 1)
     * @return true if the cell is empty, false otherwise
     */
    public boolean is_empty(int cell_id) {
        return get_value(cell_id) == EMPTY_CELL_VALUE;
    }
    /**
     * Check if a cell is empty by its row and column indices (both starting from 1).
     * @param row the row index of the cell (starting from 1)
     * @param col the column index of the cell (starting from 1)
     * @return true if the cell is empty, false otherwise
     */
    public boolean is_empty(int row, int col) {
        return get_value(row, col) == EMPTY_CELL_VALUE;
    }



    /**
     * Check if a cell id is in valid range.
     * @param cell_id the id of the cell (starting from 1)
     * @return true if the cell id is valid, false otherwise
     */
    public boolean is_valid_cell_id(int cell_id) {
        return cell_id > 0 && cell_id <= size*size;
    }
    /**
     * Check if row and column indices are in valid range.
     * @param row the row index of the cell (starting from 1)
     * @param col the column index of the cell (starting from 1)
     * @return true if the row and column indices are valid, false otherwise
     */
    public boolean is_valid_cell_id(int row, int col) {
        return row > 0 && row <= size && col > 0 && col <= size;
    }



    /**
     * Set the value that represents an empty cell. This method should also update all existing empty cells to the new value.
     * @param value the new value that represents an empty cell
     */
    public void set_empty_value(int value) {
        int old_empty_value = this.EMPTY_CELL_VALUE;
        this.EMPTY_CELL_VALUE = value;
        for(int cell_id = 1; cell_id <= size*size; cell_id++) {
            if (get_value(cell_id) == old_empty_value)
                set_value(cell_id, value);
        }
    };



    /**
     * Check if the board is full (i.e., no empty cells).
     * @return true if the board is full, false otherwise
     */
    public boolean is_full() {
        for(int cell_id = 1; cell_id <= size*size; cell_id++) {
            if (get_value(cell_id) == EMPTY_CELL_VALUE)
                return false;
        }
        return true;
    }


    
    /**
     * Get a string representation of the board for display purposes.
     * @return a string representing the board
     */
    public String to_string() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= size; i++) {
            for(int j = 1; j <= size; j++) {
                sb.append("| ").append(get_value(i, j)).append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    };

    /**
     * Print the board to the console using the string representation from to_string() method.
     */
    public void print() {
        print_stream.println(to_string());
    }
}