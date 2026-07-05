# MAIN
## common
### Board
#### SquareBoard
```java
package org.myproject.common.board;

import java.util.List;

import org.myproject.common.constant.Constant;

import java.util.ArrayList;

/**
 * This class represents a square board for a game.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public abstract class SquareBoard {
    protected int size;
    protected int total_cells;
    protected int empty_cell_value = Constant.EMPTY_CELL_VALUE;

    protected SquareBoard(int size) {
        this.size = size;
        this.total_cells = size * size;
    }


    // ------------------ Abstract methods to be implemented by subclasses ------------------
    public abstract int getCellValue(int cell_id);
    public abstract int getCellValue(int row, int col);

    public abstract void setCellValue(int cell_id, int value);
    public abstract void setCellValue(int row, int col, int value);



    // ------------------ Concrete methods implemented in this class ------------------
    /**
     * Get a list of all cell values on the board, in row-major order (i.e., left to right, top to bottom).
     * @return a list of all cell values on the board, in row-major order.
     */
    public List<Integer> getAllCells() {
        List<Integer> cells = new ArrayList<>();
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            cells.add(getCellValue(cell_id));
        }
        return cells;
    }
    /**
     * Set the values of all cells on the board, given a list of cell values in row-major order (i.e., left to right, top to bottom).
     * @param values a list of cell values to set on the board, in row-major order.
     * @throws IllegalArgumentException if the number of cell values does not match the total number
     */
    public void setAllCells(List<Integer> values) {
        if (values.size() != total_cells) {
            throw new IllegalArgumentException("Invalid number of cell values: expected " + total_cells + ", got " + values.size());
        }
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            setCellValue(cell_id, values.get(cell_id - 1));
        }
    }


    /**
     * Get the size of the board.
     * @return number of rows/columns in the square board.
     */
    public int getSize() {
        return size;
    }
    /**
     * Get the total number of cells on the board.
     * @return total number of cells on the board.
     */
    public int getTotalCells() {
        return total_cells;
    }
    /**
     * Get the value that represents an empty cell on the board.
     * @return the value that represents an empty cell on the board.
     */
    public int getEmptyCellValue() {
        return empty_cell_value;
    }


    /**
     * Check if the given cell id is valid for this board.
     * @param cell_id the cell id to check.
     * @return true if the cell id is valid, false otherwise.
     */
    public boolean isValidCell(int cell_id){
        return cell_id >= 1 && cell_id <= total_cells;
    }
    /**
     * Check if the given cell position is valid for this board.
     * @param row the row of the cell to check.
     * @param col the column of the cell to check.
     * @return true if the cell position is valid, false otherwise.
     */
    public boolean isValidCell(int row, int col) {
        return row >= 1 && row <= size && col >= 1 && col <= size;
    }


    /**
     * Check if the cell with the given id is empty.
     * @param cell_id the cell id to check.
     * @return true if the cell is empty, false otherwise.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    public boolean isCellEmpty(int cell_id) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        return getCellValue(cell_id) == empty_cell_value;
    }
    /**
     * Check if the cell at the given position is empty.
     * @param row the row of the cell to check.
     * @param col the column of the cell to check.
     * @return true if the cell is empty, false otherwise.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    public boolean isCellEmpty(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        return getCellValue(row, col) == empty_cell_value;
    }

    /**
     * Get a list of cell ids that are currently empty on the board.
     * @return a list of cell ids that are currently empty on the board.
     */
    public List<Integer> getEmptyCells() {
        List<Integer> emptyCells = new ArrayList<>();
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (isCellEmpty(cell_id)) {
                emptyCells.add(cell_id);
            }
        }
        return emptyCells;
    }

    /**
     * Check if the board is empty (i.e., all cells are empty).
     * @return true if the board is empty, false otherwise.
     */
    public boolean isEmpty() {
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (!isCellEmpty(cell_id)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Check if the board is full (i.e., no empty cells).
     * @return true if the board is full, false otherwise.
     */
    public boolean isFull() {
        for (int cell_id = 1; cell_id <= total_cells; cell_id++) {
            if (isCellEmpty(cell_id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a string representation of the board, showing the value of each cell in a grid format.
     * @return a string representation of the board.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                sb.append("| ").append(getCellValue(row, col)).append(" ");
            }
            sb.append("|");
            if (row < size)
                sb.append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Serialize the board to deserilaizable string format to be sent over the network or saved to a file.
     * @return cell values of the board in a comma-separated string format, in row-major order.
     */
    public String serialize() {
        return String.join(",", getAllCells().stream().map(String::valueOf).toArray(String[]::new));
    }

    /**
     * Deserialize a serialized board string back into a SquareBoard object.
     * @param serializedBoard the serialized board string to deserialize.
     * @return a new SquareBoard object with the same cell values as the serialized board. Underlying implementation is SquareBoard1D.
     * @throws IllegalArgumentException if the serialized board string is invalid (e.g., wrong format or size).
     */
    public static SquareBoard deserialize(String serializedBoard) {
        String[] cellValues = serializedBoard.split(",");
        // Check if the number of cell values is a perfect square to determine the size of the board
        int size = (int) Math.sqrt(cellValues.length);
        if (size * size != cellValues.length) {
            throw new IllegalArgumentException("Invalid serialized board size: " + serializedBoard);
        }
        // Create a new SquareBoard instance based on the size and fill it with the deserialized values
        SquareBoard board = new SquareBoard1D(size);
        List<Integer> values = new ArrayList<>();
        try {
            for (String cellValue : cellValues) {
                values.add(Integer.parseInt(cellValue.trim()));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid serialized board format: " + serializedBoard, e);
        }
        board.setAllCells(values);
        return board;
    }
}

```

