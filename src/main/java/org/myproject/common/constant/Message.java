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
