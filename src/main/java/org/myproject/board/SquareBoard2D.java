package org.myproject.board;

import java.util.Arrays;

/**
 * This class represents a square board for a game with cells represented in a 2D array.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public class SquareBoard2D extends SquareBoard {
    /**
     * 2D array to represent cells of the board.
     */
    private int[][] board;

    /**
     * Initialize the board with the given size.
     * @param size the size of the square board (number of rows/columns).
     */
    public SquareBoard2D(int size) {
        super(size);
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], empty_cell_value);
        }
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
        return board[getRowIndex(cell_id)][getColIndex(cell_id)];
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
        return board[getRowIndex(row, col)][getColIndex(col, row)];
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
        board[getRowIndex(cell_id)][getColIndex(cell_id)] = value;
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
        board[getRowIndex(row, col)][getColIndex(col, row)] = value;
    }



    /**
     * Get the row index of the cell in the 2D array with the given id.
     * @param cell_id the id of the cell to get the row index of.
     * @return the row index of the cell with the given id.
     */
    private int getRowIndex(int cell_id) {
        return (cell_id - 1) / size;
    }
    /**
     * Get the row index of the cell in the 2D array with the given row and column.
     * @param row the row of the cell to get the row index of.
     * @param col the column of the cell to get the row index of.
     * @return the row index of the cell with the given row and column.
     */
    private int getRowIndex(int row, int col) {
        return row - 1;
    }
    /**
     * Get the column index of the cell in the 2D array with the given id.
     * @param cell_id the id of the cell to get the column index of.
     * @return the column index of the cell with the given id.
     */
    private int getColIndex(int cell_id) {
        return (cell_id - 1) % size;
    }
    /**
     * Get the column index of the cell in the 2D array with the given row and column.
     * @param col the column of the cell to get the column index of.
     * @param row the row of the cell to get the column index of.
     * @return the column index of the cell with the given row and column.
     */
    private int getColIndex(int col, int row) {
        return col - 1;
    }
    
}
