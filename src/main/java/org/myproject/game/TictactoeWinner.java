package org.myproject.game;

import org.myproject.board.SquareBoard;

public final class TictactoeWinner {
    private TictactoeWinner() {}

    /**
     * Get the winner of the game by checking all rows, columns and diagonals.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    public static final int getWinner(SquareBoard board) {
        if(board.isEmpty())
            return -1;
        int winner_id;
        // Check diagonals first
        winner_id = checkWinnerByFirstDiagonal(board);
        if(winner_id != -1)
            return winner_id;
        winner_id = checkWinnerBySecondDiagonal(board);
        if(winner_id != -1)
            return winner_id;
        // Check rows and columns
        for(int i = 1; i <= board.getSize(); i++) {
            winner_id = checkWinnerByRow(board, i);
            if(winner_id != -1)
                return winner_id;
            winner_id = checkWinnerByColumn(board, i);
            if(winner_id != -1)
                return winner_id;
        }
        // No winner found
        return -1;
    }


    // -----------------------Private methods-----------------------
    /**
     * Check if there is a winner by row.
     * @param board the current state of the board
     * @param row the index of the row to check
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByRow(SquareBoard board, int row) {
        if(board.isCellEmpty(row, 1))
            return -1;
        int first_value = board.getCellValue(row, 1);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(row, i) != first_value)
                return -1;
        }
        return first_value;
    }

    /**
     * Check if there is a winner by column.
     * @param board the current state of the board
     * @param col the index of the column to check
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByColumn(SquareBoard board, int col) {
        if(board.isCellEmpty(1, col))
            return -1;
        int first_value = board.getCellValue(1, col);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(i, col) != first_value)
                return -1;
        }
        return first_value;
    }

    /**
     * Check if there is a winner by top-left to bottom-right diagonal.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByFirstDiagonal(SquareBoard board) {
        if(board.isCellEmpty(1, 1))
            return -1;
        int first_value = board.getCellValue(1, 1);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(i, i) != first_value)
                return -1;
        }
        return first_value;
    }
    /**
     * Check if there is a winner by top-right to bottom-left diagonal.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerBySecondDiagonal(SquareBoard board) {
        int size = board.getSize();
        if(board.isCellEmpty(1, size))
            return -1;
        int first_value = board.getCellValue(1, size);
        for(int i = 2; i <= size; i++) {
            if(board.getCellValue(i, size - i + 1) != first_value)
                return -1;
        }
        return first_value;
    }
}