#### SquareBoard1D
```java
package org.myproject.common.board;

import java.util.Arrays;


/**
 * This class represents a square board for a game with cells represented in a 1D array.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public class SquareBoard1D extends SquareBoard {
    /**
     * 1D array to represent cells of the board.
     */
    private int[] board;

    /**
     * Initialize the board with the given size.
     * @param size the size of the square board (number of rows/columns).
     */
    public SquareBoard1D(int size) {
        super(size);
        this.board = new int[total_cells];
        Arrays.fill(board, empty_cell_value);
    }

    /**
     * Get the value of the cell with the given id.
     * @param cell_id the id of the cell to get the value of.
     * @return the value of the cell with the given id.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public int getCellValue(int cell_id) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        return board[getIndex(cell_id)];
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public int getCellValue(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        return board[getIndex(row, col)];
    }


    /**
     * Set the value of the cell with the given id.
     * @param cell_id the id of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public void setCellValue(int cell_id, int value) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        board[getIndex(cell_id)] = value;
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public void setCellValue(int row, int col, int value) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        board[getIndex(row, col)] = value;
    }


    /**
     * Get the index in the 1D array for the given cell id.
     * @param cell_id the id of the cell to get the index for.
     * @return the index in the 1D array for the given cell id.
     */
    private int getIndex(int cell_id) {
        return cell_id - 1;
    }
    /**
     * Get the index in the 1D array for the given row and column.
     * @param row the row of the cell to get the index for.
     * @param col the column of the cell to get the index for.
     * @return the index in the 1D array for the given row and column.
     */
    private int getIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }
    
}
```

