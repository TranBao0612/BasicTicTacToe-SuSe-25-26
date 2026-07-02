package org.myproject;

import org.myproject.constant.Message;
import org.myproject.game.Tictactoe;
import org.myproject.io.*;


public class App {
    public static IOService ioService = new ConsoleIO();

    public static boolean validateArgs(String[] args, IOService ioService) {
            if(args.length == 1) {
            try{
                int turn_of_user = Integer.parseInt(args[0]);
                if(turn_of_user != 1 && turn_of_user != 2) {
                    ioService.println(Message.INVALID_ARGS);
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                ioService.println(Message.INVALID_ARGS);
                return false;
            }
        } else {
            ioService.println(Message.INVALID_ARGS);
            return false;
        }
    } 



    public static void main(String[] args) {
        int turn_of_user;

        if (!validateArgs(args, ioService)) {
            return;
        } else {
            turn_of_user = Integer.parseInt(args[0]);
        }


        Tictactoe tic_tac_toe_game = new Tictactoe(turn_of_user, ioService);
        tic_tac_toe_game.startGame();
    }
}
