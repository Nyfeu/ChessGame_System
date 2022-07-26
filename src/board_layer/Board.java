package board_layer;

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
        return matOfPieces[row][column];
    }

    public Piece piece(Position position) {
        return matOfPieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        matOfPieces[position.getRow()][position.getColumn()] = piece;
    }

    public Piece placePiece(Position position) {
        Piece piece = matOfPieces[position.getRow()][position.getColumn()];
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
        return matOfPieces[position.getRow()][position.getColumn()] != null;
    }

}
