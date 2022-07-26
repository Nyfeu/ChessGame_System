package chess_layer;

import board_layer.Position;
import chess_layer.exceptions.ChessException;

public class ChessPosition {

    private char column;
    private int row;

    public ChessPosition(char column, int row) {

        if(column < 'a' || column > 'h' || row < 1 || row > 8) throw new ChessException("invalid position!");

        this.column = column;
        this.row = row;
    }

    protected Position toPosition() {
        return new Position(8-row,column-'a');
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char)('a' + position.getColumn()),8- position.getRow());
    }


    @Override
    public String toString() {
        return "" + column + row;
    }

}
