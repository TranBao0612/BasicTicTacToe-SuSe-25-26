package org.myproject.common.game;

import org.myproject.common.board.*;
import org.myproject.common.constant.*;
import org.myproject.common.io.IOService;
import org.myproject.common.player.*;


/**
 * Game Tictactoe with 2 players: user and bot. <br>
 *      User can choose to go first or second. <br>
 *      The game will end when there is a winner or a draw.
 */
public class Tictactoe {
    /**
     * Players of the game. Can have more than 2 players in the future if we want to make it more complex.
     */
    private Player[] players = new Player[2];
    private int current_turn;
    private SquareBoard board;
    private boolean end_game = false;
    private IOService ioService;



    /**
     * Create a Tic-tac-toe game instance
     * @param turn_of_human_user turn of the human user (1 or 2)
     * @param ioService the IO Service for input/output
     */
    public Tictactoe(int turn_of_human_user, IOService ioService) {
        this.board = new SquareBoard1D(3);
        this.ioService = ioService;
        initializePlayers(turn_of_human_user);
    }



    // ------------------------------ PUBLIC METHODS ------------------------------
    /**
     * Start the game and keep running until the game ends.
     */
    public void startGame() {
        ioService.println(Message.WELCOME_MESSAGE);
        ioService.println(board.toString());
        while (!end_game) {
            runOneTurn();
        }
    }

    /**
     * Execute one turn of the game, include:
     *      Get the decision of the current player, 
     *      update and print the board, check for a winner, 
     *      and switch turn if the game has not ended.
     */
    public void runOneTurn() {
        ioService.println(Message.getPlayersTurnMessage(players[current_turn].getPlayerId(), players[current_turn] instanceof User));
        int decision = players[current_turn].makeDecision(board);
        if (players[current_turn] instanceof Bot) {
            ioService.println(String.valueOf(decision));
        }
        // If the user quit the game, end the game and return.
        if (decision == -1) {
            end_game = true;
            return;
        }
        // Update the board with the decision of the current player.
        board.setCellValue(decision, players[current_turn].getPlayerId());
        ioService.println(board.toString());
        // Check if there is a winner or a draw.
        int winner_id = TictactoeWinner.getWinner(board);
        if (winner_id != -1) {
            ioService.println(Message.getWinnerMessage(winner_id));
            end_game = true;
        } else if (board.isFull()) {
            ioService.println(Message.DRAW_MESSAGE);
            end_game = true;
        } else {
            switchTurn();
        }
    }

    
    // ------------------------------ PRIVATE METHODS -----------------------------    
    private void initializePlayers(int turn_of_human_user) {
        players[0] = new User(turn_of_human_user, ioService);
        players[1] = new Bot(3 - turn_of_human_user);
        current_turn = turn_of_human_user - 1;
    }

    private void switchTurn() {
        current_turn = (current_turn + 1) % players.length;
    }


}
