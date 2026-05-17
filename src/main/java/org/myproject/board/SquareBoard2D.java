package org.myproject.board;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * This class represents a square board for a game. <br>
 *    Cell id starts from 1 and increase row-wise from top-left to bottom-right. <br>
 *    For setter and getter methods by row and column, row and column indices also start from 1. <br>
 *    The value of a cell can be set to represent the player occupying it, or a special value to represent an empty cell. <br>
 * The actual implementation involving a 2D array is left to this class.   
 */
public class SquareBoard2D extends SquareBoard {
    private int[][] cells;

    // ---------Constructor---------
    /**
     * Constructor to initialize an empty board with a specified size and empty cell value.
     * @param size the size of the board (number of rows and columns)
     * @param empty_cell_value the value that represents an empty cell
     * @param print_stream the PrintStream to be used for printing the board
     */
    public SquareBoard2D(int size, int empty_cell_value, PrintStream print_stream) {
        super(size, empty_cell_value, print_stream);
        this.cells = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(cells[i], EMPTY_CELL_VALUE);
        }
    }
    /**
     * Constructor to initialize an empty board with a specified size and empty cell value, using the default print stream.
     * @param size the size of the board (number of rows and columns)
     * @param empty_cell_value the value that represents an empty cell
     */
    public SquareBoard2D(int size, int empty_cell_value) {
        this(size, empty_cell_value, System.out);
    }
    /**
     * Constructor to initialize an empty board with a specified size and default empty cell value of 0.
     * @param size the size of the board (number of rows and columns)
     */
    public SquareBoard2D(int size) {
        this(size, 0, System.out);
    }




    // ---------Override methods---------
    /**
     * {@inheritDoc}
     */
    @Override
    public int get_value(int cell_id) {
        return cells[(cell_id-1) / size][(cell_id-1) % size];
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int get_value(int row, int col) {
        return cells[row-1][col-1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set_value(int cell_id, int player_id) {
        cells[(cell_id-1) / size][(cell_id-1) % size] = player_id;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void set_value(int row, int col, int player_id) {
        cells[row-1][col-1] = player_id;
    }
}
