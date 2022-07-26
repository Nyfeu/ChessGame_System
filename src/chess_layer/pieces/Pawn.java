package chess_layer.pieces;

import board_layer.Board;
import board_layer.Position;
import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.enums.Color;

public class Pawn extends ChessPiece {

    private ChessMatch match;
    public Pawn(Board board, Color color, ChessMatch match) {
        super(board, color);
        this.match = match;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        if(getColor() == Color.WHITE) {
            p.setValues(position.getRow()-1,position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()-2,position.getColumn());
            Position p2 = new Position(position.getRow()-1,position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()-1,position.getColumn()-1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()-1,position.getColumn()+1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // specialmove - enPassant - WHITE
            if(position.getRow() == 3) {
                Position left = new Position(position.getRow(),position.getColumn() - 1);
                if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == match.getEnPassantVulnerable()) {
                    mat[left.getRow()-1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(),position.getColumn() + 1);
                if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == match.getEnPassantVulnerable()) {
                    mat[right.getRow()-1][right.getColumn()] = true;
                }
            }

        }

        if(getColor() == Color.BLACK) {
            p.setValues(position.getRow()+1,position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()+2,position.getColumn());
            Position p2 = new Position(position.getRow()+1,position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()+1,position.getColumn()+1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow()+1,position.getColumn()-1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // specialmove - enPassant - BLACK
            if(position.getRow() == 4) {
                Position left = new Position(position.getRow(),position.getColumn() - 1);
                if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == match.getEnPassantVulnerable()) {
                    mat[left.getRow()+1][left.getColumn()] = true;
                }
                Position right = new Position(position.getRow(),position.getColumn() + 1);
                if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == match.getEnPassantVulnerable()) {
                    mat[right.getRow()+1][right.getColumn()] = true;
                }
            }

        }

        return mat;
    }


}