#### SquareBoard2D
```java
package org.myproject.common.board;

import java.util.Arrays;

/**
 * This class represents a square board for a game with cells represented in a 2D array.
 * Cells on the board can be empty or occupied by a player.
 * Cells id, row and column indices are 1-based.
 */
public class SquareBoard2D extends SquareBoard {
    /**
     * 2D array to represent cells of the board.
     */
    private int[][] board;

    /**
     * Initialize the board with the given size.
     * @param size the size of the square board (number of rows/columns).
     */
    public SquareBoard2D(int size) {
        super(size);
        this.board = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], empty_cell_value);
        }
    }

    /**
     * Get the value of the cell with the given id.
     * @param cell_id the id of the cell to get the value of.
     * @return the value of the cell with the given id.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public int getCellValue(int cell_id) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        return board[getRowIndex(cell_id)][getColIndex(cell_id)];
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public int getCellValue(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        return board[getRowIndex(row, col)][getColIndex(col, row)];
    }


    /**
     * Set the value of the cell with the given id.
     * @param cell_id the id of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell id is invalid.
     */
    @Override
    public void setCellValue(int cell_id, int value) {
        if (!isValidCell(cell_id)) {
            throw new IllegalArgumentException("Invalid cell id: " + cell_id);
        }
        board[getRowIndex(cell_id)][getColIndex(cell_id)] = value;
    }
    /**
     * Set the value of the cell with the given row and column.
     * @param row the row of the cell to set the value of.
     * @param col the column of the cell to set the value of.
     * @param value the value to set the cell to.
     * @throws IllegalArgumentException if the cell position is invalid.
     */
    @Override
    public void setCellValue(int row, int col, int value) {
        if (!isValidCell(row, col)) {
            throw new IllegalArgumentException("Invalid cell position: (" + row + ", " + col + ")");
        }
        board[getRowIndex(row, col)][getColIndex(col, row)] = value;
    }



    /**
     * Get the row index of the cell in the 2D array with the given id.
     * @param cell_id the id of the cell to get the row index of.
     * @return the row index of the cell with the given id.
     */
    private int getRowIndex(int cell_id) {
        return (cell_id - 1) / size;
    }
    /**
     * Get the row index of the cell in the 2D array with the given row and column.
     * @param row the row of the cell to get the row index of.
     * @param col the column of the cell to get the row index of.
     * @return the row index of the cell with the given row and column.
     */
    private int getRowIndex(int row, int col) {
        return row - 1;
    }
    /**
     * Get the column index of the cell in the 2D array with the given id.
     * @param cell_id the id of the cell to get the column index of.
     * @return the column index of the cell with the given id.
     */
    private int getColIndex(int cell_id) {
        return (cell_id - 1) % size;
    }
    /**
     * Get the column index of the cell in the 2D array with the given row and column.
     * @param col the column of the cell to get the column index of.
     * @param row the row of the cell to get the column index of.
     * @return the column index of the cell with the given row and column.
     */
    private int getColIndex(int col, int row) {
        return col - 1;
    }
    
}
```

### constant
#### Constant
```java
package org.myproject.common.constant;

public final class Constant {
    public static final int EMPTY_CELL_VALUE = 0;

    public static final int SOCKET_TIMEOUT_MS = 3000; // 3 seconds
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 12345;
    public static final int HTTP_PORT = 8080;
}
```
#### Message
```java
package org.myproject.common.constant;

public class Message {
    public static final String START_FLAG = "[START] ";
    public static final String PROMPT_FLAG = "[PROMPT] ";
    public static final String END_FLAG = "[END] ";

    public static final String REQUEST_ARGS = "Please, specify your turn: [1-2]";
    public static final String INVALID_ARGS = "Please, input a valid option [1-2]";

    public static final String WELCOME_MESSAGE = "Hello!";
    public static final String QUIT_MESSAGE = "End of the game";
    public static final String CELL_ALREADY_OCCUPIED_MESSAGE = "The cell is occupied!";
    public static final String DRAW_MESSAGE = "It is a draw!";


    public static final String getInvalidInputMessage(int board_total_cells) {
        return String.format("Please, input a valid number [1-%d]", board_total_cells);
    }
    public static final String getPlayersTurnMessage(int player_id, boolean isUser) {
        String message = String.format("Player#%d's turn.", player_id);
        return isUser ? PROMPT_FLAG + message : message;
    }
    public static final String getPlayersTurnMessage(int player_id) {
        return getPlayersTurnMessage(player_id, false);
    }
    public static final String getWinnerMessage(int player_id) {
        return String.format("Player#%d won!", player_id);
    }
}
```

