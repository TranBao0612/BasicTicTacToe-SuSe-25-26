package org.myproject.common.io;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP-based implementation of the IOService interface. 
 * This class allows for input and output operations over a TCP connection.
 */
public class TcpIO implements IOService, AutoCloseable {
    private Socket tcpSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Initialize the TcpIO with a given TCP socket. This sets up the reader and writer for communication over the socket.
     * @param tcpSocket
     */
    public TcpIO(Socket tcpSocket) {
        this.tcpSocket = tcpSocket;
        try {
            this.reader = new BufferedReader(new java.io.InputStreamReader(tcpSocket.getInputStream()));
            this.writer = new PrintWriter(tcpSocket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clean up resources by closing the socket and associated reader and writer.
     */
    @Override
    public void close() {
        try {
            if (tcpSocket != null && !tcpSocket.isClosed())
                tcpSocket.close();
            if (writer != null)
                writer.close();
            if (reader != null)
                reader.close();
        } catch (Exception ignored) {}
    }

    /**
     * Read the next line of input from the TCP socket. This method blocks until a line of input is available.
     * @return the next line of input from the TCP socket
     * @throws RuntimeException if there is an error reading from the TCP socket
     */
    @Override
    public String nextLine() {
        try {
            return reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException("Error reading from TCP socket", e);
        }
    }

    /**
     * Print a message to the TCP socket, followed by a newline. This method flushes the output stream after writing.
     * @param message the message to print to the TCP socket
     */
    @Override
    public void println(String message) {
        writer.println(message);
    }

    /**
     * Print a message to the TCP socket without a newline. This method flushes the output stream after writing.
     * @param message the message to print to the TCP socket
     */
    @Override
    public void print(String message) {
        writer.print(message);
        writer.flush();
    }
    
}
