package org.myproject.sc_stateless_tcp;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.Constant;
import org.myproject.common.constant.Message;
import org.myproject.common.player.User;
import org.myproject.common.io.*;

import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        IOService ioService = new ConsoleIO();
        boolean end_program = false;
        TcpIO tcpIO = null;
        String msgFromServer;
        User user;

        try {
            int turn_of_user = askForArgs(ioService);
            user = new User(turn_of_user, ioService);
            tcpIO = startSession();
            tcpIO.println(Message.START_FLAG + user.getPlayerId());

            // Main loop to receive messages from server and print them to console, until the game ends
            while (!end_program) {
                // Receive message from server
                msgFromServer = tcpIO.nextLine();

                if (msgFromServer == null) {
                    ioService.println("Server was downed. Ending program.");
                    end_program = true;


                } else if (msgFromServer.startsWith(Message.END_FLAG)) {
                    end_program = true;


                } else if (msgFromServer.startsWith(Message.PROMPT_FLAG)) {
                    // End current session
                    tcpIO.close();
                    String board = msgFromServer.substring(Message.PROMPT_FLAG.length());
                    // Get user's decision
                    int move = user.makeDecision(SquareBoard.deserialize(board));
                    if (move == -1) {
                        end_program = true;
                    } else {
                        tcpIO = startSession();
                        tcpIO.println(String.format("%s;%d;%d", board, move, user.getPlayerId()));
                    }


                } else {
                    ioService.println(msgFromServer);
                }
            }

        } catch (Exception e) {
            ioService.println("Error connecting to server: " + e.getMessage());
        } finally {
            if (tcpIO != null) {
                tcpIO.close();
            }
        }

    }

    /**
     * Ask the client for the arguments needed to start the game. In this case, we only need the turn of the user (1 or 2).
     * @return the turn of the user (1 or 2)
     */
    private static int askForArgs(IOService ioService) {
        ioService.println(Message.REQUEST_ARGS);
        int turn_of_user;
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


    /**
     * Start a session by connecting to the server and returning the TcpIO object for communication.
     * @return the TcpIO object for communication with the server
     * @throws RuntimeException if there is an error connecting to the server
     */
    private static TcpIO startSession() {
        try {
            Socket socket = new Socket(Constant.SERVER_HOST, Constant.SERVER_PORT);
            // socket.setSoTimeout(Constant.SOCKET_TIMEOUT_MS);
            return new TcpIO(socket);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to server: " + e.getMessage());
        }
    }

    
}