### game
#### Tictactoe
```java
package org.myproject.common.game;

import org.myproject.common.board.*;
import org.myproject.common.constant.*;
import org.myproject.common.io.IOService;
import org.myproject.common.player.*;


/**
 * Game Tictactoe with 2 players: user and bot. <br>
 *      User can choose to go first or second. <br>
 *      The game will end when there is a winner or a draw.
 */
public class Tictactoe {
    /**
     * Players of the game. Can have more than 2 players in the future if we want to make it more complex.
     */
    private Player[] players = new Player[2];
    private int current_turn;
    private SquareBoard board;
    private boolean end_game = false;
    private IOService ioService;
    private boolean withFlag;



    /**
     * Create a Tic-tac-toe game instance
     * @param turn_of_human_user turn of the human user (1 or 2)
     * @param ioService the IO Service for input/output
     */
    public Tictactoe(int turn_of_human_user, IOService ioService, boolean withFlag) {
        this.board = new SquareBoard1D(3);
        this.ioService = ioService;
        this.withFlag = withFlag;
        initializePlayers(turn_of_human_user);
    }

    public Tictactoe(int turn_of_human_user, IOService ioService) {
        this(turn_of_human_user, ioService, false);
    }



    // ------------------------------ PUBLIC METHODS ------------------------------
    /**
     * Start the game and keep running until the game ends.
     */
    public void startGame() {
        ioService.println(Message.WELCOME_MESSAGE);
        ioService.println(board.toString());
        while (!end_game) {
            runOneTurn();
        }
    }

    /**
     * Execute one turn of the game, include:
     *      Get the decision of the current player, 
     *      update and print the board, check for a winner, 
     *      and switch turn if the game has not ended.
     */
    public void runOneTurn() {
        if (withFlag) {
            ioService.println(Message.getPlayersTurnMessage(players[current_turn].getPlayerId(), players[current_turn] instanceof User));
        } else {
            ioService.println(Message.getPlayersTurnMessage(players[current_turn].getPlayerId()));
        }
        int decision = players[current_turn].makeDecision(board, withFlag);
        if (players[current_turn] instanceof Bot) {
            ioService.println(decision);
        }
        // If the user quit the game, end the game and return.
        if (decision == -1) {
            if (withFlag) {
                ioService.println(Message.END_FLAG);
            }
            end_game = true;
            return;
        }
        // Update the board with the decision of the current player.
        board.setCellValue(decision, players[current_turn].getPlayerId());
        ioService.println(board.toString());
        // Check if there is a winner or a draw.
        int winner_id = TictactoeWinner.getWinner(board);
        if (winner_id != -1) {
            ioService.println(Message.getWinnerMessage(winner_id));
            if (withFlag) {
                ioService.println(Message.END_FLAG);
            }
            end_game = true;
        } else if (board.isFull()) {
            ioService.println(Message.DRAW_MESSAGE);
            if (withFlag) {
                ioService.println(Message.END_FLAG);
            }
            end_game = true;
        } else {
            switchTurn();
        }
    }

    
    // ------------------------------ PRIVATE METHODS -----------------------------    
    private void initializePlayers(int turn_of_human_user) {
        players[0] = new User(turn_of_human_user, ioService);
        players[1] = new Bot(3 - turn_of_human_user);
        current_turn = turn_of_human_user - 1;
    }

    private void switchTurn() {
        current_turn = (current_turn + 1) % players.length;
    }


}
```
#### TictactoeWinner
```java
package org.myproject.common.game;

import org.myproject.common.board.SquareBoard;

public final class TictactoeWinner {
    private TictactoeWinner() {}

    /**
     * Get the winner of the game by checking all rows, columns and diagonals.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    public static final int getWinner(SquareBoard board) {
        if(board.isEmpty())
            return -1;
        int winner_id;
        // Check diagonals first
        winner_id = checkWinnerByFirstDiagonal(board);
        if(winner_id != -1)
            return winner_id;
        winner_id = checkWinnerBySecondDiagonal(board);
        if(winner_id != -1)
            return winner_id;
        // Check rows and columns
        for(int i = 1; i <= board.getSize(); i++) {
            winner_id = checkWinnerByRow(board, i);
            if(winner_id != -1)
                return winner_id;
            winner_id = checkWinnerByColumn(board, i);
            if(winner_id != -1)
                return winner_id;
        }
        // No winner found
        return -1;
    }


    // -----------------------Private methods-----------------------
    /**
     * Check if there is a winner by row.
     * @param board the current state of the board
     * @param row the index of the row to check
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByRow(SquareBoard board, int row) {
        if(board.isCellEmpty(row, 1))
            return -1;
        int first_value = board.getCellValue(row, 1);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(row, i) != first_value)
                return -1;
        }
        return first_value;
    }

    /**
     * Check if there is a winner by column.
     * @param board the current state of the board
     * @param col the index of the column to check
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByColumn(SquareBoard board, int col) {
        if(board.isCellEmpty(1, col))
            return -1;
        int first_value = board.getCellValue(1, col);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(i, col) != first_value)
                return -1;
        }
        return first_value;
    }

    /**
     * Check if there is a winner by top-left to bottom-right diagonal.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerByFirstDiagonal(SquareBoard board) {
        if(board.isCellEmpty(1, 1))
            return -1;
        int first_value = board.getCellValue(1, 1);
        for(int i = 2; i <= board.getSize(); i++) {
            if(board.getCellValue(i, i) != first_value)
                return -1;
        }
        return first_value;
    }
    /**
     * Check if there is a winner by top-right to bottom-left diagonal.
     * @param board the current state of the board
     * @return the id of the player who won, or -1 if there is no winner
     */
    private static int checkWinnerBySecondDiagonal(SquareBoard board) {
        int size = board.getSize();
        if(board.isCellEmpty(1, size))
            return -1;
        int first_value = board.getCellValue(1, size);
        for(int i = 2; i <= size; i++) {
            if(board.getCellValue(i, size - i + 1) != first_value)
                return -1;
        }
        return first_value;
    }
}
```

