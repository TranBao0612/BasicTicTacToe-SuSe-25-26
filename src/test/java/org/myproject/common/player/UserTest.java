package org.myproject.common.player;

import org.myproject.common.io.IOService;
import org.myproject.common.player.Player;
import org.myproject.common.player.User;


/**
 * Unit tests for the User class. <br>
 *      User class is just to store player's and get console input from the user. <br>
 *      So currently there is no logic to test in the User class.
 */
class UserTest extends PlayerTest {
    @Override
    protected Player createPlayer(int playerId, IOService ioService) {
        return new User(playerId, ioService);
    }
}