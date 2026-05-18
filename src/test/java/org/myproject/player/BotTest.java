package org.myproject.player;

import org.junit.jupiter.api.Test;
import org.myproject.board.*;
import org.myproject.io.TestIO;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the Bot class. <br>
 * The tests focus on ensuring that the bot's decision-making logic is correct 
 *      and that it selects the appropriate cell based on the current state of the board.
 */
class BotTest {

    // This test check the correctness of the bot's decision-making logic.
    @Test
    void botShouldPickFirstAvailableCell() {
        SquareBoard board = new SquareBoard1D(3);
        Bot bot = new Bot(2, new TestIO());

        // Initially, all cells are empty, so Bot should pick cell 1
        int decision = Integer.parseInt(bot.makeDecision(board));
        assertEquals(1, decision);

        // Place a value at cell 1, 2 and 4 to simulate some moves
        board.setCellValue(1, 1);
        board.setCellValue(2, 1);
        board.setCellValue(4, 1);

        // Bot should pick next available cell (which is 3)
        decision = Integer.parseInt(bot.makeDecision(board));
        assertEquals(3, decision);
    }
}