package org.myproject.board;

import java.lang.Exception;
import java.util.ArrayList;


/**
 * This represent a square Tic-tac-toe board with n cells (cell id increase row by row from left to right)
 * The field cells is a 2D-array of Cell objects
 */
public class Board {
    private int size = 3;
    private Cell[][] cells = new Cell[size][size];

    public Board(int size) {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i*size + j + 1);
            }
        }
    }


    /**
     * To specify which cells is currently free
     * @return List of cell's id of free cells
     */
    public ArrayList<Integer> available_cells() {
        ArrayList<Integer> available_cell_list = new ArrayList<>();
        for(Cell[] row : cells) {
            for(Cell cell : row) {
                if (cell.is_available())
                    available_cell_list.add(cell.get_id());
                }
        }
        return available_cell_list;
    }


    /**
     * Check if the game is finished
     * @return -1 if not done, 0 if draw, player_id if anyone won
     * TODO: fix the logic for size expansion
     */
    public int is_done() {
        // Check column
        for(int j = 0; j < size; j++) {
            if (cells[0][j].get_state() != 0 &&
                cells[0][j].get_state() == cells[1][j].get_state() && cells[0][j].get_state() == cells[2][j].get_state())
                return cells[0][j].get_state();
        }
        // Check row
        for(int i = 0; i < size; i++) {
            if (cells[i][0].get_state() != 0 &&
                cells[i][0].get_state() == cells[i][1].get_state() && cells[i][0].get_state() == cells[i][2].get_state())
                return cells[i][0].get_state();
        }
        // Check diagonal
        int center_cell = cells[1][1].get_state();
        if(center_cell != 0) {
            if (cells[0][0].get_state() == center_cell && cells[2][2].get_state() == center_cell)
                return center_cell;
            if (cells[0][2].get_state() == center_cell && cells[2][0].get_state() == center_cell)
                return center_cell;
        }

        // Check draw
        if (available_cells().isEmpty())
            return 0;
        // If not done
        return -1;
    }


    /**
     * Update the cell's state as players occupying 
     * @param player_id
     * @param cell_id
     * @throws Exception throw an Exception if player check a cell that is not existed
     */
    public void player_choose(int player_id, int cell_id) throws Exception {
        if(cell_id < 1 || cell_id > size*size) {
            throw new Exception("Cell's id is out of range!");
        }
        cells[(cell_id-1) / size][(cell_id-1) % size].checked_by_player(player_id);
    }


    /**
     * To print out the board on Console follow with a blank line
     */
    public void print() {
        System.out.println(this.toString());
    }


    /**
     * Convert the board to string
     */
    @Override
    public String toString() {
        StringBuilder return_str = new StringBuilder();
        for(Cell[] row : cells) {
            for(Cell cell : row) {
                return_str.append("| " + cell.toString() + " ");
            }
            return_str.append("|\n");
        }
        return return_str.toString();
    }
}
