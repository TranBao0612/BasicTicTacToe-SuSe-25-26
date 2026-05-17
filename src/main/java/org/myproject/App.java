package org.myproject;

import org.myproject.game.Tictactoe;


public class App {
    public static void main(String[] args) {
        int turn_of_user;
        String invalid_arg = "Please, input a valid option [1-2]";

        if(args.length == 1) {
            try{
                turn_of_user = Integer.parseInt(args[0]);
                if(turn_of_user != 1 && turn_of_user != 2) {
                    System.out.println(invalid_arg);
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(invalid_arg);
                return;
            }
        } else {
            System.out.println(invalid_arg);
            return;
        }


        Tictactoe tic_tac_toe_game = new Tictactoe(turn_of_user);
        tic_tac_toe_game.run_game_loop();
    }
}
