import java.io.*;
import java.util.*;

public class Reversi8x8 {

	public char[][] gameBoard;
	public int turnNum;
	public int tilesPlayed;
	public int consecutiveTurnsSkipped;
	
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
	
	public Reversi8x8() {
		
		// initialize game board
		gameBoard = new char[8][8];
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				gameBoard[i][j] = ' ';
			}
		}
		
		// initial positions for light chips
		gameBoard[row(4)][col('d')] = 'O';
		gameBoard[row(5)][col('e')] = 'O';
		
		// initial positions for dark chips
		gameBoard[row(4)][col('e')] = 'X';
		gameBoard[row(5)][col('d')] = 'X';
	
		turnNum = 0;		// initialize turn count to 0
		tilesPlayed = 0;	// initialize tiles played to 0
	}
	
	// constructor that copies the argument Reversi8x8 object
	// into a new, separate object (to test successive moves on)
	public Reversi8x8(Reversi8x8 sourceState) {
			
		// initialize the game board
		this.gameBoard = new char[8][8];
			
		// copy the gameboard from the source
		// state into the new game state
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
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
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
					
				// for the first or tenth column,
				// print the row numbers for clarity
				if (j == 0 || j == 9) {
						
					// the corners will be blank
					if (i == 0 || i == 9) {
						System.out.print(' ');
						System.out.print(" ");
					}
						
					// the row numbers
					else {
						System.out.print(i);
						System.out.print(" ");
					}
						
				}
					
				// for the first or tenth rows,
				// print the column headers for clarity
				else if (i == 0 || i == 9) {
					System.out.print((char)(97 + j - 1));
					System.out.print(" ");
				}
					
				// for all other rows / columns print the game board values
				else if (gameBoard[i-1][j-1] != 'X' && gameBoard[i-1][j-1] != 'O') {
					System.out.print(' ');
					System.out.print(" ");
				}
				else {
					System.out.print(gameBoard[i-1][j-1]);
					System.out.print(" ");
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
		while (0 <= curRow && curRow < 8 && 0 <= curCol && curCol < 8) {
				
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
			
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
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
			
		// if the number of tiles played is 60,
		// then the board is full and game is over
		if (tilesPlayed >= 60) {
			return true;
		}
			
		// if both players have been skipped for lack
		// of valid moves, then the game is over
		if (consecutiveTurnsSkipped > 1) {
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
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
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
