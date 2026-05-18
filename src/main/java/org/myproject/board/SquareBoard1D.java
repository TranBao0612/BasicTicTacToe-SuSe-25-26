package org.myproject.board;

import java.util.Arrays;


/**
 * This class represents a square board for a game with cells represented in a 1D array.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public class SquareBoard1D extends SquareBoard {
    /**
     * 1D array to represent cells of the board.
     */
    private int[] board;

    /**
     * Initialize the board with the given size.
     * @param size the size of the square board (number of rows/columns).
     */
    public SquareBoard1D(int size) {
        super(size);
        this.board = new int[total_cells];
        Arrays.fill(board, empty_cell_value);
    }

    /**
     * Get the value of the cell with the given id.
     * @param cell_id the id of the cell to get the value of.
     * @return the value of the cell with the given id.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public int getCellValue(int cell_id) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        return board[getIndex(cell_id)];
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public int getCellValue(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        return board[getIndex(row, col)];
    }


    /**
     * Set the value of the cell with the given id.
     * @param cell_id the id of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public void setCellValue(int cell_id, int value) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        board[getIndex(cell_id)] = value;
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public void setCellValue(int row, int col, int value) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        board[getIndex(row, col)] = value;
    }


    /**
     * Get the index in the 1D array for the given cell id.
     * @param cell_id the id of the cell to get the index for.
     * @return the index in the 1D array for the given cell id.
     */
    private int getIndex(int cell_id) {
        return cell_id - 1;
    }
    /**
     * Get the index in the 1D array for the given row and column.
     * @param row the row of the cell to get the index for.
     * @param col the column of the cell to get the index for.
     * @return the index in the 1D array for the given row and column.
     */
    private int getIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }
    
}
