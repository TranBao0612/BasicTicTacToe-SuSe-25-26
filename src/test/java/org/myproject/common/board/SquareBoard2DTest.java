package org.myproject.common.board;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.board.SquareBoard2D;

/**
 * Unit tests for SquareBoard2D implementation. <br>
 * It extends the abstract SquareBoardTest to run all the generic tests on the 2D implementation. <br>
 * This class does not add any new tests, but ensures that SquareBoard2D passes all the tests defined in SquareBoardTest. <br>
 * If SquareBoard2D has any specific behavior / methods that are not inherited from parents 
 *      that needs testing, additional test methods can be added here.
 */
class SquareBoard2DTest extends SquareBoardTest {
    // Implement the abstract method to create a SquareBoard2D instance
    @Override
    protected SquareBoard createBoard(int size) {
        return new SquareBoard2D(size);
    }
}