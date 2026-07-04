package org.myproject.common.board;

/**
 * Unit tests for SquareBoard1D implementation. <br>
 * It extends the abstract SquareBoardTest to run all the generic tests on the 1D implementation. <br>
 * This class does not add any new tests, but ensures that SquareBoard1D passes all the tests defined in SquareBoardTest. <br>
 * If SquareBoard1D has any specific behavior / methods that are not inherited from parents 
 *      that needs testing, additional test methods can be added here.
 */
class SquareBoard1DTest extends SquareBoardTest {
    // Implement the abstract method to create a SquareBoard1D instance
    @Override
    protected SquareBoard createBoard(int size) {
        return new SquareBoard1D(size);
    }
}