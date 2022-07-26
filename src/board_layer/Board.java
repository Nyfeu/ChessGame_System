package board_layer;

import board_layer.exceptions.BoardException;

public class Board {

    private int rows;
    private int columns;

    private Piece[][] matOfPieces;


    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matOfPieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {

        if(!positionExists(row,column)) throw new BoardException("Invalid position!");

        return matOfPieces[row][column];
    }

    public Piece piece(Position position) {
        return piece(position.getRow(),position.getColumn());
    }

    public void placePiece(Piece piece, Position position) {

        if(thereIsAPiece(position)) throw new BoardException("There's already a piece on position!");

        matOfPieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece removePiece(Position position) {

        if(!positionExists(position.getRow(),position.getColumn())) throw new BoardException("Invalid position!");
        if (piece(position) == null) {
            return null;
        }
        Piece piece = piece(position);
        piece.position = null;
        matOfPieces[position.getRow()][position.getColumn()] = null;
        return piece;
    }


    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {

        if(!positionExists(position.getRow(),position.getColumn())) throw new BoardException("Invalid position!");

        return piece(position) != null;
    }

}
