package org.myproject.common.player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.myproject.common.board.*;
import org.myproject.common.io.IOService;

/**
 * Unit tests for the Bot class. <br>
 * The tests focus on ensuring that the bot's decision-making logic is correct 
 *      and that it selects the appropriate cell based on the current state of the board.
 */
class BotTest extends PlayerTest {
    @Override
    protected Player createPlayer(int playerId, IOService ioService) {
        return new Bot(playerId, ioService);
    }

    /**
     * TS-017
     */
    @Test
    void testComputerMoveStrategy() {
        SquareBoard board = new SquareBoard1D(3);
        Bot bot = new Bot(2);

        // Initially, all cells are empty, so Bot should pick cell 1
        Integer decision = bot.makeDecision(board);
        assertEquals(1, decision);

        // Place a value at cell 1, 2 and 4 to simulate some moves
        board.setCellValue(1, 1);
        board.setCellValue(2, 1);
        board.setCellValue(4, 1);

        // Bot should pick next available cell (which is 3)
        decision = bot.makeDecision(board);
        assertEquals(3, decision);
    }
}