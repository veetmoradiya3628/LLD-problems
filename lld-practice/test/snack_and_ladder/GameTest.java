package snack_and_ladder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snack_and_ladder.entities.BoardEntity;
import snack_and_ladder.entities.Dice;
import snack_and_ladder.entities.Player;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testBuilderFailsWithoutSetup() {
        Game.Builder builder = new Game.Builder();

        Exception exception = assertThrows(IllegalStateException.class, builder::build);

        assertEquals("Board, Players, and Dice must be set.", exception.getMessage());
    }

    @Test
    public void testGameFailsWithOnePlayer() {
        List<String> players = new ArrayList<>();
        players.add("Alice");

        Game game = new Game.Builder()
                .setBoard(100, new ArrayList<BoardEntity>())
                .setPlayers(players)
                .setDice(new Dice(1, 6))
                .build();
        game.play();

        String output = outContent.toString();
        assertTrue(output.contains("Cannot start game. at least 2 players are required."));
    }

    @Test
    void testPlayerInitialState() {
        Player player = new Player("Bob");

        // Assert that the player's name is saved correctly
        assertEquals("Bob", player.getName());

        // Assert that a new player always starts at position 0
        assertEquals(0, player.getPosition());
    }

    @Test
    void testDiceRollBounds() {
        // Assuming your dice class rolls a 6-sided die
        Dice dice = new Dice(1, 6);

        // Roll the dice 50 times and ensure the result is always between 1 and 6
        for (int i = 0; i < 50; i++) {
            int result = dice.roll();
            assertTrue(result >= 1 && result <= 6, "Dice roll should be between 1 and 6, but was " + result);
        }
    }
}