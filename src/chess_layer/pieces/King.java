package chess_layer.pieces;

import board_layer.Board;
import board_layer.Position;
import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.enums.Color;

public class King extends ChessPiece {

    private ChessMatch match;

    public King(Board board, Color color, ChessMatch match) {
        super(board, color); this.match = match;
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        // above:

        p.setValues(position.getRow()-1,position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        // below:

        p.setValues(position.getRow()+1,position.getColumn());
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        // left:

        p.setValues(position.getRow(),position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        // right:

        p.setValues(position.getRow(),position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        // diagonal:

        p.setValues(position.getRow()-1,position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow()+1,position.getColumn()-1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow()+1,position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;

        p.setValues(position.getRow()-1,position.getColumn()+1);
        if (getBoard().positionExists(p) && canMove(p)) mat[p.getRow()][p.getColumn()] = true;


        if(getMoveCount() == 0 && !match.getCheck()) {
            Position posT1 = new Position(position.getRow(), position.getColumn()+3);
            if(testRookCastling(posT1)){
                Position p1 = new Position(position.getRow(), position.getColumn()+1);
                Position p2 = new Position(position.getRow(), position.getColumn()+2);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRow()][position.getColumn()+2] = true;
                }
            }
            Position posT2 = new Position(position.getRow(), position.getColumn()-4);
            if(testRookCastling(posT2)){
                Position p1 = new Position(position.getRow(), position.getColumn()-1);
                Position p2 = new Position(position.getRow(), position.getColumn()-2);
                Position p3 = new Position(position.getRow(), position.getColumn()-3);
                if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
                    mat[position.getRow()][position.getColumn()-2] = true;
                }
            }
        }

        return mat;

    }


}
