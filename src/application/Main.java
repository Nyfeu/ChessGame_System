package application;

import chess_layer.ChessMatch;
import chess_layer.ChessPiece;
import chess_layer.ChessPosition;
import chess_layer.exceptions.ChessException;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(true) {

            try {

                UI.clearScreen();
                UI.printMatch(match, captured);
                System.out.print("\nSource: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = match.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(match.getPieces(),possibleMoves, match.getCurrentPlayer());

                System.out.print("\nTarget: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);

                if(capturedPiece != null) captured.add(capturedPiece);


            } catch (ChessException | InputMismatchException e) {

                System.out.println("\n" + e.getMessage() + "\n\nPress 'Enter' to try again!");
                sc.nextLine();

            }

        }

    }

}