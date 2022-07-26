package application;

import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.ChessPosition;
import chess_layer.exceptions.ChessException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(true) {

            try {

                UI.clearScreen();
                UI.printBoard(match.getPieces());
                System.out.print("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = match.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(match.getPieces(),possibleMoves);

                System.out.print("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);

                if(captured != null) captured.add(capturedPiece);


            } catch (ChessException | InputMismatchException e) {

                System.out.println("\n" + e.getMessage() + "\n\nPress 'Enter' to try again!");
                sc.nextLine();

            }

        }

    }

}