### io
#### IOService
```java
package org.myproject.common.io;

/**
 * This class represents an input/output service for a game.
 */
public interface IOService {
    public String nextLine();
    public void print(String message);
    public void println(String message);
    public default void println(int message) {
        println(String.valueOf(message));
    };
}
```
#### ConsoleIO
```java
package org.myproject.common.io;

import java.util.Scanner;

/**
 * ConsoleIO is a concrete implementation of the IOService interface that uses the console for input and output.
 */
public class ConsoleIO implements IOService {
    private Scanner scanner;

    /**
     * Constructs a new ConsoleIO instance and initializes the scanner for reading input from the console.
     */
    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of input from the console.
     * @return the next line of input from the console.
     */
    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Prints the specified format string to the console without a newline at the end.
     * @param message the string to be printed to the console.
     */
    @Override
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Prints the specified message to the console with a newline at the end.
     * @param message the string to be printed to the console.
     */
    @Override
    public void println(String message) {
        System.out.println(message);
    }
}
```
#### TcpIO
```java
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
```

### player
#### Player
```java
package org.myproject.common.player;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.Message;
import org.myproject.common.io.IOService;

/**
 * This class represents a player in the game.
 * Each player has a unique id.
 * 
 */
public abstract class Player {
    protected int player_id;
    protected IOService ioService;
    protected String promptMessage;

    protected Player(int player_id, IOService ioService) {
        this.player_id = player_id;
        this.ioService = ioService;
        this.promptMessage = Message.getPlayersTurnMessage(player_id);
    }

    protected Player(int player_id) {
        this(player_id, null);
    }

    /**
     * Get the id of the player.
     * @return the id of the player.
     */
    public int getPlayerId() {
        return player_id;
    }

    /**
     * Make a move for the player based on the current state of the board.
     * @param board the current state of the board.
     * @return the move made by the player.
     */
    public abstract int makeDecision(SquareBoard board, boolean withTag);
    public int makeDecision(SquareBoard board) {
        return makeDecision(board, false);
    }
}
```

