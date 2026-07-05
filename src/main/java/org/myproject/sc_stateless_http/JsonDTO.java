package org.myproject.sc_stateless_http;

public class JsonDTO {
    public static class StartRequest {
        public Integer turn;
    }
    public static class MoveRequest {
        public Integer turn;
        public String board_msg;
        public Integer move;
    }

    public static class StartResponse {
        public String welcome_message;
        public String init_board_msg;
        public Integer bot_move = null;
        public String new_board_msg = null;
    }
    public static class MoveResponse {
        public Integer winner_id = null;
        public Boolean end_status = false;
        public String user_move_board_msg;
        public Integer bot_move = null;
        public String bot_move_board_msg = null;
    }
}
