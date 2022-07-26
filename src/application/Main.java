package application;

import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.ChessPosition;
import chess_layer.exceptions.ChessException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();

        while(true) {

            try {

                UI.clearScreen();
                UI.printBoard(match.getPieces());
                System.out.print("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);
                System.out.print("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);

            } catch (ChessException | InputMismatchException e) {

                System.out.println("\n" + e.getMessage() + "\n\nPress 'Enter' to try again!");
                sc.nextLine();

            }

        }

    }

}