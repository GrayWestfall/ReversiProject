import java.io.*;
import java.util.*;

// class that contains the game state
// for a 4x4 Reversi board
public class Reversi4x4 {
	
	public char[][] gameBoard;				// matrix that contains the game board
	public int turnNum;						// tracks how many turns have passed to see which player's turn is next
	public int tilesPlayed;					// tracks how many tiles have been placed on the board (separate from turns passed since turns can be skipped)
	public int consecutiveTurnsSkipped;		// tracks how many consecutive turns have been skipped to determine if the game needs to end

	
	// function that converts the given
	// row number into an array index
	public static int row(int rowNum) {
		return rowNum - 1;
	}
	
	// function that converts the given
	// column number into an array index
	public static int col(char colName) {
		return ((int) colName) - 97;
	}
	
	// constructor for the Reversi4x4 class
	public Reversi4x4() {
		
		// initialize game board
		gameBoard = new char[4][4];
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				gameBoard[i][j] = ' ';
			}
		}
		
		// initial positions for light chips
		gameBoard[row(2)][col('b')] = 'O';
		gameBoard[row(3)][col('c')] = 'O';
		
		// initial positions for dark chips
		gameBoard[row(3)][col('b')] = 'X';
		gameBoard[row(2)][col('c')] = 'X';
	
		turnNum = 0;		// initialize turn count to 0
		tilesPlayed = 0;	// initialize tiles played to 0
	}
	
	// constructor that copies the argument Reversi4x4 object
	// into a new, separate object (to test successive moves on)
	public Reversi4x4(Reversi4x4 sourceState) {
		
		// initialize the game board
		this.gameBoard = new char[4][4];
		
		// copy the gameboard from the source
		// state into the new game state
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				this.gameBoard[i][j] = sourceState.gameBoard[i][j];
			}
		}
		
		// copy turn number and tiles played into the new state
		this.turnNum = sourceState.turnNum;
		this.tilesPlayed = sourceState.tilesPlayed;
		

	}

	// function to print the current
	// state of the game board
	public void printBoard() {
		
		// pad game board 1 column and 1 row on either side 
		for (int i = 0; i < 6; ++i) {
			for (int j = 0; j < 6; ++j) {
				
				// for the first or sixth column,
				// print the row numbers for clarity
				if (j == 0 || j == 5) {
					
					// the corners will be blank
					if (i == 0 || i == 5) {
						System.out.print(' ');
						System.out.print("  ");
					}
					
					// the row numbers
					else {
						System.out.print(i);
						System.out.print("  ");
					}
					
				}
				
				// for the first or sixth rows,
				// print the column headers for clarity
				else if (i == 0 || i == 5) {
					System.out.print((char)(97 + j - 1));
					System.out.print("  ");
				}
				
				// for all other rows / columns print the game board values
				else if (gameBoard[i-1][j-1] != 'X' && gameBoard[i-1][j-1] != 'O') {
					System.out.print(' ');
					System.out.print("  ");
				}
				else {
					System.out.print(gameBoard[i-1][j-1]);
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
	
	// returns the number of flips an individual move 
	// would cause (to help confirm it is a valid move)
	public int validateMove(int row, int column) {

		// if that spot on the board isn't empty, return 0
		if (gameBoard[row][column] != ' ') {
			return 0;
		}
		
		char playerChar;			// variable to store the player's character
		char opponentChar;			// variable to store the opponent's character
		
		
		// if the turn is even, it's dark's turn
		if (turnNum % 2 == 0) {
			playerChar = 'X';
			opponentChar = 'O';
		}
		// if the turn is odd, it's light's turn
		else {
			playerChar = 'O';
			opponentChar = 'X';
		}
		
		int numTurned = 0;			// variable to track how many opponent chips are flipped
		
		
		// check each of the 8 directions in which one or
		// more of the opponent's chips could be flipped
		
		
		// left and upward
		int curRow = row - 1;
		int curCol = column - 1;
		
		// while still on the board
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			// if an empty spot is reached, stop the loop
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			
			// if another of the current player's tokens is reached,
			// increment the number of chips flipped for each opponent
			// chip in between to the current player's character
			// by moving back to the current move's position
			else if (gameBoard[curRow][curCol] == playerChar) {
				
				curRow += 1;
				curCol += 1;
				while (curRow < row && curCol < column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow += 1;
					curCol += 1;
				}
				break;
			}
			curRow -= 1;
			curCol -= 1;
		}
		
		// left
		curRow = row;
		curCol = column - 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curCol += 1;
				while (curCol < column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curCol += 1;
				}
				break;
			}
			curCol -= 1;
		}
		
		// left and downward
		curRow = row + 1;
		curCol = column - 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				curCol += 1;
				while (curRow > row && curCol < column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow -= 1;
					curCol += 1;
				}
				break;
			}
			curRow += 1;
			curCol -= 1;
			
		}
		
		// downward
		curRow = row + 1;
		curCol = column;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				while (curRow > row) {
					
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow -= 1;
				}
				break;
			}
			curRow += 1;
		}
		
		// right and downward
		curRow = row + 1;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				curCol -= 1;
				while (curRow > row && curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow -= 1;
					curCol -= 1;
				}
				break;
			}
			curRow += 1;
			curCol += 1;
		}
		
		// right
		curRow = row;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curCol -= 1;
				while (curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curCol -= 1;
				}
				break;
			}
			curCol += 1;
		}
		
		// right and upward
		curRow = row - 1;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow += 1;
				curCol -= 1;
				while (curRow < row && curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow += 1;
					curCol -= 1;
				}
				break;
			}
			curRow -= 1;
			curCol += 1;
		}
		
		// upward
		curRow = row - 1;
		curCol = column;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow += 1;
				while (curRow < row) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						numTurned += 1;
					}
					curRow += 1;
				}
				break;
			}
			curRow -= 1;
		}
		
		return numTurned;
		
	}
	
	// function that actually executes the move at the
	// given row and column; same implementation as validateMove()
	// but actually modifies the game state
	public int playMove(int row, int column) {
		
		if (gameBoard[row][column] != ' ') {
			return 0;
		}
		
		char playerChar;
		char opponentChar;
		
		if (turnNum % 2 == 0) {
			playerChar = 'X';
			opponentChar = 'O';
		}
		else {
			playerChar = 'O';
			opponentChar = 'X';
		}
		
		int numTurned = 0;
		
		int curRow = row - 1;
		int curCol = column - 1;
		
		// left and upward
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow += 1;
				curCol += 1;
				while (curRow < row && curCol < column) {
					
					// flips the opponent's chips to the player's chip
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow += 1;
					curCol += 1;
				}
				break;
			}
			curRow -= 1;
			curCol -= 1;
		}
		
		// left
		curRow = row;
		curCol = column - 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curCol += 1;
				while (curCol < column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curCol += 1;
				}
				break;
			}
			curCol -= 1;
		}
		
		// left and downward
		curRow = row + 1;
		curCol = column - 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				curCol += 1;
				while (curRow > row && curCol < column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow -= 1;
					curCol += 1;
				}
				break;
			}
			curRow += 1;
			curCol -= 1;
			
		}
		
		// downward
		curRow = row + 1;
		curCol = column;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				while (curRow > row) {
					
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow -= 1;
				}
				break;
			}
			curRow += 1;
		}
		
		// right and downward
		curRow = row + 1;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow -= 1;
				curCol -= 1;
				while (curRow > row && curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow -= 1;
					curCol -= 1;
				}
				break;
			}
			curRow += 1;
			curCol += 1;
		}
		
		// right
		curRow = row;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curCol -= 1;
				while (curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curCol -= 1;
				}
				break;
			}
			curCol += 1;
		}
		
		// right and upward
		curRow = row - 1;
		curCol = column + 1;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow += 1;
				curCol -= 1;
				while (curRow < row && curCol > column) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow += 1;
					curCol -= 1;
				}
				break;
			}
			curRow -= 1;
			curCol += 1;
		}
		
		// upward
		curRow = row - 1;
		curCol = column;
		while (0 <= curRow && curRow < 4 && 0 <= curCol && curCol < 4) {
			
			if (gameBoard[curRow][curCol] == ' ') {
				break;
			}
			else if (gameBoard[curRow][curCol] == playerChar) {
				curRow += 1;
				while (curRow < row) {
					if (gameBoard[curRow][curCol] == opponentChar) {
						gameBoard[curRow][curCol] = playerChar;
						numTurned += 1;
					}
					curRow += 1;
				}
				break;
			}
			curRow -= 1;
		}
		
		// play the tile and increment the turn number
		// and number of tiles played only if the 
		// number flipped is more than none
		if (numTurned > 0) {
			gameBoard[row][column] = playerChar;
			turnNum += 1;
			tilesPlayed += 1;
		}
		return numTurned;
	}
	
	// check all positions on the
	// board for possible moves
	public int checkForMoves() {
		int validMoves = 0;
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				if (validateMove(i, j) > 0) {
					validMoves += 1;
				}
			}
		}
		
		// if there are no valid moves, skip
		// the current turn and allow the 
		// other player to move
		if (validMoves == 0) {
			turnNum += 1;
			consecutiveTurnsSkipped += 1;
		}
		// if valid moves are available,
		// reset the turns skipped counter
		else {
			consecutiveTurnsSkipped = 0;
		}
		// return the number of valid moves
		return validMoves;
	}
	
	// checks if the game has ended
	public boolean gameOver() {
		
		// if the number of tiles played is 12,
		// then the board is full and game is over
		if (tilesPlayed >= 12) {
			//System.out.println("Board is full, game ending...");
			return true;
		}
		
		// if both players have been skipped for lack
		// of valid moves, then the game is over
		if (consecutiveTurnsSkipped > 1) {
			//System.out.println("No valid moves remaining on either side, game ending...");
			return true;
		}
		
		// if neither of those situations
		// arises, the game is still ongoing
		return false;

	}
	
	// checks who the winner of the game is
	// RETURN VALS:
	//  1:	Dark wins
	// -1: 	Light wins
	//  0:	Draw
	public int checkWinner() {
		
		// counts for each player's tiles
		int darkCount = 0;
		int lightCount = 0;
		
		// iterate through the game board
		// and count the number of tiles
		// for each of the players
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				if (gameBoard[i][j] == 'X') {
					darkCount += 1;
				}
				else if (gameBoard[i][j] == 'O') {
						lightCount += 1;
				}
			}
		}
		
		// return the correct value based
		// on whose tile count is higher
		if (darkCount > lightCount) {
			return 1;
		}
		else if (lightCount > darkCount) {
			return -1;
		}
		else {
			return 0;
		}
		
	}


}
