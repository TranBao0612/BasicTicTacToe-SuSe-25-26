package org.myproject.sc_stateless_http;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.*;
import org.myproject.common.player.User;
import org.myproject.common.io.*;
import org.myproject.sc_stateless_http.JsonDTO.*;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    private String SERVER_URL = String.format("http://%s:%d", Constant.SERVER_HOST, Constant.HTTP_PORT);
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();
    private ConsoleIO consoleIO = new ConsoleIO();

    int user_turn;
    private String user_turn_msg;
    private String bot_turn_msg;
    private StartRequest startReq;
    private MoveRequest moveReq;
    private SquareBoard currentBoard;

    private Client() {
        int user_turn = askForArgs(consoleIO);
        this.user_turn = user_turn;
        this.user_turn_msg = Message.getPlayersTurnMessage(user_turn);
        this.bot_turn_msg = Message.getPlayersTurnMessage(3 - user_turn);

        this.startReq = new StartRequest();
        startReq.turn = user_turn;
        this.moveReq = new MoveRequest();
        moveReq.turn = user_turn;
    }


    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startRequestHandler();
            client.gameLoop();

        } catch (Exception e) {
            client.consoleIO.println("An error occurred during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }





    // --------------------------------- HTTP REQUEST HANDLERS ------------------------------- //

    /**
     * Handle the initial start request to the server.
     * @throws Exception if an error occurs during the HTTP request or response processing
     */
    private void startRequestHandler() throws Exception {
        // Send request to /start endpoint and receive response
        String startJson = sendPostRequest("/start", gson.toJson(startReq));
        StartResponse startRes = gson.fromJson(startJson, StartResponse.class);
        currentBoard = SquareBoard.deserialize(startRes.init_board_msg);
        // Print the welcome message and initial board to the console
        consoleIO.println(startRes.welcome_message);
        consoleIO.println(currentBoard.toString());
        // If the bot goes first, print the bot's move and the updated board to the console
        if (user_turn == 2) {
            consoleIO.println(bot_turn_msg);
            consoleIO.println(startRes.bot_move);
            currentBoard = SquareBoard.deserialize(startRes.new_board_msg);
            consoleIO.println(currentBoard.toString());
        }
    }

    /**
     * Handle the game loop, which runs after the initial start request and continues until the game is over.
     * @throws Exception if an error occurs during the HTTP request or response processing
     */
    private void gameLoop() throws Exception {
        boolean game_over = false;
        while (!game_over) {
            // Ask the user for their move and validate it
            consoleIO.println(user_turn_msg);
            int user_move = User.getUserDecision(consoleIO, currentBoard, user_turn_msg);
            if (user_move == -1) {
                game_over = true;
                continue;
            }
            // Send request to /move endpoint and receive response
            MoveResponse moveRes = sendMoveRequest(user_move);
            game_over = moveRes.end_status;
            processMoveResponse(moveRes);
        }
    }

    /**
     * Send a move request to the server with the user's move and receive the response.
     * @param user_move the move made by the user
     * @return the response from the server
     * @throws Exception if an error occurs during the HTTP request or response processing
     */
    private MoveResponse sendMoveRequest(int user_move) throws Exception {
        this.moveReq.board_msg = currentBoard.serialize();
        this.moveReq.move = user_move;
        String moveJson = sendPostRequest("/move", gson.toJson(this.moveReq));
        return gson.fromJson(moveJson, MoveResponse.class);
    }

    /**
     * Process the response from the server after sending a move request.
     * @param moveRes the response from the server
     */
    private void processMoveResponse(MoveResponse moveRes) {
        boolean game_over = moveRes.end_status;
        SquareBoard userMoveBoard = SquareBoard.deserialize(moveRes.user_move_board_msg);
        SquareBoard botMoveBoard = moveRes.bot_move_board_msg != null ? SquareBoard.deserialize(moveRes.bot_move_board_msg) : null;
        // Print information in the response to the console
        consoleIO.println(userMoveBoard.toString());
        if (moveRes.bot_move != null) {
            consoleIO.println(bot_turn_msg);
            consoleIO.println(moveRes.bot_move);
            consoleIO.println(botMoveBoard.toString());
        }
        // Print the end game message if game over, else update the current board based on the server's response
        if(game_over) {
            if (moveRes.winner_id == -1) {
                consoleIO.println(Message.DRAW_MESSAGE);
            } else {
                consoleIO.println(Message.getWinnerMessage(moveRes.winner_id));
            }
        } else {
            currentBoard = botMoveBoard != null? botMoveBoard : userMoveBoard;
        }
    }




    // --------------------------------- UTILITY METHODS ------------------------------- //

    /**
     * Send a POST request to the server with the given endpoint and JSON payload, and return the response body as a string.
     * @param endpoint the endpoint to send the POST request to (e.g., "/start" or "/move")
     * @param jsonPayload the JSON payload to send in the POST request body
     * @return the response body from the server as a string
     * @throws Exception if an error occurs during the HTTP request, or if the server returns a non-200 status code
     */
    private String sendPostRequest(String endpoint, String jsonPayload) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new RuntimeException("Server returned HTTP " + response.statusCode() + ": " + response.body());
        }
        return response.body();
    }


    /**
     * Ask the client for the arguments needed to start the game. In this case, we only need the turn of the user (1 or 2).
     * @return the turn of the user (1 or 2)
     */
    private int askForArgs(IOService ioService) {
        int turn_of_user;
        ioService.println(Message.REQUEST_ARGS);
        String args;
        while(true) {
            args = ioService.nextLine();
            try{
                turn_of_user = Integer.parseInt(args.strip());
                if(turn_of_user == 1 || turn_of_user == 2) {
                    return turn_of_user;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                ioService.println(Message.INVALID_ARGS);
            }
        }
    }
}