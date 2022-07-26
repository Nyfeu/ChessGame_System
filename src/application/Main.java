package application;

import chess_layer.ChessMatch;

public class Main {

    public static void main(String[] args) {

        ChessMatch match = new ChessMatch();
        UI.printBoard(match.getPieces());

    }

}