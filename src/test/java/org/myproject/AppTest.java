package org.myproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.constant.Message;
import org.myproject.io.TestIO;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class AppTest {

    private TestIO testIO;
    private String printedBoardFormat = "| %d | %d | %d |\n| %d | %d | %d |\n| %d | %d | %d |";

    @BeforeEach
    void setUp() {
        testIO = new TestIO();
        App.ioService = testIO; // Inject the TestIO into the App class
    }


    // -----------------------Startup Argument Validation Tests-----------------------
    @Test
    void testStartupWithoutArgument_Fails() {
        String[] args = {};
        boolean isValid = App.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS)); 
    }

    @Test
    void testStartupWithInvalidArgument_Fails() {
        String[] args = {"3"}; // Only 1 or 2 allowed
        boolean isValid = App.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    @Test
    void testStartupWithNonNumericArgument_Fails() {
        String[] args = {"abc"};
        boolean isValid = App.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    @Test
    void testStartupWithExtraArguments_Fails() {
        String[] args = {"1", "2"};
        boolean isValid = App.validateArgs(args, testIO);

        assertFalse(isValid);
        assertTrue(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }

    @Test
    void testStartupWithValidArguments_Passes() {
        String[] args = {"1"};
        boolean isValid = App.validateArgs(args, testIO);

        assertTrue(isValid);
        assertFalse(testIO.hasOutputsContaining(Message.INVALID_ARGS));
    }



    // -----------------------Output Tests-----------------------
    @Test
    void testStartupMessageAndOrder_HumanFirst() {
        String[] args = {"1"};
        App.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.WELCOME_MESSAGE, output.get(0));
        assertEquals(Message.getPlayersTurnMessage(1), output.get(2));
    }

    @Test
    void testStartupMessageAndOrder_BotFirst() {
        String[] args = {"2"};
        App.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.WELCOME_MESSAGE, output.get(0));
        assertEquals(Message.getPlayersTurnMessage(1), output.get(2));
    }

    @Test
    void testInitialBoardMapping() {
        String[] args = {"1"};
        App.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(String.format(printedBoardFormat, 0, 0, 0, 0, 0, 0, 0, 0, 0), output.get(1));
    }

    @Test
    void testDisplayQuitMessage() {
        testIO.addSimulatedInputs("q");
        String[] args = {"1"};
        App.main(args);

        List<String> output = testIO.getCapturedOutputs();
        assertEquals(Message.QUIT_MESSAGE, output.get(output.size() - 1));
    }



    // -----------------------Input Handling Tests-----------------------
    @Test
    void testHumanNonIntegerAndWhitespaceInput_Reprompts() {
        testIO.addSimulatedInputs("x", "", "6 X", "q");
        String[] args = {"1"};
        App.main(args);

        assertEquals(3, testIO.hasOutputsContainingWithCount(Message.getInvalidInputMessage(9)));
    }

    @Test
    void testHumanOutOfRangeIntegerInput_Reprompts() {
        testIO.addSimulatedInputs("10", "q");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.getInvalidInputMessage(9)));
    }

    @Test
    void testHumanOccupiedCellInput_PreventsOverwriting() {
        // Scenario: Human selects cell 1. Bot then naturally occupies cell 2 (first free cell strategy).
        // Human attempts to re-select cell 2. System must reject without shifting turns.
        testIO.addSimulatedInputs("1", "2", "q");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.CELL_ALREADY_OCCUPIED_MESSAGE));
        assertFalse(testIO.hasOutputsContaining(String.format(printedBoardFormat, 1, 1, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test
    void testHumanValidMove_UpdatesBoard() {
        testIO.addSimulatedInputs("5", "   6    ", "q");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(String.format(printedBoardFormat, 0, 0, 0, 0, 1, 0, 0, 0, 0)));
        assertTrue(testIO.hasOutputsContaining(String.format(printedBoardFormat, 2, 0, 0, 0, 1, 1, 0, 0, 0)));
    }

    @Test
    void testNoInputAccepted_AfterGameEnds() {
        testIO.addSimulatedInputs("1", "4", "7", "q", "2"); // Human wins
        String[] args = {"1"};
        App.main(args);

        assertTrue(Arrays.asList("q", "2").containsAll(testIO.getRemainingSimulatedInputs()));
    }



    // -----------------------Game Outcome Tests-----------------------
    @Test
    void testHumanWinDetection() {
        testIO.addSimulatedInputs("1", "4", "7");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.getWinnerMessage(1)));
    }

    @Test
    void testBotWinDetection() {
        testIO.addSimulatedInputs("4", "5", "7");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.getWinnerMessage(2)));
    }

    @Test
    void testDrawDetection() {
        // Forces a draw scenario by filling the board without any player winning.
        // O(1) X(2) O(3)
        // O(5) X(4) O(7)
        // X(6) O(9) X(8)
        testIO.addSimulatedInputs("2", "5", "7", "9");
        String[] args = {"2"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.DRAW_MESSAGE));
    }

    @Test
    void testGameQuitDetection() {
        testIO.addSimulatedInputs("q");
        String[] args = {"1"};
        App.main(args);

        assertTrue(testIO.hasOutputsContaining(Message.QUIT_MESSAGE));
    }
}