package tic_tac_toe;

import tic_tac_toe.entities.Game;
import tic_tac_toe.entities.Player;
import tic_tac_toe.enums.Symbol;

public class TicTacToeDemo {
    public static void main(String[] args) {
        Player alice = new Player("Alice", Symbol.X);
        Player bob = new Player("Bob", Symbol.O);

        Game game = new Game(alice, bob, 3);

        System.out.println("========== TIC TAC TOE ==========");

        // Alice (X) completes the top row and wins
//        game.makeMove(0, 0);  // X at (0,0)
//        game.makeMove(1, 0);  // O at (1,0)
//        game.makeMove(0, 1);  // X at (0,1)
//        game.makeMove(1, 1);  // O at (1,1)
//        game.makeMove(0, 2);  // X at (0,2) - Alice wins!

        game.makeMove(0, 0);
        game.makeMove(0, 1);
        game.makeMove(0, 2);
        game.makeMove(1, 0);
        game.makeMove(1, 1);
        game.makeMove(1, 2);
        game.makeMove(2, 0); // Alice winner

        game.printBoard();

        System.out.println("Result: " + game.getStatus());
        Player winner = game.getWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner.getName());
        }
    }
}
