package chess_layer;

import board_layer.Board;
import board_layer.Piece;
import board_layer.Position;
import chess_layer.enums.Color;
import chess_layer.exceptions.ChessException;
import chess_layer.pieces.*;

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
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheckmate() {
        return Checkmate;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece getPromoted() {
        return promoted;
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

        ChessPiece movedPiece = (ChessPiece)board.piece(finalPosition.toPosition());

        // #specialmove - promotion
        promoted = null;
        if(movedPiece instanceof Pawn) {
            if((movedPiece.getColor() == Color.WHITE && finalPosition.toPosition().getRow() == 0) || (movedPiece.getColor() == Color.BLACK && finalPosition.toPosition().getRow() == 7)) {
                promoted = (ChessPiece) board.piece(finalPosition.toPosition());
                promoted = replacePromotedPiece("Q");
            }
        }


        check = (testCheck(opponent(currentPlayer)));

        if(testCheckmate(opponent(currentPlayer))) {
            Checkmate = true;
        } else {
            nextTurn();
        }

        if(movedPiece instanceof Pawn && (finalPosition.toPosition().getRow() == initialPosition.toPosition().getRow()-2) || (finalPosition.toPosition().getRow() == initialPosition.toPosition().getRow()+2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;

    }

    public ChessPiece replacePromotedPiece(String type) {
        if(promoted == null) {
            throw new ChessException("There is no piece to be promoted!");
        }
        if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type,promoted.getColor());

        board.placePiece(newPiece,pos);
        piecesOnTheBoard.add(newPiece);

        check = testCheck(currentPlayer);
        Checkmate = testCheckmate(currentPlayer);

        return newPiece;

    }

    private ChessPiece newPiece(String type, Color color) {
        if(type.equals("B")) return new Bishop(board,color);
        if(type.equals("N")) return new Knight(board,color);
        if(type.equals("Q")) return new Queen(board,color);
        return new Rook(board,color);
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

        // #specialmove castling kingside rook
        if (p instanceof King && finalPosition.getColumn() == initialPosition.getColumn() + 2) {
            Position sourceT = new Position(initialPosition.getRow(), initialPosition.getColumn() + 3);
            Position targetT = new Position(initialPosition.getRow(), initialPosition.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && finalPosition.getColumn() == initialPosition.getColumn() - 2) {
            Position sourceT = new Position(initialPosition.getRow(), initialPosition.getColumn() - 4);
            Position targetT = new Position(initialPosition.getRow(), initialPosition.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove enPassant
        if(p instanceof Pawn) {
            if(initialPosition.getColumn() != finalPosition.getColumn() && captured == null) {
                Position pawnPosition;
                if(p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(finalPosition.getRow()+1,finalPosition.getColumn());
                } else {
                    pawnPosition = new Position(finalPosition.getRow()-1,finalPosition.getColumn());
                }
                captured = board.removePiece(pawnPosition);
                capturedPieces.add(captured);
                piecesOnTheBoard.remove(captured);
            }
        }

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
        // #specialmove castling kingside rook
        if (p instanceof King && finalPosition.getColumn() == initialPosition.getColumn() + 2) {
            Position sourceT = new Position(initialPosition.getRow(), initialPosition.getColumn() + 3);
            Position targetT = new Position(initialPosition.getRow(), initialPosition.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && finalPosition.getColumn() == initialPosition.getColumn() - 2) {
            Position sourceT = new Position(initialPosition.getRow(), initialPosition.getColumn() - 4);
            Position targetT = new Position(initialPosition.getRow(), initialPosition.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #specialmove enPassant
        if(p instanceof Pawn) {
            if(initialPosition.getColumn() != finalPosition.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(finalPosition.toPosition());
                Position pawnPosition;
                if(p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3,finalPosition.getColumn());
                } else {
                    pawnPosition = new Position(4,finalPosition.getColumn());
                }
                board.placePiece(pawn,pawnPosition);

                capturedPiece = board.removePiece(pawnPosition);
            }
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
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));

    }

}
