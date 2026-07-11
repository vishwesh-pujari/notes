// ═══════════════════════════════════════════════════════════════════════════
// REQUIREMENTS
//
// Example (Tic Tac Toe):
//   1. Two players alternate placing X and O on a 3x3 grid.
//   2. A player wins by completing a row, column, or diagonal.
//   Out of Scope: UI, AI opponent, networking
// ═══════════════════════════════════════════════════════════════════════════

/*
    REQUIREMENTS

    Connect Four
    
    1. Board needs to be of 6 rows and 7 columns. No configuration required.
    2. There need to be two players.
    3. Players will take turns to play the game.
    4. The first one to connect the same color 4 times in the row/col/diag wins.
    5. The system will assign colors to the players.
    6. Each player will just choose a column to place the disk, the disk will automatically be assigned in the lowest available row in that column.
    7. If all the cells in the board are full with no winner, then it's a draw and the game should stop.
    
    Error Handling:
    1. Invalid column input should throw an error (column < 0 || column >= 7)
    2. If user tries to add a disk in a column which is full, it should throw an error and force user to pick an available column.

    Out of Scope:
    1. Undo / Redo for the moves.
    2. Player statistics / game states don't need to be persisted.

*/


// ═══════════════════════════════════════════════════════════════════════════
// ENTITIES & RELATIONSHIPS
//
// Example (Tic Tac Toe):
//   Game, Board, Player
// ═══════════════════════════════════════════════════════════════════════════

/*
    Entities:

    Board -> State[][]
    Player
    State -> RED / YELLOW / UNOCCUPIED
    Game -> Board, Player1, Player2

*/


// ═══════════════════════════════════════════════════════════════════════════
// CLASS DESIGN
//
// Example (Tic Tac Toe):
//   class Game:
//     - board: Board
//     - currentPlayer: Player
//     + makeMove(row, col) -> bool
// ═══════════════════════════════════════════════════════════════════════════

enum State {
    RED,
    YELLOW,
    UNOCCUPIED;
}

class Board:
    - State[6][7] grid

    + placeDisk(int col) -> return row
    + isFourAdjacent(int col, int row) -> checks if (row, col) placement forms any 4-adjacent structure of the same state
    + isFull()

class Player:
    - State color;

    + State getColor()

enum GameState {
    IN_PROGRESS,
    WIN,
    DRAW
}

class Game:
    - Board board;
    - Player[] players;
    - int currentPlayer;
    - GameState gameState

    + makeMove(int col)


// ═══════════════════════════════════════════════════════════════════════════
// IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════

class Board {
    private State[][] grid;
    int x; // connect x

    public Board(int row, int col, int connectX) {
        grid = new State[row][col];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
                grid[i][j] = UNOCCUPIED;
        x = connectX;
    }

    public boolean isFourAdjacent(int col, int row) {
        // up-down
        return helper(col, row, new int[]{0, 1}, new int[]{0, -1}) // up-down
            || helper(col, row, new int[]{-1, 0}, new int[]{1, 0}) // left-right
            || helper(col, row, new int[]{-1, -1}, new int[]{1, 1}) // diag1
            || helper(col, row, new int[]{1, -1}, new int[]{-1, 1}); // diag2
    }

    private boolean helper(int col, int row, int[] firstDir, int[] secondDir) {
        State state = grid[row][col];
        int firstCnt = getCountSameState(col, row, firstDir, state);
        int secondCnt = getCountSameState(col, row, secondDir, state);
        if (firstCnt + secondCnt + 1 == x)
            return true;
        return false;
    }

    private int getCountSameState(int col, int row, int[] dir, State state) {
        int i = row, j = col, cnt = 0;
        
        for (i += dir[0], j += dir[1]; !exceedsBounds(i, j) && grid[i][j] == state; i += dir[0], j += dir[1])
            cnt++;
        return cnt;
    }

    private boolean exceedsBounds(int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length)
            return true;
        return false;
    }

    public boolean exceedsCol(int j) {
        return j < 0 || j >= grid[0].length;
    }

    public int placeDisk(int col, State state) {
        int row = grid[0].length - 1;
        for (; row >= 0; row--) {
            if (grid[row][col] == UNOCCUPIED) {
                grid[row][col] = state;
                break;
            }
        }
        return row; // returns -1 if col is full
    }
}

class Player (
    private State color;
    private abstract int getColChoice(Board board);

    public Player(State color) {
        this.color = color;
    }

    public State getColor() {
        return color;
    }
)

class HumanPlayer extends Player {
    public int getColChoice(Board board) {
        // some implementation
    }
}

class AIPlayer extends Player {
    public int getColChoice(Board board) {
        // some implementation
    }
}

class Game {
    private Board board;
    private Player[] players;
    int currentPlayer;
    GameState gameState;

    public Game(int rows, int cols, int connectX) {
        this.board = new Board(rows, cols, connectX);
        this.players = new Player[2];
        players[0] = new HumanPlayer(RED);
        players[1] = new AIPlayer(YELLOW);
        currentPlayer = 0;

        gameState = IN_PROGRESS;
    }

    public int makeMove() {
        int col = getColChoice();
        updateState(col);
    }
    
    private int getColChoice() {
        return players[currentPlayer].getColChoice(board);
    }

    private synchronized void updateState(int col) {
        if (gameState != IN_PROGRESS) {
            IO.println("Game has ended with state " + gameState);
            return;
        }

        if (board.exceedsCol(col)) {
            IO.println("Invalid col number " + col);
            return;
        }

        int row = board.placeDisk(col, players[currentPlayer].getColor());
        if (row == -1) {
            IO.println("Col " + col + " is full");
            return;
        }

        if (board.isFourAdjacent(col, row)) {
            IO.println("Player " + (currentPlayer + 1) + " Wins!");
            this.gameState = WIN;
            return;
        }

        if (board.isFull()) {
            IO.println("Game is Draw.")
            gameState = DRAW;
            return;
        }

        currentPlayer = (currentPlayer + 1) % players.length;
    }
}


// ═══════════════════════════════════════════════════════════════════════════
// EXTENSIBILITY
// ═══════════════════════════════════════════════════════════════════════════

