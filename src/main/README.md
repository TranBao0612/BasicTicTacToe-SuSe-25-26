# TIC-TAC-TOE GAME:

## Package: `org.myproject.game`
### Class `Tictactoe`
- Consist of 2 Player objects and a Board object.
- Public method `game_start()` to start the game:
    + Continuously switch between 2 players
    + Print the board and check state of the game after each turn
    + Print the winner / draw when the game ends.

## Package `org.myproject.board`
### Class `Cell`
- Represent a cell on the board, each has:
    + State: Unoccupied / Occupied by player `player_id`
    + Id: identifier of the cell on the board
- Public method:
    + Getter
    + `checked_by_player(int player_id) throws Exception`: to change the state of cell from unoccupied to occupied by `player_id`, throws Exception if the cell is occupied
    + `is_available()`: whether the cell is unoccupied
    + `toString`: to convert its state to string
### Class `Board`
- Represent a Tic-tac-toe board, consists of 9 `Cell` objects.
- Public method:
    + `available_cells()`: to get list of id of unoccupied cells
    + `is_done()`: return the state of the board (or of the game): not done / draw / `player_id` won
    + `player_choose(int player_id, int cell_id) throws Exception`: to change state of a cell to `player_id`, throws Exception if the `cell_id` is out of range.
    + `print()`: to print the board to console follow by a blank line
    + `toString()`

## Package `org.myproject.player`
### Class `Player`
- Represent players with player_id. Is public to be used, however, its constructor is set to protected to prevent initialize a Player without specific type.
- This is for further expand with more modes: User vs User, Bot vs Bot, ...
- Public method:
    + `move()`: public method, to change the state of the board
    + `make_decision()`: is set to protected to be inherited only
#### Sub-class `User`
- Represent human player
    + `make_decision()` method is overrided to wait for user input
    + `move()` method is overrided to handle invalid user input and to let user re-enter until receive a valid input
#### Sub-class `Bot`
- Represent computer player
    + `make_decision()` method is overrided to make decision with simple algorithm
    + `move()`