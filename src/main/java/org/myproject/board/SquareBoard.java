package org.myproject.board;

public abstract class SquareBoard {
    protected int EMPTY_CELL_VALUE;
    protected int size;

    public SquareBoard(int size, int empty_cell_value) {
        this.size = size;
        this.EMPTY_CELL_VALUE = empty_cell_value;
    }

    public abstract int get_value(int cell_id);
    public abstract void set_value(int cell_id, int player_id);
    public abstract void set_empty_value(int value);
    public abstract boolean is_full();
    public abstract String to_string();

    public boolean is_empty(int cell_id) {
        return get_value(cell_id) == EMPTY_CELL_VALUE;
    }

    public boolean is_valid_cell_id(int cell_id) {
        return cell_id > 0 && cell_id <= size*size;
    }

    public void print() {
        System.out.println(to_string());
    }
}