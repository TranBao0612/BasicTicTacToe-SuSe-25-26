package org.myproject.common.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.common.board.*;
import org.myproject.common.game.TictactoeWinner;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TictactoeWinnerTest {

    private SquareBoard board;

    @BeforeEach
    void setUp() {
        board = new SquareBoard1D(3);
    }

    @Test
    void testGetWinner_EmptyBoard() {
        assertEquals(-1, TictactoeWinner.getWinner(board));
    }

    /**
     * TS-015
     */
    @Test
    void testGetWinner_RowWin() {
        board.setCellValue(2, 1, 1);
        board.setCellValue(2, 2, 1);
        board.setCellValue(2, 3, 1);

        assertEquals(1, TictactoeWinner.getWinner(board));
    }

    /**
     * TS-015
     */
    @Test
    void testGetWinner_ColumnWin() {
        board.setCellValue(1, 3, 2);
        board.setCellValue(2, 3, 2);
        board.setCellValue(3, 3, 2);

        assertEquals(2, TictactoeWinner.getWinner(board));
    }

    /**
     * TS-015
     */
    @Test
    void testGetWinner_FirstDiagonalWin() {
        board.setCellValue(1, 1, 1);
        board.setCellValue(2, 2, 1);
        board.setCellValue(3, 3, 1);

        assertEquals(1, TictactoeWinner.getWinner(board));
    }

    /**
     * TS-015
     */
    @Test
    void testGetWinner_SecondDiagonalWin() {
        board.setCellValue(1, 3, 2);
        board.setCellValue(2, 2, 2);
        board.setCellValue(3, 1, 2);

        assertEquals(2, TictactoeWinner.getWinner(board));
    }

    /**
     * TS-016
     */
    @Test
    void testGetWinner_Draw() {
        // X O X
        // X O O
        // O X X
        board.setAllCells(Arrays.asList(
            1, 2, 1,
            1, 2, 2,
            2, 1, 1
        ));

        assertEquals(-1, TictactoeWinner.getWinner(board));
    }
}