package org.myproject.game;

import java.io.PrintStream;
import java.io.InputStream;

import org.myproject.board.*;
import org.myproject.player.*;


/**
 * Game Tictactoe with 2 players: user and bot. <br>
 *      User can choose to go first or second. <br>
 *      The game will end when there is a winner or a draw.
 */
public class Tictactoe {
    private PrintStream out;
    private InputStream in;

    private Player[] players = new Player[2];
    private int current_turn;
    private SquareBoard board;

    private boolean end_game = false;

    /**
     * Initializer of Tictactoe game and print the initial board
     * @param turn_of_user The turn of user, either 1 (goes first) or 2 (goes second). If no input, the default mode with user goes first will be used.
     */
    public Tictactoe(int turn_of_user, PrintStream out, InputStream in) {
        this.out = out;
        this.in = in;
        this.board = new SquareBoard1D(3, 0, out);

        initialize_players(turn_of_user);
        print_welcome_message();
        board.print();
    }
    public Tictactoe(int turn_of_user) {
        this(turn_of_user, System.out, System.in);
    }



    // ------------------------------ PUBLIC METHODS ------------------------------
    /**
     * Execute one turn of the game, which includes: <br>
     *     1. The current player makes a move. <br>
     *     2. Print the board after the move.
     */
    public void run_one_turn() {
        int cell_id = players[current_turn].make_decision(board);
        if (cell_id == -1) {
            end_game = true;
            return;
        }
        board.set_value(cell_id, players[current_turn].get_player_id());
        board.print();
    }



    /**
     * Execute the game loop, which includes: <br>
     *    1. Run one turn of the game. <br>
     *    2. Check if the game is completed. If not, switch turn and repeat step 1. <br>
     *    3. If the game is completed, print the result (winner or draw).
     */
    public void run_game_loop() {
        run_one_turn();
        while(!TictactoeLogic.is_completed(board) && !end_game) {
            switch_turn();
            run_one_turn();
        }
        if (end_game)
            this.out.println("Game ended by user.");
        else if(TictactoeLogic.have_winner(board)) 
            this.out.println("Player#" + TictactoeLogic.get_winner(board) + " won!");
        else
            this.out.println("It is a draw!");
    }



    // ------------------------------ PRIVATE METHODS -----------------------------    
    private void initialize_players(int turn_of_user) {
        players[0] = new User(1, this.out, this.in);
        players[1] = new Bot(2, this.out);
        current_turn = turn_of_user - 1;
    }

    private void switch_turn() {
        current_turn = (current_turn + 1) % players.length;
    }

    private void print_welcome_message() {
        this.out.println("Hello!");
    }
}
