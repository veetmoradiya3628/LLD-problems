### Tic Tac Toe

#### 1. Requirement Gathering

- Functional Requirement
  - The game is played on 3 x 3 grid
  - Players take turns alternatively
  - Game should detect the winner
  - if Game is draw should detect it and declare it
  - Game should reject invalid moves & inform players
- Non-Functional Requirement
  - should follow ood principles
  - board size should be modular, move history in future etc
  - game logic to be testable 
  - provider clear console output

#### 2. Core Identity
- Symbol - Enum - Cell Values (X or O)
- GameStatus - Enum - Game State - IN_PROGRESS, WINNER_X, WINNER_O, DRAW
- Cell - Data class
- Player - Data class
- Board - core entity to manage board
- Game - core entity for game orchestration

### 3. Design class & relationships
- Symbol - Enum
  - X, O, EMPTY
- GameStatus - Enum
  - IN_PROGRESS, WINNER_X, WINNER_O, DRAW
- Player - Class
  - -String name
  - -Symbol symbol
  - +Player(String name, Symbol symbol)
- Cell - Class
  - -Symbol symbol
  - +Cell()
  - +isEmpty(): boolean
- Board - Class
  - -Cell[][] grid
  - -int size
  - +Board(int size)
  - +placeSymbol(int row, int col, Symbol symbol)
  - +isCellEmpty(int row, int col): boolean
  - +isFull(): boolean
  - +printBoard()
- Game - Class
  - -Board board
  - -Player[] players
  - -int currentPlayerIndex
  - -GameStatus status
  - +Game(Player p1, Player p2, int boardSize)
  - +makeMove(int row, int col)

### 4. Code Impl, Run & Test
- Keep code clean and write good code

### 5. Concurrency & Thread Safety
- In Web version where two player plays on different web browser will convert it to two different request on separate thread so game state and makeMove() can corrupt the game.
- Game is a shared state 
  - The fix is to make makeMove() atomic as a all steps as a single entity by one single thead.

### 6. Extensions

#### 6.1 Variable board size
- Our design already handle this, the board takes a size parameter and using that for all the condition checking.

#### 6.2 AI Opponent
- Add a computer player that makes moves automatically
- We can introduce `MoveStrategy` interface for selecting moves. it controls how a player picks a move.
- SimpleRandomStrategy, MinMaxMoveStrategy

#### 6.3 ScoreBoard
- Observer pattern to notify and subscribe implement using `GameObserver` pattern

#### Design patterns & Principles
- SOC / SRP - for clean class
- Observer - for notifying user / score board
- Strategy - for move strategy