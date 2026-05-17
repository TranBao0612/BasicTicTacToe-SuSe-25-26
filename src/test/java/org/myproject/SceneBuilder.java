package org.myproject;

import org.myproject.board.*;

public final class SceneBuilder {

    /**
     * Test helper method to build a string representation of the board from a given board state
     * @param boardState the state of the board represented as a 1D array, where each element represents the value of the corresponding position on the board
     * @return a string representation of the board built from the given board state
     */
     public static String buildBoardString(int[] boardState) {
        StringBuilder sb = new StringBuilder();
        int size = (int) Math.sqrt(boardState.length);
        for (int i = 0; i < boardState.length; i++) {
            sb.append("| ").append(boardState[i]).append(" ");
            if ((i + 1) % size == 0)
                sb.append("|\n");
        }
        return sb.toString();
     }

    /**
     * Test helper method to build a board scene from a given board state and board type
     * @param boardState the state of the board represented as a 1D array, where each element represents the value of the corresponding position on the board
     * @param boardTypeIs1D a boolean flag to indicate whether the board type is 1D or 2D
     * @return a SquareBoard object representing the board scene built from the given board state and board type
     */
    public static SquareBoard buildBoardScene(int[] boardState, boolean boardTypeIs1D) {
        SquareBoard board;
        int size = (int) Math.sqrt(boardState.length);
        if (boardTypeIs1D) 
            board = new SquareBoard1D(size);
        else
            board = new SquareBoard2D(size);
        for (int i = 1; i <= boardState.length; i++) {
            board.set_value(i, boardState[i - 1]);
        }
        return board;
    }
}
