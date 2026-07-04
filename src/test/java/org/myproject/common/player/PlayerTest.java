package org.myproject.common.player;

import org.junit.jupiter.api.Test;
import org.myproject.common.io.IOService;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PlayerTest {
    // Factory method to create a board instance for testing
    protected abstract Player createPlayer(int playerId, IOService ioService);
    protected Player createPlayer(int playerId) {
        return createPlayer(playerId, null);
    }

    @Test
    void testGetPlayerId() {
        Player player = createPlayer(1);
        assertEquals(1, player.getPlayerId());

        player = createPlayer(20);
        assertEquals(20, player.getPlayerId());
    }

    
}
