package org.myproject.sc_multithread_multiuser;

import org.myproject.common.constant.Constant;
import org.myproject.common.io.*;
import org.myproject.sc_singlethread_singleuser.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadPool {
    public static IOService ioService = new ConsoleIO();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        try {
            serverSocket = new ServerSocket(Constant.SERVER_PORT);
            ioService.println("Server started on port " + Constant.SERVER_PORT);

            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                ioService.println("Client connected: " + clientSocket.getInetAddress());

                executorService.submit(new ClientHandler(clientSocket, ioService));
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
            executorService.shutdown();
        }

    }

    
}