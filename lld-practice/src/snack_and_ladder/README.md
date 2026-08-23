### Snack And Ladder  

#### 1. Requirement Gathering  
- Functional Requirement 
  - Game is played on standard 10 * 10 board with 100 number of cells
  - Support configuration of snacks and ladders with flexible start and end positions
  - Allow multiple players (minimum two) with turn rotation in round-robin order
  - Simulate dice rolls with random values between 1 and 6. A player gets an extra turn if they roll a 6
  - Three 6s in a row forfeits the turn, sending the player back to the position they started the turn from
  - A player must roll the exact number to land on cell 100 and win the game
  - Multiple players can occupy the same cell without interaction.
- Non-Functional Requirement
  - Modularity: The system should follow object-oriented principles with clean separation between components
  - Extensibility: The design should allow future enhancements such as custom board sizes or different types of dice
  - Maintainability: The codebase should be clean, readable, and easy to extend
  - User Feedback: The system should provide clear console output after each turn, indicating player moves, dice rolls, snake or ladder interactions, and current positions.

#### 2. Core Identity
- Board - class
- Snack - class - start point > end point
- Ladder - class - start point < end point
  - Snack & Ladder shares few properties so abstract class called `BoardEntity` makes sense. validation rules dependes on the implementation while abstract class (common class) shares common property.
- Player - class - name & current position on the board
- Dice - class 
- Game - class - coordinates the game play
  - has board, player, dice, status
- GameStatus - enum - NOT_STARTED, RUNNING, FINISHED

#### 3. Design class & relationships
- GameStatus - ENUM
  - NOT_STARTED, RUNNING, FINISHED
- Player - data class
  - -String name
  - -int position
  - +Player(String name)
- BoardEntity - abstract class
  - -int start
  - -int end
  - +BoardEntity(int start, int end)
- Snack - class
  - validates start <= end
- Ladder - class
  - validates start >= end
- Dice - class
  - -int minValue
  - -int maxValue
  - +Dice(int minValue, int maxValue)
  - +roll() : int
- Board - class
  - int size
  - Map<Integer, Integer> snakesAndLadders
  - +Board(int size, List<BoardEntity> entities)
  - +getFinalPosition(int position): int
- Game - class
  - Main orchestrator that coordinates all game elements
  - -Board board
  - -Queue<Player> players
  - -Dice dice
  - -GameStatus status
  - -Player winner
  - +construtor() as builder as this class has multiple parameters
  - +play()
  - -takeTurn(Player player)
- Facade pattern - Game as Controller

#### 4. Code Impl, Run & Test
- Keep code clean and write good code

#### 5. Concurrency & Thread Safety
- TODO...

#### 6. Extensions 
##### 6.1 Configurable Dice Count
- Let player roll two dice per turn and grant an extra turn when both dice shows the same value
  - we wrap a dice with diceRoller and call the orchestration logic for N number of dice from this class & game uses diceRoller instead of dice directly.

##### 6.2 Configurable board size and placement strategy
- The Board constructor already takes a size and a list of BoardEntity objects, so a 10x10 board is just one configuration. To vary how snakes and ladders are placed, we introduce a BoardSetupStrategy that produces the entity list for a given size. A fixed strategy returns a hand-authored layout, while a random strategy generates valid snakes and ladders. The Game.Builder.setBoard step calls the strategy and passes the result to the existing Board constructor.

#### Design patterns & Principles
- Builder
- Strategy
- OCP / SRP
- Orchestrator 

#### Open issues & Enhancements
- TODO...