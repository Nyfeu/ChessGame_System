package chess_layer;

import board_layer.Board;
import board_layer.Piece;
import chess_layer.enums.Color;

abstract public class ChessPiece extends Piece {

    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
