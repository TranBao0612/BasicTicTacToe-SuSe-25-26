package org.myproject.board;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SquareBoard implementations. <br>
 * Abstract test class for any implementation of SquareBoard. <br>
 * Subclasses should override createBoard() to provide an instance.
 */
public abstract class SquareBoardTest {
    // Factory method to create a board instance for testing
    protected abstract SquareBoard createBoard(int size);

    // Test that the board initializes correctly with the specified size and all cells are empty
    @Test
    void testEmptyBoardInitialization() {
        int size = 3;
        SquareBoard board = createBoard(size);
        // All cells should be empty
        assertTrue(board.isEmpty());
    }

    // Test setting and getting values by cell ID
    @Test
    void testSetAndGetById() {
        SquareBoard board = createBoard(3);
        board.setCellValue(1, 2);
        assertEquals(2, board.getCellValue(1));
        assertFalse(board.isCellEmpty(1));
    }

    // Test setting and getting values by row and column
    @Test
    void testSetAndGetByRowCol() {
        SquareBoard board = createBoard(3);
        board.setCellValue(2, 2, 5);
        assertEquals(5, board.getCellValue(2, 2));
        assertFalse(board.isCellEmpty(2, 2));
    }

    // Test get_empty_cells() returns correct list of empty cell IDs
    @Test
    void testGetEmptyCells() {
        SquareBoard board = createBoard(2);
        board.setCellValue(1, 2, 1);
        List<Integer> emptyCells = board.getEmptyCells();
        assertEquals(3, emptyCells.size());
        assertFalse(emptyCells.contains(2));
    }

    // Test is_full() returns true when all cells are filled and false otherwise
    @Test
    void testIsFull() {
        SquareBoard board = createBoard(2);
        assertFalse(board.isFull());
        // Fill the board
        for (int i = 1; i <= 4; i++) {
            board.setCellValue(i, 1);
        }
        assertTrue(board.isFull());
    }

    // Test is_valid_cell_id() returns true for valid cell IDs / row & col access and false for invalid ones
    @Test
    void testValidCellChecks() {
        SquareBoard board = createBoard(3);
        assertTrue(board.isValidCell(1));
        assertFalse(board.isValidCell(0));
        assertFalse(board.isValidCell(10));
        assertTrue(board.isValidCell(3, 3));
        assertFalse(board.isValidCell(4, 2));
    }

    // Test to_string() returns the correct string representation of the board
    @Test
    void testToString() {
        SquareBoard board = createBoard(2);
        board.setCellValue(1, 1, 1);
        String expected = "| 1 | 0 |\n| 0 | 0 |";
        assertEquals(expected, board.toString());
    }
}