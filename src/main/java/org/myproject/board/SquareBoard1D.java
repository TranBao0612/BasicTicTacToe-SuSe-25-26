package org.myproject.board;

import java.util.Arrays;

public class SquareBoard1D extends SquareBoard {
    private int[] cells;

    // ---------Constructor---------
    public SquareBoard1D(int size, int empty_cell_value) {
        super(size, empty_cell_value);
        this.cells = new int[size*size];
        Arrays.fill(cells, empty_cell_value);
    }

    public SquareBoard1D(int size) {
        this(size, 0);
    }




    // ---------Override methods---------
    @Override
    public int get_value(int cell_id) {
        return cells[cell_id-1];
    }

    @Override
    public void set_value(int cell_id, int player_id) {
        cells[cell_id-1] = player_id;
    }

    @Override
    public void set_empty_value(int value) {
        int old_empty_value = this.EMPTY_CELL_VALUE;
        this.EMPTY_CELL_VALUE = value;
        for(int i = 0; i < cells.length; i++) {
            if (cells[i] == old_empty_value)
                cells[i] = value;
        }
    }

    @Override
    public boolean is_full() {
        for(int cell_value : cells) {
            if (cell_value == EMPTY_CELL_VALUE)
                return false;
        }
        return true;
    }

    @Override
    public String to_string() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++)
                sb.append("| ").append(cells[i*size + j]).append(" ");
            sb.append("|\n");
        }
        return sb.toString();
    }
}
