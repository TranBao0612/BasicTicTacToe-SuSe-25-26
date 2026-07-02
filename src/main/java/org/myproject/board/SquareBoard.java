package org.myproject.board;

import org.myproject.constant.Constant;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a square board for a game.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public abstract class SquareBoard {
    protected int size;
    protected int total_cells;
    protected int empty_cell_value = Constant.EMPTY_CELL_VALUE;

    protected SquareBoard(int size) {
        this.size = size;
        this.total_cells = size * size;
    }


    // ------------------ Abstract methods to be implemented by subclasses ------------------
    public abstract int getCellValue(int cell_id);
    public abstract int getCellValue(int row, int col);

    public abstract void setCellValue(int cell_id, int value);
    public abstract void setCellValue(int row, int col, int value);



    // ------------------ Concrete methods implemented in this class ------------------
    /**
     * Get a list of all cell values on the board, in row-major order (i.e., left to right, top to bottom).
     * @return a list of all cell values on the board, in row-major order.
     */
    public List<Integer> getAllCells() {
        List<Integer> cells = new ArrayList<>();
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            cells.add(getCellValue(cell_id));
        }
        return cells;
    }
    /**
     * Set the values of all cells on the board, given a list of cell values in row-major order (i.e., left to right, top to bottom).
     * @param values a list of cell values to set on the board, in row-major order.
     * @throws IllegalArgumentException if the number of cell values does not match the total number
     */
    public void setAllCells(List<Integer> values) {
        if (values.size() != total_cells) {
            throw new IllegalArgumentException("Invalid number of cell values: expected " + total_cells + ", got " + values.size());
        }
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            setCellValue(cell_id, values.get(cell_id - 1));
        }
    }


    /**
     * Get the size of the board.
     * @return number of rows/columns in the square board.
     */
    public int getSize() {
        return size;
    }
    /**
     * Get the total number of cells on the board.
     * @return total number of cells on the board.
     */
    public int getTotalCells() {
        return total_cells;
    }
    /**
     * Get the value that represents an empty cell on the board.
     * @return the value that represents an empty cell on the board.
     */
    public int getEmptyCellValue() {
        return empty_cell_value;
    }


    /**
     * Check if the given cell id is valid for this board.
     * @param cell_id the cell id to check.
     * @return true if the cell id is valid, false otherwise.
     */
    public boolean isValidCell(int cell_id){
        return cell_id >= 1 && cell_id <= total_cells;
    }
    /**
     * Check if the given cell position is valid for this board.
     * @param row the row of the cell to check.
     * @param col the column of the cell to check.
     * @return true if the cell position is valid, false otherwise.
     */
    public boolean isValidCell(int row, int col) {
        return row >= 1 && row <= size && col >= 1 && col <= size;
    }


    /**
     * Check if the cell with the given id is empty.
     * @param cell_id the cell id to check.
     * @return true if the cell is empty, false otherwise.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    public boolean isCellEmpty(int cell_id) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        return getCellValue(cell_id) == empty_cell_value;
    }
    /**
     * Check if the cell at the given position is empty.
     * @param row the row of the cell to check.
     * @param col the column of the cell to check.
     * @return true if the cell is empty, false otherwise.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    public boolean isCellEmpty(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        return getCellValue(row, col) == empty_cell_value;
    }

    /**
     * Get a list of cell ids that are currently empty on the board.
     * @return a list of cell ids that are currently empty on the board.
     */
    public List<Integer> getEmptyCells() {
        List<Integer> emptyCells = new ArrayList<>();
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (isCellEmpty(cell_id)) {
                emptyCells.add(cell_id);
            }
        }
        return emptyCells;
    }

    /**
     * Check if the board is empty (i.e., all cells are empty).
     * @return true if the board is empty, false otherwise.
     */
    public boolean isEmpty() {
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (!isCellEmpty(cell_id)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Check if the board is full (i.e., no empty cells).
     * @return true if the board is full, false otherwise.
     */
    public boolean isFull() {
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (isCellEmpty(cell_id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a string representation of the board, showing the value of each cell in a grid format.
     * @return a string representation of the board.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                sb.append("| ").append(getCellValue(row, col)).append(" ");
            }
            sb.append("|");
            if (row < size)
                sb.append("\n");
        }
        return sb.toString();
    }
}
