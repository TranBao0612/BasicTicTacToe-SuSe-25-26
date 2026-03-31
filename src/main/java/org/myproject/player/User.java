package org.myproject.player;

import java.util.Scanner;
import org.myproject.board.Board;


/**
 * Class represent a User (human player) with a player_id and method to input their decision
 */
public class User extends Player {
    Scanner user_choice = new Scanner(System.in);

    public User() {
        super(1);
    }


    /**
     * Bot (computer) move with basic strategy: choose 1st available cell
     * @param board
     */
    public void move(Board board) {
        try {
            board.player_choose(player_id, make_decision(board));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            move(board);
        }
    }


    /**
     * Let user enter the cell to be checked
     * @param board
     * @return cell id of the cell on tic-tac-toe board
     */
    protected int make_decision(Board board) {
        int cell_id = -1; // IDE mistakenly state this as error if not define
        System.out.printf("Player %d choose: ", player_id);
        boolean valid_input = false;
        while(!valid_input) {
            String move = user_choice.nextLine();  // To prevent case like "12 aaaaa " (Wrong format)
            try {
                cell_id = Integer.parseInt(move); 
                valid_input = true;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Please try again: ");
            }
        }
        return cell_id;
    }
}
