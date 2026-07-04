package org.myproject.sc_stateless_tcp;

import org.myproject.common.constant.Message;
import org.myproject.common.game.TictactoeWinner;
import org.myproject.common.io.*;
import org.myproject.common.player.Bot;
import org.myproject.common.board.*;

import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private TcpIO tcpIO;
    private IOService ioService;
    private String msgFromClient;

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
            msgFromClient = tcpIO.nextLine();
            if (msgFromClient == null) {
                ioService.println("Request failed: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " Error occurred: Client disconnected.");
                return;
            } else if (msgFromClient.startsWith(Message.START_FLAG)) {
                handleStartRequest(msgFromClient.substring(Message.START_FLAG.length()));
            } else {
                handleMoveRequest(msgFromClient);
            }



            ioService.println("Request handled successfully: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
        } catch (Exception e) {
            ioService.println("Request failed: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " Error occurred: " + e.getMessage());
        } finally {
            tcpIO.close();
        }
    }


    // -------------------------------- Request Handlers ---------------------------------
    /**
     * Send Welcome message, message in order: board, player's turn message, player move, and finally prompt message to the client.
     * @param payload the payload from the client, which contains the player ID (1 or 2)
     * @throws Exception if the payload is invalid or an error occurs during processing
     */
    private void handleStartRequest(String payload) throws Exception {
        // Validate payload
        payload = payload.trim();
        if (!payload.equals("1") && !payload.equals("2")) {
            throw new Exception("Invalid player ID. Expected 1 or 2, but got: " + payload);
        }

        // Processing
        tcpIO.println(Message.WELCOME_MESSAGE);
        SquareBoard board = new SquareBoard1D(3);
        tcpIO.println(board.toString());
        tcpIO.println(Message.getPlayersTurnMessage(1));

        int playerId = Integer.parseInt(payload);
        if (playerId != 1) {
            int botDecision = Bot.getBotDecision(board);
            tcpIO.println(String.valueOf(botDecision));
            board.setCellValue(botDecision, 3 - playerId);
            tcpIO.println(board.toString());
            tcpIO.println(Message.getPlayersTurnMessage(2));
        }

        tcpIO.println(getPromptMessage(board));
    }


    /**
     * Handle the move request from the client.
     * @param payload the payload from the client, which contains the board state, move, and player ID
     * @throws Exception if the payload is invalid or an error occurs during processing
     */
    private void handleMoveRequest(String payload) throws Exception {
        // Validate payload
        String[] parts = payload.split(";");
        if (parts.length != 3) {
            throw new Exception("Invalid payload format: " + payload + ". Expected format: <board>; <move>; <playerId>");
        }
        SquareBoard board;
        int move;
        int playerId;
        try {
            board = SquareBoard.deserialize(parts[0]);
            move = Integer.parseInt(parts[1]);
            playerId = Integer.parseInt(parts[2]);
            ioService.println("Received move request: board=" + parts[0] + ", move=" + move + ", playerId=" + playerId);
        } catch (Exception e) {
            throw new Exception("Invalid payload format: " + payload + ". Expected format: <board>; <move>; <playerId>");
        }

        // Processing
            // Player's turn
        board.setCellValue(move, playerId);
        tcpIO.println(board.toString());
        if (isEndGame(board)) {
            return;
        }

            // Bot's turn
        tcpIO.println(Message.getPlayersTurnMessage(3 - playerId));
        int botDecision = Bot.getBotDecision(board);
        tcpIO.println(String.valueOf(botDecision));
        board.setCellValue(botDecision, 3 - playerId);
        tcpIO.println(board.toString());
        if (isEndGame(board)) {
            return;
        }

        // Prompt for the next move
        tcpIO.println(getPromptMessage(board));

    }



    // -------------------------------- Utility Methods ---------------------------------
    private String getPromptMessage(SquareBoard board) {
        return Message.PROMPT_FLAG + board.serialize();
    }

    private boolean isEndGame(SquareBoard board) {
        int winner_id = TictactoeWinner.getWinner(board);
        if (winner_id != -1) {
            tcpIO.println(Message.getWinnerMessage(winner_id));
            tcpIO.println(Message.END_FLAG);
            return true;
        } else if (board.isFull()) {
            tcpIO.println(Message.DRAW_MESSAGE);
            tcpIO.println(Message.END_FLAG);
            return true;
        }
        return false;
    }
}
