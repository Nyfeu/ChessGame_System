package chess_layer;

import board_layer.Board;
import board_layer.Piece;
import board_layer.Position;
import chess_layer.enums.Color;
import chess_layer.exceptions.ChessException;
import chess_layer.pieces.King;
import chess_layer.pieces.Rook;

public class ChessMatch {

    private Board board;

    public ChessMatch() {
        board = new Board(8,8);
        initialSetup();
    }

    public ChessPiece[][] getPieces() {

        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i = 0; i < board.getRows(); i++) {

            for(int j = 0; j < board.getColumns(); j++) {

                mat[i][j] = (ChessPiece) board.piece(i,j);

            }

        }
        return mat;

    }

    public ChessPiece performChessMove(ChessPosition initialPosition, ChessPosition finalPosition) {

        validateInitialPosition(initialPosition.toPosition());
        Piece capturedPiece = makeMove(initialPosition.toPosition(),finalPosition.toPosition());
        return (ChessPiece) capturedPiece;

    }

    private void validateInitialPosition(Position position) {
        if (!board.thereIsAPiece(position)) throw new ChessException("there isn't piece on position!");
    }

    private Piece makeMove(Position initialPosition, Position finalPosition) {
        Piece p = board.removePiece(initialPosition);
        Piece captured = board.removePiece(finalPosition);
        board.placePiece(p,finalPosition);
        return captured;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }

}
