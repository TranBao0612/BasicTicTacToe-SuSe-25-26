package org.myproject.sc_singlethread_singleuser;

import org.myproject.common.constant.Constant;
import org.myproject.common.io.*;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static IOService ioService = new ConsoleIO();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
            ioService.println("Server started on port " + Constant.SERVER_PORT);

            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                ioService.println("Client connected: " + clientSocket.getInetAddress());

                new ClientHandler(clientSocket, ioService).run();

                // // Handle the client connection in a separate thread
                // new Thread(new ClientHandler(clientSocket, consoleIO)).start();
            }

        } catch (Exception e) {
            ioService.println("Error occurred: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    ioService.println("Error closing server socket: " + e.getMessage());
                }
            }
        }

    }

    
}
