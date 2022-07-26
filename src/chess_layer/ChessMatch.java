package chess_layer;

import board_layer.Board;
import board_layer.Piece;
import board_layer.Position;
import chess_layer.enums.Color;
import chess_layer.exceptions.ChessException;
import chess_layer.pieces.Bishop;
import chess_layer.pieces.King;
import chess_layer.pieces.Pawn;
import chess_layer.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private boolean check;
    private boolean Checkmate;

    public int getTurn() {
        return turn;
    }

    public boolean getCheckmate() {
        return Checkmate;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessMatch() {
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
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

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public boolean[][] possibleMoves(ChessPosition initialPosition) {
        Position position = initialPosition.toPosition();
        validateInitialPosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition initialPosition, ChessPosition finalPosition) {

        validateInitialPosition(initialPosition.toPosition());
        validateFinalPosition(initialPosition.toPosition(),finalPosition.toPosition());
        Piece capturedPiece = makeMove(initialPosition.toPosition(),finalPosition.toPosition());

        if(testCheck(currentPlayer)) {
            undoMove(initialPosition,finalPosition,capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = (testCheck(opponent(currentPlayer)));

        if(testCheckmate(opponent(currentPlayer))) {
            Checkmate = true;
        } else {
            nextTurn();
        }
        return (ChessPiece) capturedPiece;

    }

    public boolean getCheck() {
        return check;
    }

    private void validateInitialPosition(Position position) {
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) throw new ChessException("The chosen piece isn't yours!");
        if (!board.thereIsAPiece(position)) throw new ChessException("There isn't a piece on position!");
        if (!board.piece(position).isThereAnyPossibleMove()) throw new ChessException("There's no possible movement for the chosen piece!");
    }

    private void validateFinalPosition(Position initialPosition ,Position finalPosition) {
        if (!board.piece(initialPosition).possibleMove(finalPosition)) throw new ChessException("The chosen piece can't move to target position!");
    }

    private Piece makeMove(Position initialPosition, Position finalPosition) {
        ChessPiece p = (ChessPiece) board.removePiece(initialPosition);
        p.increaseMoveCount();
        Piece captured = board.removePiece(finalPosition);
        if(captured != null) {
            piecesOnTheBoard.remove(captured);
            capturedPieces.add(captured);
        }
        board.placePiece(p,finalPosition);
        return captured;
    }

    private void undoMove(ChessPosition initialPosition, ChessPosition finalPosition, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(finalPosition.toPosition());
        p.decreaseMoveCount();
        board.placePiece(p,initialPosition.toPosition());
        if(capturedPiece != null) {
            piecesOnTheBoard.add(capturedPiece);
            capturedPieces.remove(capturedPiece);
            board.placePiece(capturedPiece,finalPosition.toPosition());
        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There's no " + color + " king on the board!");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).toList();
        for(Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }
    private boolean testCheckmate(Color color) {

        if(!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();
        for(Piece x : list) {
            boolean[][] mat = x.possibleMoves();
            for(int i = 0; i < board.getRows(); i++) {
                for(int j = 0; j < board.getColumns(); j++) {
                    if(mat[i][j]) {
                        ChessPosition source = ((ChessPiece)x).getChessPosition();
                        ChessPosition target = ChessPosition.fromPosition(new Position(i,j));
                        Piece captured = makeMove(source.toPosition(),target.toPosition());
                        boolean testeCheck = testCheck(color);
                        undoMove(source,target,captured);
                        if(!testeCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;

    }


    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {

        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));

    }

}
