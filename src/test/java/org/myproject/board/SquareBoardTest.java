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
    protected abstract SquareBoard createBoard(int size, int emptyCellValue);

    // Test to see if EMPTY_CELL_VALUE is set correctly and all cells are initialized as empty
    @Test
    void testEmptyBoardInitialization() {
        int size = 3;
        int emptyValue = 1;
        SquareBoard board = createBoard(size, emptyValue);
        assertEquals(emptyValue, board.get_empty_cell_value(), "Empty cell value should be set correctly");
        // All cells should be empty
        for (int i = 1; i <= size*size; i++) {
            assertTrue(board.is_empty(i), "Cell " + i + " should be empty");
            assertEquals(emptyValue, board.get_value(i), "Cell empty value is incorrect.");
            int row = (i - 1) / size + 1;
            int col = (i - 1) % size + 1;
            assertTrue(board.is_empty(row, col), "Cell (" + row + "," + col + ") should be empty");
            assertEquals(emptyValue, board.get_value(row, col), "Cell empty value is incorrect.");
        }
    }

    // Test setting and getting values by cell ID
    @Test
    void testSetAndGetById() {
        SquareBoard board = createBoard(3, 0);
        board.set_value(1, 2);
        assertEquals(2, board.get_value(1));
        assertFalse(board.is_empty(1));
    }

    // Test setting and getting values by row and column
    @Test
    void testSetAndGetByRowCol() {
        SquareBoard board = createBoard(3, 0);
        board.set_value(2, 2, 5);
        assertEquals(5, board.get_value(2, 2));
        assertFalse(board.is_empty(2, 2));
    }

    // Test get_emopty_cells() returns correct list of empty cell IDs
    @Test
    void testGetEmptyCells() {
        SquareBoard board = createBoard(2, 0);
        board.set_value(1, 2, 1);
        List<Integer> emptyCells = board.get_empty_cells();
        assertEquals(3, emptyCells.size());
        assertFalse(emptyCells.contains(2));
    }

    // Test is_full() returns true when all cells are filled and false otherwise
    @Test
    void testIsFull() {
        SquareBoard board = createBoard(2, 0);
        assertFalse(board.is_full());
        // Fill the board
        for (int i = 1; i <= 4; i++) {
            board.set_value(i, 1);
        }
        assertTrue(board.is_full());
    }

    // Test set_empty_value() updates the empty cell value and all empty cells correctly and is_empty() reflects the new empty value
    @Test
    void testSetEmptyValueUpdatesBoard() {
        SquareBoard board = createBoard(2, 0);
        board.set_empty_value(9);
        assertEquals(9, board.get_empty_cell_value());
        for (int i = 1; i <= 4; i++) {
            assertTrue(board.is_empty(i));
            assertEquals(9, board.get_value(i));
        }
    }

    // Test is_valid_cell_id() returns true for valid cell IDs / row & col access and false for invalid ones
    @Test
    void testValidCellChecks() {
        SquareBoard board = createBoard(3, 0);
        assertTrue(board.is_valid_cell_id(1));
        assertFalse(board.is_valid_cell_id(0));
        assertFalse(board.is_valid_cell_id(10));
        assertTrue(board.is_valid_cell_id(3, 3));
        assertFalse(board.is_valid_cell_id(4, 2));
    }

    // Test to_string() returns the correct string representation of the board
    @Test
    void testToString() {
        SquareBoard board = createBoard(2, 0);
        board.set_value(1, 1, 1);
        String expected = "| 1 | 0 |\n| 0 | 0 |\n";
        assertEquals(expected, board.to_string());
    }
}