#### Bot
```java
package org.myproject.common.player;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.io.IOService;

/**
 * This class represents a bot player in a game. 
 */
public class Bot extends Player {
    public Bot(int player_id, IOService ioService) {
        super(player_id, ioService);
    }

    public Bot(int player_id) {
        super(player_id);
    }

    /**
     * Bot (computer player) makes a decision by selecting the first available empty cell on the board.
     * @param board the current state of the board.
     * @return the decision made by the bot, which is the index of the first empty cell on the board.
     */
    @Override
    public int makeDecision(SquareBoard board, boolean withTag) {
        return getBotDecision(board);
    }

    /**
     * Bot (computer player) makes a decision by selecting the first available empty cell on the board.
     * @param board the current state of the board.
     * @return the decision made by the bot, which is the index of the first empty cell on the board.
     */
    public static int getBotDecision(SquareBoard board) {
        int decision = board.getEmptyCells().get(0);
        return decision;
    }
}
```

#### User
```java
package org.myproject.common.player;

import org.myproject.common.board.SquareBoard;
import org.myproject.common.constant.Message;
import org.myproject.common.io.IOService;


public class User extends Player {
    public User(int player_id, IOService ioService) {
        super(player_id, ioService);
    }

    /**
     * User makes a decision by reading input from the IOService.
     * @param board the current state of the board.
     * @param withTag whether to include a prompt tag in the message
     * @return the decision made by the user, or -1 if the user wants to quit.
     */
    @Override
    public int makeDecision(SquareBoard board, boolean withTag) {
        return getUserDecision(ioService, board, withTag, promptMessage);
    }

    /**
     * User makes a decision by reading input from the IOService.
     * @param ioService the IOService for input/output
     * @param board the current state of the board.
     * @param promptMessage the message to prompt the user for input
     * @return the decision made by the user, or -1 if the user wants to quit.
     */
    public static int getUserDecision(IOService ioService, SquareBoard board, String promptMessage) {
        return getUserDecision(ioService, board, false, promptMessage);
    }

    /**
     * User makes a decision by reading input from the IOService.
     * @param ioService the IOService for input/output
     * @param board the current state of the board.
     * @param withTag whether to include a prompt tag in the message
     * @param promptMessage the message to prompt the user for input
     * @return the decision made by the user, or -1 if the user wants to quit.
     */
    public static int getUserDecision(IOService ioService, SquareBoard board, boolean withTag, String promptMessage) {
        while (true) {
            String input = ioService.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                ioService.println(Message.QUIT_MESSAGE);
                return -1; // User wants to quit
            }
            try {
                int decision = Integer.parseInt(input);
                if (board.isValidCell(decision)) {
                    // If cell id is in valid range and empty.
                    if (board.isCellEmpty(decision))
                        return decision;
                    ioService.println(Message.CELL_ALREADY_OCCUPIED_MESSAGE);
                } else {
                    ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
                }
            } catch (NumberFormatException e) {
                ioService.println(Message.getInvalidInputMessage(board.getTotalCells()));
            } catch (Exception e) {
                ioService.println("An unexpected error occurred: " + e.getMessage());
            }
            // Prompt the user again for input after handling invalid input or exceptions.
            if (withTag) {
                ioService.println(Message.PROMPT_FLAG + promptMessage);
            } else {
                ioService.println(promptMessage);
            }
        }
    }
    
}
```

## sc_stateless_http
