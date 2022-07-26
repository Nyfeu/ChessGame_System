package application;

import chess_layer.ChessPiece;

public class UI {

    public static void printBoard(ChessPiece[][] pieces) {

        System.out.println();
        for(int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for(int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]);
            }
            System.out.println();
        }

    }

    private static void printPiece(ChessPiece piece) {
        if(piece == null ) {
            System.out.print("-");
        } else {
            System.out.print(piece);
        }
        System.out.print(" ");
    }

}
