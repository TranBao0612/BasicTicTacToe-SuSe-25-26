package org.myproject.sc_singlethread_singleuser;

import org.myproject.common.constant.Constant;
import org.myproject.common.constant.Message;
import org.myproject.common.io.*;

import java.net.Socket;

public class Client {
    public static IOService ioService = new ConsoleIO();
    private static int turn_of_user;


    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Constant.SERVER_HOST, Constant.SERVER_PORT);
            TcpIO tcpIO = new TcpIO(socket);
            boolean end_program = false;

            turn_of_user = askForArgs();
            tcpIO.println(String.valueOf(turn_of_user));

            while (!end_program) {
                String serverMessage = tcpIO.nextLine();
                if (serverMessage.startsWith(Message.END_FLAG)) {
                    end_program = true;
                } else if (serverMessage.startsWith(Message.PROMPT_FLAG)) {
                    tcpIO.println(serverMessage.substring(Message.PROMPT_FLAG.length()));
                    String input = ioService.nextLine();
                    tcpIO.println(input);
                } else {
                    ioService.println(serverMessage);
                }
            }

            tcpIO.close();


        } catch (Exception e) {
            ioService.println("Error connecting to server: " + e.getMessage());
        }

    }

    /**
     * Ask the client for the arguments needed to start the game. In this case, we only need the turn of the user (1 or 2).
     * @return the turn of the user (1 or 2)
     */
    private static int askForArgs() {
        ioService.println(Message.REQUEST_ARGS);
        String args;
        while(true) {
            args = ioService.nextLine();
            String[] args_array = args.split(" ");
            if(args_array.length == 1) {
                try{
                    turn_of_user = Integer.parseInt(args_array[0]);
                    if(turn_of_user != 1 && turn_of_user != 2) {
                        ioService.println(Message.INVALID_ARGS);
                        continue;
                    }
                    return turn_of_user;
                } catch (NumberFormatException e) {
                    ioService.println(Message.INVALID_ARGS);
                    continue;
                }
            } else {
                ioService.println(Message.INVALID_ARGS);
                continue;
            }
        }
    }

    
}
