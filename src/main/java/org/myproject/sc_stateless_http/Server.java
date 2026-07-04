package org.myproject.sc_stateless_http;

import com.sun.net.httpserver.HttpServer;

import org.myproject.common.constant.Constant;
import org.myproject.common.io.ConsoleIO;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final ConsoleIO consoleIO = new ConsoleIO();
    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static HttpServer server;
    


    public static void main(String[] args) {

        try {
            server = HttpServer.create(new InetSocketAddress(Constant.SERVER_PORT), 0);
            
            ClientHandler handler = new ClientHandler(consoleIO);
            server.createContext("/start", handler);
            server.createContext("/move", handler);

            // Force the server to use exactly one background thread for all requests
            server.setExecutor(singleThreadExecutor);            
            server.start();
            consoleIO.println("Single-threaded HTTP Server started on port " + Constant.SERVER_PORT);

        } catch (Exception e) {
            consoleIO.println("Error occurred: " + e.getMessage());
            if (singleThreadExecutor != null) {
                singleThreadExecutor.shutdown();
            }
        }
    }
}