package org.myproject.sc_stateless_http;

import org.myproject.sc_stateless_http.JsonDTO.*;
import org.myproject.common.board.*;
import org.myproject.common.constant.*;
import org.myproject.common.player.Bot;
import org.myproject.common.game.TictactoeWinner;

import com.google.gson.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/start", "/move"})
public class ServletHandler extends HttpServlet {
    private final int BOARD_SIZE = 3; // Assuming a NxN board for Tic Tac Toe
    private final Gson gson = new Gson();

    public ServletHandler() {}




    /**
     * Handle CORS preflight OPTIONS requests globally for Tomcat
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Handle POST requests for the /start and /move endpoints
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json; charset=UTF-8");

        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        try (BufferedReader reader = request.getReader()) {
            String requestBody = reader.lines().collect(Collectors.joining("\n"));

            if ("/start".equals(path)) {
                handleStart(response, requestBody);
            } else if ("/move".equals(path)) {
                handleMove(response, requestBody);
            } else {
                sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, createErrorJson("Not Found"));
            }
        } catch (JsonSyntaxException e) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, createErrorJson("Malformed JSON payload."));
        } catch (Exception e) {
            sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, createErrorJson("Internal Server Error: " + e.getMessage()));
        }
    }









    // --------------------------------- ENDPOINT HANDLERS ------------------------------- //

    /**
     * Handle the /start endpoint, which initializes the game based on the player's turn. <br>
     * The method will send the welcome message, initial board, or bot's first move if the bot goes first, and new board state.
     * @param response the HttpServletResponse object to send the response through
     * @param body the raw JSON string from the request body containing the player's turn
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void handleStart(HttpServletResponse response, String body) throws IOException {
        // Expected Request JSON: { "turn": 1 } or { "turn": 2 }
        StartRequest request = gson.fromJson(body, StartRequest.class);
        if (request == null || request.turn == null) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, createErrorJson("Invalid request format."));
            return;
        } else if (request.turn != 1 && request.turn != 2) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, createErrorJson("Invalid turn value. Must be 1 (go first) or 2 (go second)."));
            return;
        }
        
        // Extract player's turn and initialize the game state
        int turn = request.turn;
        SquareBoard board = new SquareBoard1D(BOARD_SIZE);
        
        // Build the JSON response with welcome message and initial board state
        StartResponse startResponse = new StartResponse();
        startResponse.welcome_message = Message.WELCOME_MESSAGE;
        startResponse.init_board_msg = board.serialize();

        // If user wants to go second, Bot makes the first move
        if (turn == 2) {
            int botMove = Bot.getBotDecision(board);
            board.setCellValue(botMove, 1);
            startResponse.bot_move = botMove;
            startResponse.new_board_msg = board.serialize();
        }

        sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(startResponse));
    }



    /**
     * Handle the /move endpoint, which processes the player's move, updates the board, checks for game end conditions, and generates the bot's response.
     * @param response the HttpServletResponse object to send the response through  
     * @param body the raw JSON string from the request body containing the player's move and current board state
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void handleMove(HttpServletResponse response, String body) throws IOException {
        MoveRequest request = gson.fromJson(body, MoveRequest.class);
        // Validate the request format
        if (request == null || request.turn == null || request.board_msg == null || request.move == null) {
            sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, createErrorJson("Invalid request format."));
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
        
        sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(jsonResponse));
    }






    


    // --------------------------------- UTILITY METHODS ------------------------------- //


    /**
     * Set CORS headers to allow cross-origin requests.
     * @param response the HttpServletResponse object to set headers on
     */
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    }

    /**
     * Utility method to send JSON response with appropriate headers and status code.
     * @param response the HttpServletResponse object to send the response through
     * @param statusCode the HTTP status code to send
     * @param responseJson the JSON string to send in the response body
     * @throws IOException if an I/O error occurs while sending the response
     */
    public void sendJsonResponse(HttpServletResponse response, int statusCode, String responseJson) throws IOException {
        response.setStatus(statusCode);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(responseJson);
            writer.flush();
        }
    }

    /**
     * Creates a JSON object representing an error message.
     * @param errorMessage the error message to include in the JSON
     * @return the JSON string representing the error
     */
    public String createErrorJson(String errorMessage) {
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
