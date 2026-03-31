package org.myproject.game;

import org.myproject.board.Board;
import org.myproject.player.*;


/**
 * Game Tictactoe with 2 players: user and bot. <br>
 *      User can choose to go first or second. <br>
 *      The game will end when there is a winner or a draw.
 */
public class Tictactoe {
    private Player player_go_first;
    private Player player_go_second;
    private Board board = new Board(3);

    /**
     * Initializer of Tictactoe game and print the initial board
     * @param turn_of_user
     */
    public Tictactoe(int turn_of_user) {
        initialize_players(turn_of_user);
        board.print();
    }


    /**
     * Private helper method: game loop
     */
    public void game_start() {
        int turn = 1;
        while(board.is_done() == -1) {
            if (turn == 1) {
                player_go_first.move(board);
                turn = 2;
            } else {
                player_go_second.move(board);
                turn = 1;
            }
            board.print();
        }
        if (board.is_done() == 0)
            System.out.println("DRAW!");
        else 
            System.out.printf("The winner is player %d!", board.is_done());
    }



    /**
     * Private helper method: Assign type to player go first and second. <br>
     * Default type is User go first and Bot go second.
     */
    private void assign_player_type(String type1, String type2) {
        this.player_go_first = type1 == "Bot"? new Bot() : new User();
        this.player_go_second = type2 == "Bot"? new Bot() : new User();
    }

    /**
     * Private helper method: Initialize players
     */
    private void initialize_players(int turn_of_user) {
            System.out.println("User vs Bot Mode");
            if (turn_of_user == 1) 
                assign_player_type(null, "Bot");
            else if (turn_of_user == 2) 
                    assign_player_type("Bot", null);
            else {
                System.out.println("Invalid input! Turn of user should be 1 or 2. The default mode with user goes 1st is running.");
                assign_player_type(null, "Bot");
            }
    }
}
