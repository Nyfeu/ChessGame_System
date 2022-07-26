package application;

import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.ChessPosition;
import chess_layer.enums.Color;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {

    // Colors! ----------------------------------------------------------------
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // ---------------------------------------------------------------------

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printMatch(ChessMatch match) {
        printBoard(match.getPieces(),match);
        System.out.print("\nTurn: " + match.getTurn());
        System.out.print("\nWaiting player: " + match.getCurrentPlayer());
    }

    public static void printBoard(ChessPiece[][] pieces, ChessMatch match) {

        System.out.println();
        for(int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for(int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j],false, match);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");

    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves, ChessMatch match) {

        System.out.println();
        for(int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for(int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j],possibleMoves[i][j], match);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");

    }

    private static void printPiece(ChessPiece piece, boolean background, ChessMatch match) {
        if(background) {
            if(match.getCurrentPlayer() == Color.WHITE) System.out.print(ANSI_BLUE_BACKGROUND);
            else System.out.print(ANSI_YELLOW_BACKGROUND);
        }
        if(piece == null ) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_BLUE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    public static ChessPosition readChessPosition(Scanner sc) {
        try {

            String s = sc.nextLine().trim();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column,row);

        } catch (RuntimeException e) {

            throw new InputMismatchException("Invalid chess position!");

        }
    }

}
