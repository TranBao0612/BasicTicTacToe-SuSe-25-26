package org.myproject.sc_multithread_multiuser;

import org.myproject.common.game.Tictactoe;
import org.myproject.common.io.IOService;
import org.myproject.common.io.TcpIO;

import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private TcpIO tcpIO;
    private IOService ioService;

    public ClientHandler(Socket clientSocket, IOService ioService) {
        this.clientSocket = clientSocket;
        this.tcpIO = new TcpIO(clientSocket);
        this.ioService = ioService;
    }

    /**
     * Handle the client connection by 
     *      asking for the necessary arguments to start the game, 
     *      then create a Tictactoe game instance and start the game. <br>
     * The method will run until the game ends or an exception occurs (e.g., client disconnects).
     */
    @Override
    public void run() {
        try {
            int turn_of_user = Integer.parseInt(tcpIO.nextLine());
            Tictactoe game = new Tictactoe(turn_of_user, tcpIO);
            game.startGame();
            ioService.println("Client: " + clientSocket.getInetAddress() + " disconnected gracefully.");
        } catch (Exception e) {
            ioService.println("Client: " + clientSocket.getInetAddress() + " Error occurred: " + e.getMessage());
        } finally {
            tcpIO.close();
        }
    }


}
