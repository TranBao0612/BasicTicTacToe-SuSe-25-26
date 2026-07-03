package org.myproject.common.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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

    private SquareBoard board3x3;

    /**
     * Set up a board before each test. Default 3x3 board setup for standardized tests.
     */
    @BeforeEach
    void setUp() {
        board3x3 = createBoard(3);
    }

    /**
     * Test the initialization of the board, ensuring that it has the 
     *      correct size, total cells, and that it is empty and not full.
     */
    @Test
    void testInitialization() {
        assertEquals(3, board3x3.getSize());
        assertEquals(9, board3x3.getTotalCells());
        assertTrue(board3x3.isEmpty());
        assertFalse(board3x3.isFull());
    }

    /**
     * Test setting and getting cell values by both cell ID and row/column.
     */
    @Test
    void testSetAndGetCellValue() {
        assertEquals(board3x3.empty_cell_value, board3x3.getCellValue(1));
        assertEquals(board3x3.empty_cell_value, board3x3.getCellValue(1, 2));

        board3x3.setCellValue(1, 1);
        assertEquals(1, board3x3.getCellValue(1));
        assertFalse(board3x3.isCellEmpty(1));

        board3x3.setCellValue(2, 2, 2);
        assertEquals(2, board3x3.getCellValue(2, 2));
        assertFalse(board3x3.isCellEmpty(2, 2));

        assertThrows(IllegalArgumentException.class, () -> board3x3.getCellValue(0));
        assertThrows(IllegalArgumentException.class, () -> board3x3.getCellValue(1, 4));
        assertThrows(IllegalArgumentException.class, () -> board3x3.setCellValue(0, 1));
        assertThrows(IllegalArgumentException.class, () -> board3x3.setCellValue(1, 4, 1));
    }

    /**
     * Test the validity of cell IDs within a 3x3 board.
     */
    @Test
    void testIsValidCell_ById() {
        assertTrue(board3x3.isValidCell(1));
        assertTrue(board3x3.isValidCell(9));
        assertFalse(board3x3.isValidCell(0));
        assertFalse(board3x3.isValidCell(10));
    }

    /**
     * Test the validity of row and column coordinates within a 3x3 board.
     */
    @Test
    void testIsValidCell_ByRowCol() {
        assertTrue(board3x3.isValidCell(1, 1));
        assertTrue(board3x3.isValidCell(3, 3));
        assertFalse(board3x3.isValidCell(0, 1));
        assertFalse(board3x3.isValidCell(1, 4));
    }

    /**
     * Test the isCellEmpty() method for both valid and invalid cell IDs and coordinates.
     */
    @Test
    void testIsCellEmpty_ValidAndInvalid() {
        // Initially empty
        assertTrue(board3x3.isCellEmpty(5));
        assertTrue(board3x3.isCellEmpty(2, 2));

        // Mutate cell
        board3x3.setCellValue(5, 1);
        assertFalse(board3x3.isCellEmpty(5));
        assertFalse(board3x3.isCellEmpty(2, 2));

        // Exception boundary tests
        assertThrows(IllegalArgumentException.class, () -> board3x3.isCellEmpty(0));
        assertThrows(IllegalArgumentException.class, () -> board3x3.isCellEmpty(4, 1));
    }

    /**
     * Test the getAllCells() and setAllCells() methods to ensure that the board can be 
     *      correctly populated and retrieved in row-major order.
     */
    @Test
    void testGetAllCellsAndSetAllCells() {
        List<Integer> newValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        board3x3.setAllCells(newValues);

        List<Integer> retrievedValues = board3x3.getAllCells();
        assertEquals(newValues, retrievedValues);
        
        // Assert specific coordinates to ensure correct row-major mapping
        assertEquals(1, board3x3.getCellValue(1, 1));
        assertEquals(5, board3x3.getCellValue(2, 2));
        assertEquals(9, board3x3.getCellValue(3, 3));
    }

    /**
     * Test that setAllCells() throws an exception when the provided list size does not match the total number of cells.
     */
    @Test
    void testSetAllCells_ThrowsExceptionOnSizeMismatch() {
        List<Integer> invalidSizeList = Arrays.asList(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> board3x3.setAllCells(invalidSizeList));
    }

    /**
     * Test the getEmptyCells() method to ensure it returns the correct list of empty cell IDs after some cells have been filled.
     */
    @Test
    void testGetEmptyCells() {
        assertEquals(9, board3x3.getEmptyCells().size());

        board3x3.setCellValue(1, 1); 
        board3x3.setCellValue(9, 2); 

        List<Integer> emptyCells = board3x3.getEmptyCells();
        assertEquals(7, emptyCells.size());
        assertFalse(emptyCells.contains(1));
        assertFalse(emptyCells.contains(9));
    }

    /**
     * Test the isEmpty() and isFull() methods to ensure they correctly reflect the state of the board.
     */
    @Test
    void testIsEmptyAndIsFull() {
        assertTrue(board3x3.isEmpty());
        assertFalse(board3x3.isFull());

        // Partially fill
        board3x3.setCellValue(1, 1);
        assertFalse(board3x3.isEmpty());
        assertFalse(board3x3.isFull());

        // Completely fill
        List<Integer> fullList = Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1);
        board3x3.setAllCells(fullList);
        assertFalse(board3x3.isEmpty());
        assertTrue(board3x3.isFull());
    }

    /**
     * Test the toString() method to ensure it returns the correct string representation of the board.
     */
    @Test
    void testToStringFormat() {
        String expectedFormat = "| %d | %d | %d |\n| %d | %d | %d |\n| %d | %d | %d |";
        int e = board3x3.getEmptyCellValue();
        String expected;
        
        expected = String.format(expectedFormat, e, e, e, e, e, e, e, e, e);
        assertEquals(expected, board3x3.toString());

        board3x3.setCellValue(1, 1);
        expected = String.format(expectedFormat, 1, e, e, e, e, e, e, e, e);
        assertEquals(expected, board3x3.toString());

        board3x3.setCellValue(5, 2);
        expected = String.format(expectedFormat, 1, e, e, e, 2, e, e, e, e);
        assertEquals(expected, board3x3.toString());

        board3x3.setAllCells(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        expected = String.format(expectedFormat, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(expected, board3x3.toString());
    }
}