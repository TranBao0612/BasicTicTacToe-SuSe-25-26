package org.myproject.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.constant.Message;
import org.myproject.io.TestIO;

import static org.junit.jupiter.api.Assertions.*;

public class TictactoeTest {

    private TestIO testIO;

    @BeforeEach
    void setUp() {
        testIO = new TestIO();
    }

    @Test
    void testRunOneTurn_UserQuitsImmediately() {
        // Arrange: Human player goes first (turn 1).
        // When User.makeDecision calls nextLine(), it will read "q"
        testIO.addSimulatedInputs("q");
        
        Tictactoe game = new Tictactoe(1, testIO);

        // Verify that the game printed the player turn message before quitting
        game.runOneTurn();
        boolean turnMessagePrinted = testIO.getCapturedOutputs().stream()
                .anyMatch(line -> line.contains(Message.getPlayersTurnMessage(1)));
        
        assertTrue(turnMessagePrinted, "Should print turn message before asking for input.");
    }
}