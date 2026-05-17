package org.myproject.player;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.myproject.board.SquareBoard;
import org.myproject.game.TictactoeLogic;


/**
 * Class represent a User (human player) with a player_id and method to input their decision
 */
public class User extends Player {
    /**
     * Scanner to get user input
     */
    private Scanner user_choice;
    /**
     * Error message for invalid input, which is not an integer or not a number between 1 and 9
     */
    private String err_invalid_input = "Please, input a valid number [1-9]";
    /**
     * Error message for occupied cell, which is already occupied by either player
     */
    private String err_occupied_cell = "The cell is occupied!";
    /**
     * Regex pattern for valid input, which is either an integer between 1 and 9, or "q" to quit the game
     */
    private String valid_input_regex = "^[1-9]$|^q$";

    /**
     * Initializer of User with player_id and input stream, which is used to get user input. The default input stream is System.in.
     * @param id player id of the user, either 1 or 2
     * @param input_stream input stream to get user input, if no input, the default input stream System.in will be used
     */
    public User(int id, PrintStream printStream, InputStream input_stream) {
        super(id, printStream);
        this.user_choice = new Scanner(input_stream);
    }
    public User(int id) {
        this(id, System.out, System.in);
    }


    /**
     * Let user enter the cell to be checked
     * @param board the board of current game state, used to validate user input
     * @return cell id of the cell on tic-tac-toe board, or -1 if the user wants to quit the game
     */
    public int make_decision(SquareBoard board) {
        while (true) {
            print_stream.print(decision_console_string);
            String user_input = get_input();
            if (user_input.equals("q")) {
                return -1;
            }
            int input = Integer.parseInt(user_input);
            if (board.is_valid_cell_id(input) && TictactoeLogic.is_valid_move(board, input)) {
                return input;
            } else if (!board.is_valid_cell_id(input)) {
                print_stream.print(err_invalid_input);
            } else {
                print_stream.print(err_occupied_cell);
            }
        }
    }


    /**
     * Prompt user input until the input is a valid integer between 1 and 9, or "q" to quit the game.
     * @return the valid user input
     */
    private String get_input() {
        while(true) {
            String user_input = user_choice.nextLine().trim();
            if (user_input.matches(valid_input_regex)) {
                return user_input;
            } else {
                print_stream.print(err_invalid_input);
            }
        }
    }
}
