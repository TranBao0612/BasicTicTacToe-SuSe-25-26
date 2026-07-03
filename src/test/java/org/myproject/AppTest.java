package org.myproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.common.constant.Message;
import org.myproject.common.io.TestIO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class AppTest {
    // Missing: TS-018, TS0-19, TS-021 -> TS-024

    private TestIO testIO;
    private String printedBoardFormat = "| %d | %d | %d |\n| %d | %d | %d |\n| %d | %d | %d |";

    @BeforeEach
    void setUp() {
        testIO = new TestIO();
        ConsoleApp.ioService = testIO; // Inject the TestIO into the App class
    }


    // -----------------------Startup Argument Validation Tests-----------------------
    /**
     * TS-003
     */
    @Test
    void testStartupWithoutArgument_Fails() {
        String[] args = {};
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS)); 
    }

    /**
     * TS-004
     */
    @Test
    void testStartupWithInvalidArgument_Fails() {
        String[] args = {"3"}; // Only 1 or 2 allowed
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    /**
     * TS-004
     */
    @Test
    void testStartupWithNonNumericArgument_Fails() {
        String[] args = {"abc"};
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    /**
     * TS-025
     */
    @Test
    void testStartupWithExtraArguments_Fails() {
        String[] args = {"1", "2"};
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    /**
     * TS-005
     */
    @Test
    void testStartupWithHumanGoesFirst_Passes() {
        String[] args = {"1"};
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertTrue(isValid);
        assertFalse(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    /**
     * TS-005
     */
    @Test
    void testStartupWithBotGoesFirst_Passes() {
        String[] args = {"2"};
        boolean isValid = ConsoleApp.validateArgs(args, testIO);

        assertTrue(isValid);
        assertFalse(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }



    // -----------------------Output Tests-----------------------
    /**
     * TS-001
     */
    @Test
    void testStartupMessageAndOrder_HumanFirst() {
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.WELCOME_MESSAGE, output.get(0));
        assertEquals(String.format(printedBoardFormat, 0, 0, 0, 0, 0, 0, 0, 0, 0), output.get(1));
        assertEquals(Message.getPlayersTurnMessage(1, true), output.get(2));
    }

    /**
     * TS-002
     */
    @Test
    void testStartupMessageAndOrder_BotFirst() {
        String[] args = {"2"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.WELCOME_MESSAGE, output.get(0));
        assertEquals(String.format(printedBoardFormat, 0, 0, 0, 0, 0, 0, 0, 0, 0), output.get(1));
        assertEquals(Message.getPlayersTurnMessage(1), output.get(2));
    }

    /**
     * TS-006
     */
    @Test
    void testInitialBoardMapping() {
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(String.format(printedBoardFormat, 0, 0, 0, 0, 0, 0, 0, 0, 0), output.get(1));
    }

    /**
     * TS-009
     */
    @Test
    void testDisplayQuitMessage_Lowercase() {
        testIO.addSimulatedInputs("q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.QUIT_MESSAGE, output.get(output.size() - 1));
    }

    /**
     * TS-010
     */
    @Test
    void testDisplayQuitMessage_Uppercase() {
        testIO.addSimulatedInputs("Q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.QUIT_MESSAGE, output.get(output.size() - 1));
    }

    /**
     * TS-010, TS-020, TS-026
     */
    @Test
    void testDisplayQuitMessage_Whitespace() {
        testIO.addSimulatedInputs("     q   ");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.QUIT_MESSAGE, output.get(output.size() - 1));
    }



    // -----------------------Input Handling Tests-----------------------
    /**
     * TS-008
     */
    @Test
    void testHumanNonIntegerAndWhitespaceInput_Reprompts() {
        testIO.addSimulatedInputs("x", "", "6 X", "q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        assertEquals(3, testIO.hasOutputsContainingWithCount(Message.getInvalidInputMessage(9)));
    }

    /**
     * TS-011
     */
    @Test
    void testHumanOutOfRangeIntegerInput_Reprompts() {
        testIO.addSimulatedInputs("10", "q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.getInvalidInputMessage(9)));
        assertEquals(2, testIO.hasOutputsContainingWithCount(Message.getPlayersTurnMessage(1)));
    }

    /**
     * TS-012
     */
    @Test
    void testHumanOccupiedCellInput_PreventsOverwriting() {
        // Scenario: Human selects cell 1. Bot then naturally occupies cell 2 (first free cell strategy).
        // Human attempts to re-select cell 2. System must reject without shifting turns.
        testIO.addSimulatedInputs("1", "2", "q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.CELL_ALREADY_OCCUPIED_MESSAGE));
        assertFalse(testIO.hasOutputsContaining(String.format(printedBoardFormat, 1, 1, 0, 0, 0, 0, 0, 0, 0)));
    }

    /**
     * TS-007, TS-026
     */
    @Test
    void testHumanValidMove_UpdatesBoard() {
        testIO.addSimulatedInputs("5", "   6    ", "q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        assertTrue(testIO.hasOutputsContaining(String.format(printedBoardFormat, 0, 0, 0, 0, 1, 0, 0, 0, 0)));
        assertTrue(testIO.hasOutputsContaining(String.format(printedBoardFormat, 2, 0, 0, 0, 1, 1, 0, 0, 0)));
    }

    /**
     * TS-009
     */
    @Test
    void testNoInputAccepted_AfterGameEnds() {
        testIO.addSimulatedInputs("1", "4", "7", "q", "2"); // Human wins
        String[] args = {"1"};
        ConsoleApp.main(args);

        assertTrue(Arrays.asList("q", "2").containsAll(testIO.getRemainingSimulatedInputs()));
    }



    // -----------------------Game Outcome Tests-----------------------
    /**
     * TS-013, TS-020
     */
    @Test
    void testHumanWinDetection() {
        testIO.addSimulatedInputs("1", "4", "7");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.getWinnerMessage(1), output.get(output.size() - 1));
    }

    /**
     * TS-014, TS-020
     */
    @Test
    void testBotWinDetection() {
        testIO.addSimulatedInputs("4", "5", "7");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.getWinnerMessage(2), output.get(output.size() - 1));
    }

    /**
     * TS-015, TS-016, TS-020
     */
    @Test
    void testDrawDetection() {
        // Forces a draw scenario by filling the board without any player winning.
        // O(1) X(2) O(3)
        // O(5) X(4) O(7)
        // X(6) O(9) X(8)
        testIO.addSimulatedInputs("2", "5", "7", "9");
        String[] args = {"2"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.DRAW_MESSAGE, output.get(output.size() - 1));
    }

    /**
     * TS-010, TS-020
     */
    @Test
    void testGameQuitDetection() {
        testIO.addSimulatedInputs("q");
        String[] args = {"1"};
        ConsoleApp.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.QUIT_MESSAGE, output.get(output.size() - 1));
    }
}