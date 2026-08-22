package tic_tac_toe.entities;

import org.junit.jupiter.api.*;
import tic_tac_toe.enums.GameStatus;
import tic_tac_toe.enums.Symbol;
import tic_tac_toe.exceptions.InvalidMoveException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameTest {
    private Player player1;
    private Player player2;
    private Game game;

    @BeforeEach
    void setup() {
        player1 = mock(Player.class);
        when(player1.getSymbol()).thenReturn(Symbol.X);

        player2 = mock(Player.class);
        when(player2.getSymbol()).thenReturn(Symbol.O);

        game = new Game(player1, player2, 3);
    }

    @Test
    @Order(1)
    @DisplayName("Testing Game Initialization")
    public void testGameInitialization() {
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
        assertEquals(player1, game.getCurrentPlayer());
        assertNotNull(game.getBoard());
    }

    @Test
    @Order(2)
    @DisplayName("Valid move should switch the current player")
    public void testValidMoveSwitchPlayer(){
        game.makeMove(0, 0);
        assertEquals(player2, game.getCurrentPlayer(), "Player should switch after a valid move");
        assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    void testMoveOnOccupiedCellThrowsException() {
        game.makeMove(0, 0); // Player 1 plays
        InvalidMoveException exception = assertThrows(InvalidMoveException.class, () -> {
            game.makeMove(0, 0); // Player 2 tries the same cell
        });
        assertTrue(exception.getMessage().contains("already occupied"));
    }

    @Test
    void testRowWin() {
        game.makeMove(0, 0); // P1 (X)
        game.makeMove(1, 0); // P2 (O)
        game.makeMove(0, 1); // P1 (X)
        game.makeMove(1, 1); // P2 (O)
        game.makeMove(0, 2); // P1 (X) wins on row 0

        assertEquals(GameStatus.WINNER_X, game.getStatus());
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testColumnWin() {
        game.makeMove(0, 0); // P1 (X)
        game.makeMove(0, 1); // P2 (O)
        game.makeMove(1, 0); // P1 (X)
        game.makeMove(1, 1); // P2 (O)
        game.makeMove(2, 0); // P1 (X) wins on col 0

        assertEquals(GameStatus.WINNER_X, game.getStatus());
        assertEquals(player1, game.getWinner());
    }

    @Test
    void testMainDiagonalWin() {
        game.makeMove(0, 0); // P1 (X)
        game.makeMove(0, 1); // P2 (O)
        game.makeMove(1, 1); // P1 (X)
        game.makeMove(0, 2); // P2 (O)
        game.makeMove(2, 2); // P1 (X) wins on main diagonal

        assertEquals(GameStatus.WINNER_X, game.getStatus());
    }

    @Test
    void testAntiDiagonalWin() {
        game.makeMove(0, 2); // P1 (X)
        game.makeMove(0, 0); // P2 (O)
        game.makeMove(1, 1); // P1 (X)
        game.makeMove(0, 1); // P2 (O)
        game.makeMove(2, 0); // P1 (X) wins on anti-diagonal

        assertEquals(GameStatus.WINNER_X, game.getStatus());
    }

    @Test
    void testDrawCondition() {
        // Simulating a full board with no winner:
        // X O X
        // X O O
        // O X X
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(0, 2); // X
        game.makeMove(1, 1); // O
        game.makeMove(1, 0); // X
        game.makeMove(1, 2); // O
        game.makeMove(2, 1); // X
        game.makeMove(2, 0); // O
        game.makeMove(2, 2); // X

        assertEquals(GameStatus.DRAW, game.getStatus());
        assertNull(game.getWinner());
    }

    @Test
    void testMoveAfterGameOverThrowsException() {
        // P1 wins quickly
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X wins

        InvalidMoveException exception = assertThrows(InvalidMoveException.class, () -> {
            game.makeMove(2, 2); // Try to play after game over
        });

        assertEquals("Game is already over!", exception.getMessage());
    }
}