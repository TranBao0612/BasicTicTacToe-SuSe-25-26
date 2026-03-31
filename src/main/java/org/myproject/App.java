package org.myproject;

import org.myproject.game.Tictactoe;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        int start_player;

        
        if(args.length == 1) {
            try{
                start_player = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                start_player = -1; // To flag invalid input, will be handled in Tictactoe constructor
            }
        } else if(args.length == 0) {
            System.out.println("No argument! The default mode with user goes 1st is running.");
            start_player = 1;
        } else {
            System.out.println("Too many arguments! Please only specify the turn of user (1 or 2).");
            return;
        }


        Tictactoe tic_tac_toe_game = new Tictactoe(start_player);
        tic_tac_toe_game.game_start();
        
    }
}
