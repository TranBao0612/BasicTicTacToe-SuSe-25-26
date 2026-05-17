package org.myproject.game;

import org.myproject.board.SquareBoard;

public final class TictactoeLogic {
    private TictactoeLogic() {
    }

    /**
     * Check if the move is valid, which means the cell is empty.
     * @param board the board to check
     * @param cell_id the cell id of the move to check
     * @return true if the move is valid (cell is empty), false otherwise
     */
    public static boolean is_valid_move(SquareBoard board, int cell_id) {
        return board.is_empty(cell_id);
    }

    /**
     * Check if the game is completed, which means there is a winner or the board is full (draw).
     * @param board the board to check
     * @return true if the game is completed, false otherwise
     */
    public static boolean is_completed(SquareBoard board) {
        return have_winner(board) || board.is_full();
    }

    /**
     * Check if there is a winner.
     * @param board the board to check
     * @return true if there is a winner, false otherwise
     */
    public static boolean have_winner(SquareBoard board) {
        return get_winner(board) != board.get_empty_cell_value();
    }

    /**
     * Get the id of the player who won.
     * @param board the board to check
     * @return the id of the player who won, or the value of an empty cell if there is no winner
     */
    public static int get_winner(SquareBoard board) {
        int no_winner_value = board.get_empty_cell_value();
        int winner_id = check_winner_by_first_diagonal(board);
        if(winner_id != no_winner_value)
            return winner_id;
        winner_id = check_winner_by_second_diagonal(board);
        if(winner_id != no_winner_value)
            return winner_id;
        for(int i = 1; i <= board.get_size(); i++) {
            winner_id = check_winner_by_row(board, i);
            if(winner_id != no_winner_value)
                return winner_id;
            winner_id = check_winner_by_column(board, i);
            if(winner_id != no_winner_value)
                return winner_id;
        }
        return no_winner_value;
    }


    // -----------------------Private methods-----------------------
    /**
     * Check if there is a winner by row.
     * @param board the board to check
     * @param row the row to check
     * @return the id of the player who won, or the value of an empty cell if there is no winner
     */
    private static int check_winner_by_row(SquareBoard board, int row) {
        if(board.is_empty(row, 1))
            return board.get_empty_cell_value();
        int first_value = board.get_value(row, 1);
        for(int i = 2; i <= board.get_size(); i++) {
            if(board.get_value(row, i) != first_value)
                return board.get_empty_cell_value();
        }
        return first_value;
    }

    /**
     * Check if there is a winner by column.
     * @param board the board to check
     * @param col the column to check
     * @return the id of the player who won, or the value of an empty cell if there is no winner
     */
    private static int check_winner_by_column(SquareBoard board, int col) {
        if(board.is_empty(1, col))
            return board.get_empty_cell_value();
        int first_value = board.get_value(1, col);
        for(int i = 2; i <= board.get_size(); i++) {
            if(board.get_value(i, col) != first_value)
                return board.get_empty_cell_value();
        }
        return first_value;
    }

    /**
     * Check if there is a winner by top-left to bottom-right diagonal.
     * @param board the board to check
     * @return the id of the player who won, or the value of an empty cell if there is no winner
     */
    private static int check_winner_by_first_diagonal(SquareBoard board) {
        if(board.is_empty(1, 1))
            return board.get_empty_cell_value();
        int first_value = board.get_value(1, 1);
        for(int i = 2; i <= board.get_size(); i++) {
            if(board.get_value(i, i) != first_value)
                return board.get_empty_cell_value();
        }
        return first_value;
    }
    /**
     * Check if there is a winner by top-right to bottom-left diagonal.
     * @param board the board to check
     * @return the id of the player who won, or the value of an empty cell if there is no winner
     */
    private static int check_winner_by_second_diagonal(SquareBoard board) {
        int size = board.get_size();
        if(board.is_empty(1, size))
            return board.get_empty_cell_value();
        int first_value = board.get_value(1, size);
        for(int i = 2; i <= size; i++) {
            if(board.get_value(i, size - i + 1) != first_value)
                return board.get_empty_cell_value();
        }
        return first_value;
    }
}
