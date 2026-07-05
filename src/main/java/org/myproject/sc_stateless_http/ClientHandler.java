package org.myproject.sc_stateless_http;

import org.myproject.sc_stateless_http.JsonDTO.*;
import org.myproject.common.board.*;
import org.myproject.common.constant.*;
import org.myproject.common.player.Bot;
import org.myproject.common.io.IOService;
import org.myproject.common.game.TictactoeWinner;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ClientHandler implements HttpHandler {
    private final int BOARD_SIZE = 4; // Assuming a NxN board for Tic Tac Toe
    private final IOService ioService;
    private final Gson gson = new Gson();

    public ClientHandler(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS and JSON headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        // Handle preflight OPTIONS request if modern web clients call it
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        // Only allow POST requests for game actions
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, createErrorJson("Method Not Allowed. Use POST."));
            return;
        }

        // Determine the endpoint being called and handle accordingly
        String path = exchange.getRequestURI().getPath();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            String requestBody = reader.lines().collect(Collectors.joining("\n"));

            if ("/start".equals(path)) {
                handleStart(exchange, requestBody);
            } else if ("/move".equals(path)) {
                handleMove(exchange, requestBody);
            } else {
                sendJsonResponse(exchange, 404, createErrorJson("Not Found"));
            }
        } catch (JsonSyntaxException e) {
            sendJsonResponse(exchange, 400, createErrorJson("Malformed JSON payload."));
        } catch (Exception e) {
            ioService.println("Error handling request: " + e.getMessage());
            sendJsonResponse(exchange, 500, createErrorJson("Internal Server Error: " + e.getMessage()));
        }
    }









    // --------------------------------- ENDPOINT HANDLERS ------------------------------- //

    /**
     * Handle the /start endpoint, which initializes the game based on the player's turn. <br>
     * The method will send the welcome message, initial board, or bot's first move if the bot goes first, and new board state.
     * @param exchange the HttpExchange object representing the incoming request and response
     * @param body the raw JSON string from the request body containing the player's turn
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void handleStart(HttpExchange exchange, String body) throws IOException {
        // Expected Request JSON: { "turn": 1 } or { "turn": 2 }
        StartRequest request = gson.fromJson(body, StartRequest.class);
        if (request == null || request.turn == null) {
            sendJsonResponse(exchange, 400, createErrorJson("Invalid request format."));
            return;
        } else if (request.turn != 1 && request.turn != 2) {
            sendJsonResponse(exchange, 400, createErrorJson("Invalid turn value. Must be 1 (go first) or 2 (go second)."));
            return;
        }
        
        // Extract player's turn and initialize the game state
        int turn = request.turn;
        SquareBoard board = new SquareBoard1D(BOARD_SIZE);
        
        // Build the JSON response with welcome message and initial board state
        StartResponse response = new StartResponse();
        response.welcome_message = Message.WELCOME_MESSAGE;
        response.init_board_msg = board.serialize();

        // If user wants to go second, Bot makes the first move
        if (turn == 2) {
            int botMove = Bot.getBotDecision(board);
            board.setCellValue(botMove, 1);
            response.bot_move = botMove;
            response.new_board_msg = board.serialize();
        }

        sendJsonResponse(exchange, 200, gson.toJson(response));
    }



    /**
     * Handle the /move endpoint, which processes the player's move, updates the board, checks for game end conditions, and generates the bot's response.
     * @param exchange the HttpExchange object representing the incoming request and response
     * @param body the raw JSON string from the request body containing the player's move and current board state
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void handleMove(HttpExchange exchange, String body) throws IOException {
        MoveRequest request = gson.fromJson(body, MoveRequest.class);
        // Validate the request format
        if (request == null || request.turn == null || request.board_msg == null || request.move == null) {
            sendJsonResponse(exchange, 400, createErrorJson("Invalid request format."));
            return;
        }
        // Extract the board state, player's move, and turn from the request
        SquareBoard board = SquareBoard.deserialize(request.board_msg);
        int playerMove = request.move;
        int playerTurn = request.turn;

        // Create the JSON response object
        MoveResponse jsonResponse = new MoveResponse();

        // 1. Player's Turn Logic
        board.setCellValue(playerMove, playerTurn);
        jsonResponse.user_move_board_msg = board.serialize();
        jsonResponse.winner_id = TictactoeWinner.getWinner(board);
        jsonResponse.end_status = getEndGameStatus(board);
        // 2. If game not end, Bot's Turn Logic 
        if (!jsonResponse.end_status) {
            int botTurn = 3 - playerTurn;
            int botMove = Bot.getBotDecision(board);
            board.setCellValue(botMove, botTurn);
            jsonResponse.bot_move = botMove;
            jsonResponse.bot_move_board_msg = board.serialize();
            jsonResponse.winner_id = TictactoeWinner.getWinner(board);
            jsonResponse.end_status = getEndGameStatus(board);
        }
        
        sendJsonResponse(exchange, 200, gson.toJson(jsonResponse));
    }





    


    // --------------------------------- UTILITY METHODS ------------------------------- //


    /**
     * Utility method to send JSON response with appropriate headers and status code.
     * @param exchange the HttpExchange object to send the response through
     * @param statusCode the HTTP status code to send
     * @param responseJson the JSON string to send in the response body
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String responseJson) throws IOException {
        byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    /**
     * Creates a JSON object representing an error message.
     * @param errorMessage the error message to include in the JSON
     * @return the JSON string representing the error
     */
    private String createErrorJson(String errorMessage) {
        JsonObject errorObj = new JsonObject();
        errorObj.addProperty("error", errorMessage);
        return gson.toJson(errorObj);
    }

    /**
     * Check if the game has ended by checking for a winner or a draw and return the appropriate message.
     * @param board the current state of the board
     * @return true if the game has ended (either a win or a draw), false otherwise
     */
    private boolean getEndGameStatus(SquareBoard board) {
        int winnerId = TictactoeWinner.getWinner(board);
        if (winnerId != -1) {
            return true;
        } else if (board.isFull()) {
            return true;
        }
        return false;
    }
}