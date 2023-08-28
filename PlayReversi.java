import java.util.Scanner;
import java.io.*;

public class PlayReversi {
	
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
	
	// function that converts given row index
	// into the visual board's row number
	public static int reverseRow(int rowIndex) {
		return rowIndex + 1;
	}
	
	// function that converts the given column index
	// into the visual board's column letter
	public static char reverseCol(int colIndex) {
		return (char) (colIndex + 97);
	}

	// function to build the subtree under the
	// node passed in as an argument
	public static void buildReversiSubtree(ReversiNode curNode) {
		
		int validMoves = 0;
		
		// check for valid moves given the current state and turn
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				if (curNode.gameState.validateMove(i, j) > 0) {
					
					// if the current grid position is a valid
					// move, then create a child node under the current
					// state that assumes the player played at that position
					validMoves += 1;
					
					curNode.insertChild(i, j);
					curNode.leftChild.gameState.playMove(i, j);
					curNode.leftChild.gameState.consecutiveTurnsSkipped = 0;
					
					// continue building the subtree under that node
					buildReversiSubtree(curNode.leftChild);
				}
			}
		}
		
		// if there are no valid moves, check for special cases
		if (validMoves == 0) {
			
			// if the board is full, it's a terminal node
			if (curNode.gameState.tilesPlayed >= 12) {
				return;
			}
			// if there are no possible moves left in the game, it's a terminal node
			else if (curNode.gameState.consecutiveTurnsSkipped >= 1) {
				return;
			}
			// otherwise increment the consecutiveTurnsSkipped variable
			// and continue building the subtree under that node
			else {
				curNode.insertChild(-1, -1);
				curNode.leftChild.gameState.turnNum += 1;
				curNode.leftChild.gameState.consecutiveTurnsSkipped += 1;
				buildReversiSubtree(curNode.leftChild);
			}
				                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		}
			
			
	}

	// minimax function used for 4x4 Reversi computer player
	public static int Minimax(ReversiNode curNode) {

		// if the game is over, return the utility of the node
		if (curNode.gameState.gameOver()) {
			
			return curNode.gameState.checkWinner();
		}
		
		// for player Dark (X), the goal is to maximize the value...
		else if (curNode.gameState.turnNum % 2 == 0) {
			
			int maxVal = -10;
			
			// find the maximum Minimax value of the children and return
			// that value as the current node's Minimax value
			for (ReversiNode childNode = curNode.leftChild; childNode != null; childNode = childNode.rightSibling) {
				
				int childVal = Minimax(childNode);

				if (childVal >= maxVal) {
					maxVal = childVal;
				}
			}
			
			return maxVal;
			
		}
		
		// for player Light (O), the goal is to minimize the value...
		else {
			
			int minVal = 10;
			
			// find the minimum Minimax value of the children and return
			// that value as the current node's Minimax value
			for (ReversiNode childNode = curNode.leftChild; childNode != null; childNode = childNode.rightSibling) {
				
				int childVal = Minimax(childNode);
				
				if (childVal <= minVal) {
					minVal = childVal;
				}
			}
			
			return minVal;
		}
		 
	}
	
	// function that employs the traditional minimax algorithm
	// as implemented above to help the computer player choose a move
	public static void chooseMove(Reversi4x4 game, ReversiNode curState) {
		
		// store the destination node so we can find what move to take later
		ReversiNode destinationNode = new ReversiNode(curState.gameState);
		int maxVal = -10;
		int minVal = 10;
		
		// same logic as the Minimax algorithm, just
		// keeping in mind the current root node
		if (curState.gameState.turnNum % 2 == 0) {

			for (ReversiNode rootChild = curState.leftChild; rootChild != null; rootChild = rootChild.rightSibling) {
				
				int nodeMinimax = Minimax(rootChild);
				if (nodeMinimax >= maxVal) {
					maxVal = nodeMinimax;
					destinationNode = rootChild;
				}
			}
			
		}
		else {

			for (ReversiNode rootChild = curState.leftChild; rootChild != null; rootChild = rootChild.rightSibling) {
				
				int nodeMinimax = Minimax(rootChild);
				if (nodeMinimax <= minVal) {
					minVal = nodeMinimax;
					destinationNode = rootChild;
				}
			}
			
		}
	
		// print out the computer player's move choice based on
		// the destination node's move row and column
		String playPosition = reverseCol(destinationNode.moveCol) + String.valueOf(reverseRow(destinationNode.moveRow));
		System.out.println(playPosition + "\n");
		
		// actually play the move that the computer player has chosen
		game.playMove(destinationNode.moveRow, destinationNode.moveCol);
		
		
	}
	
	// prints the tree starting with the current node
	// as a root
	/*public static void printTree(ReversiNode curNode) {
		
		if (curNode == null) {
			return;
		}
		
		System.out.println("Move number: " + curNode.gameState.turnNum);
		curNode.gameState.printBoard();
		System.out.println("\n");
		
		// print the left child's subtree and
		// then the right sibling's
		printTree(curNode.leftChild);
		printTree(curNode.rightSibling);
	}*/
	
	// function that allows the user to play 4x4 Reversi
	// with the computer player (and choose between playing
	// as Dark or Light)
	public static void playReversi4x4() throws IOException {
		
		String characterSelection = "";
		
		// welcome the user to the game and ask the user
		// which tile they would like to play as
		System.out.println("Welcome to 4x4 Reversi!");
		while (characterSelection.compareTo("X") != 0 && characterSelection.compareTo("O") != 0) {
			System.out.print("Type 'X' to play as Dark and 'O' to play as Light: ");
			Scanner scnr = new Scanner(System.in);
			characterSelection = scnr.next();
		}
		System.out.println();
		
		
		// initialize the game
		Reversi4x4 game = new Reversi4x4();
		game.printBoard();
		System.out.println("\n");
		
		// game logic if the player plays as Dark (X)
		if (characterSelection.compareTo("X") == 0) {
			
			// continue looping for turns until
			// the game has ended
			while (!game.gameOver()) {
				
				// if the turn is even, announce Dark's turn
				if (game.turnNum % 2 == 0) {
					System.out.print("Dark (X)'s turn: ");
				}
				// if the turn is odd, announce Light's turn
				else {
					System.out.print("Light (O)'s turn: ");
				}
				
				// if there are no available moves, skip
				// the turn and notify the players
				if (game.checkForMoves() == 0) {
					System.out.println("No valid moves available, skipping turn...");
				}
				
				// otherwise go into deciding which move to play
				else {
					
					// if the current turn is Light (O), that is 
					// the computer player, use Minimax to select a move
					if (game.turnNum % 2 != 0) {
						ReversiNode curStateNode = new ReversiNode(game);
						buildReversiSubtree(curStateNode);
						chooseMove(game, curStateNode);
						
					}
					
					// otherwise prompt the user to choose a move
					else {
						
						Scanner scnr = new Scanner(System.in);
						String nextMove = scnr.next();
						
						boolean validInput = false;
						
						while (!validInput) {
							
							try {
								
								// if input is over 2 characters, deny
								if (nextMove.length() > 2) {
									throw new Exception();
								}
								
								// if the move column is not a character between
								// lowercase a and d, deny
								char moveCol = nextMove.charAt(0);
								if ((int) moveCol < 97 || (int) moveCol > 100) {
									throw new Exception();
								}
								
								// attempt to parse the second character as Integer,
								// throw an Exception if it fails
								int moveRow = Integer.parseInt(Character.toString(nextMove.charAt(1)));
								System.out.println();
								
								// if the move is able to be played, then decide that
								// valid input has been accepted
								if (game.playMove(row(moveRow), col(moveCol)) != 0) {
									validInput = true;
								}
								
								// otherise print an error messsage and
								// prompt the user for input once again
								else {
									System.out.println("Invalid move. Try again.");
									if (game.turnNum % 2 == 0) {
										System.out.print("Dark (X)'s turn: ");
									}
									else {
										System.out.print("Light (O)'s turn: ");
									}
									nextMove = scnr.next();
								}
							}
							
							// catch any Exceptions that may occur and
							// prompt the user for input once again
							catch (Exception e) {
								System.out.println("Invalid input. Try again.");
								if (game.turnNum % 2 == 0) {
									System.out.print("Dark (X)'s turn: ");
								}
								else {
									System.out.print("Light (O)'s turn: ");
								}
								nextMove = scnr.next();
							}
						}
						
					}
					
					// print the board at the end
					// of each turn (unless a skip has occurred)
					game.printBoard();
					System.out.println();
				
				}
				
			}
			
			// once the game has ended, decide
			// and announce the winner
			if (game.checkWinner() == 1) {
				System.out.println("Dark (X) wins!");
			}
			else if (game.checkWinner() == -1) {
				System.out.println("Light (O) wins!");
			}
			else {
				System.out.println("The game is a draw!");
			}
			
		}
		
		// game logic if the player plays as Light (O)
		else if (characterSelection.compareTo("O") == 0) {
			
			
			while (!game.gameOver()) {
				
				if (game.turnNum % 2 == 0) {
					System.out.print("Dark (X)'s turn: ");
				}
				else {
					System.out.print("Light (O)'s turn: ");
				}
				
				if (game.checkForMoves() == 0) {
					System.out.println("No valid moves available, skipping turn...");
				}
				else {
					
					// if the current turn is Dark (X), that is the computer
					// player, so use Minimax to choose and play a move
					if (game.turnNum % 2 == 0) {
						ReversiNode curStateNode = new ReversiNode(game);
						buildReversiSubtree(curStateNode);
						chooseMove(game, curStateNode);
						
					}
					
					// otherwise allow the user to input their move
					else {
						
						Scanner scnr = new Scanner(System.in);
						String nextMove = scnr.next();
						
						boolean validInput = false;
						while (!validInput) {
							
							try {
								
								if (nextMove.length() > 2) {
									throw new Exception();
								}
								char moveCol = nextMove.charAt(0);
								if ((int) moveCol < 97 || (int) moveCol > 100) {
									throw new Exception();
								}
								int moveRow = Integer.parseInt(Character.toString(nextMove.charAt(1)));
								System.out.println();
								if (game.playMove(row(moveRow), col(moveCol)) != 0) {
									validInput = true;
								}
								else {
									System.out.println("Invalid move. Try again.");
									if (game.turnNum % 2 == 0) {
										System.out.print("Dark (X)'s turn: ");
									}
									else {
										System.out.print("Light (O)'s turn: ");
									}
									nextMove = scnr.next();
								}
							}
							catch (Exception e) {
								System.out.println("Invalid input. Try again.");
								if (game.turnNum % 2 == 0) {
									System.out.print("Dark (X)'s turn: ");
								}
								else {
									System.out.print("Light (O)'s turn: ");
								}
								nextMove = scnr.next();
							}
						}
					
						
					}
					
					game.printBoard();
					System.out.println();
				
				}
				
			}
			
			// once the game has ended, decide
			// and announce the winner
			if (game.checkWinner() == 1) {
				System.out.println("Dark (X) wins!");
			}
			else if (game.checkWinner() == -1) {
				System.out.println("Light (O) wins!");
			}
			else {
				System.out.println("The game is a draw!");
			}
		}
		
	}
	
	///////////////////////////////
	// Part 2 Functions Start Here
	///////////////////////////////
	
	// heuristic evaluation function that determines
	// the value of a state even if it is non-terminal
	// (counts the number of dark tiles on the board and returns it)
	public static int H_Reversi(Reversi8x8 curState) {
			
		int darkVal = 0;
			
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (curState.gameBoard[i][j] == 'X') {
					darkVal += 1;
				}
			}
		}
			
			return darkVal;
	}
	
	// heuristic minimax function that both builds a node's
	// subtree (with limited depth) and calculates 
	// minimax values using alpha-beta pruning
	public static void H_Minimax(BigReversiNode curNode, int searchDepth) {
		
		// if the game is over in the state...
		if (curNode.gameState.gameOver()) {

			// if Dark is the winner, return 64 as the H_Minimax value 
			// (as this trumps any possible value of the heuristic function
			if (curNode.gameState.checkWinner() > 0) {
				curNode.lowerBound = 64;
				curNode.upperBound = 64;
			}
			
			// if Light is the winner, return -1 as the H_Minimax value 
			// as this is less than any possible value of the heuristic function
			else if (curNode.gameState.checkWinner() < 0) {
				
				curNode.lowerBound = -1;
				curNode.upperBound = -1;

			}
			
			// if the game is a draw, return 0 as the H_Minimax value
			else {
				curNode.lowerBound = 0;
				curNode.upperBound = 0;
			}
		}
		
		// otherwise, if the current node's depth is at 
		// the maximum allowed depth, return the heuristic
		// functions value as its H_Minimax value
		else if (curNode.depth >= searchDepth) {
			int nodeVal = H_Reversi(curNode.gameState);
			curNode.lowerBound = nodeVal;
			curNode.upperBound = nodeVal;
		}
		
		// otherwise, continue building the subtree under this node and
		// select the maximum/minimum value of its children as appropriate
		else {
			
			int validMoves = 0;
			
			// check for valid moves given the current state and turn
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					if (curNode.gameState.validateMove(i, j) > 0) {
						
						validMoves += 1;
						
						curNode.insertChild(i, j);
						curNode.leftChild.gameState.playMove(i, j);
						curNode.leftChild.gameState.consecutiveTurnsSkipped = 0;
					}
				}
			}
			
			if (validMoves == 0) {
					curNode.insertChild(-1, -1);
					curNode.leftChild.gameState.turnNum += 1;
					curNode.leftChild.gameState.consecutiveTurnsSkipped += 1;
			}
			
			// if it's Dark (X)'s turn, find the maximum H_Minimax
			// value of all the node's children and adjust the upper and
			// lower bounds of the node accordingly
			if (curNode.gameState.turnNum % 2 == 0) {
				
				
				// calculate the H_Minimax values for each of the children nodes
				for (BigReversiNode childNode = curNode.leftChild; childNode != null; childNode = childNode.rightSibling) {
					
					H_Minimax(childNode, searchDepth);
					
					if (childNode.upperBound > curNode.lowerBound) {
						curNode.lowerBound = childNode.upperBound;
					}
					
					// if the current node's lower bound is greater than
					// the parent's upper bound, Light (X) should never
					// select this option (optimally), so disregard it
					if (curNode.parent != null) {
						if (curNode.lowerBound > curNode.parent.upperBound) {
							break;
						}
					}
				}
				
				// make the upper bound match the lower bound once
				// the appropriate value has been found
				curNode.upperBound = curNode.lowerBound;
				
				
			}
			
			// if it's Light (O)'s turn, find the minimum H_Minimax
			// value of all the node's children and adjust the upper and
			// lower bounds of the node accordingly
			else {
				
				// calculate the H_Minimax values for each of the children nodes
				for (BigReversiNode childNode = curNode.leftChild; childNode != null; childNode = childNode.rightSibling) {

					H_Minimax(childNode, searchDepth);
					
					
					if (childNode.lowerBound < curNode.upperBound) {
						curNode.upperBound = childNode.lowerBound;
					}
					
					// if the current node's lower bound is greater than
					// the parent's upper bound, Light (X) should never
					// select this option (optimally), so disregard it
					if (curNode.parent != null) {
						if (curNode.upperBound < curNode.parent.lowerBound) {
							break;
						}
					}
				}
				
				// make the lower bound match the upper bound once
				// the appropriate value has been found
				curNode.lowerBound = curNode.upperBound;

				
			}
			
		}

	}
	
	// function that utilizes the H_Minimax function to help the
	// computer player select a promising move
	public static void H_chooseMove(Reversi8x8 game, BigReversiNode curState, int searchDepth) {

		// store the destination node so we can find what move to take later
		BigReversiNode destinationNode = new BigReversiNode(curState.gameState);
		
		// same logic as H_Minimax but only for the root
		// node and the game's current state
		if (curState.gameState.turnNum % 2 == 0) {
			
			int curMaximum = -10;
			
			H_Minimax(curState, searchDepth);

			for (BigReversiNode rootChild = curState.leftChild; rootChild != null; rootChild = rootChild.rightSibling) {
				
				if (rootChild.lowerBound >= curMaximum) {
					curMaximum = rootChild.lowerBound;
					destinationNode = rootChild;
				}
				
			}
			
		}
		else {
			
			int curMinimum = 70;
			
			H_Minimax(curState, searchDepth);

			for (BigReversiNode rootChild = curState.leftChild; rootChild != null; rootChild = rootChild.rightSibling) {
				
				if (rootChild.upperBound <= curMinimum) {
					curMinimum = rootChild.upperBound;
					destinationNode = rootChild;
				}

				
			}
		}
		
		// announce the computer player's selection using the destination node's move's row and column
		String playPosition = reverseCol(destinationNode.moveCol) + String.valueOf(reverseRow(destinationNode.moveRow));
		System.out.println(playPosition + "\n");
		
		// actually play the move that the computer has selected
		game.playMove(destinationNode.moveRow, destinationNode.moveCol);
	}
	
	
	/*public static void printTree(BigReversiNode curNode) {
		if (curNode == null) {
			return;
		}
		
		System.out.println("Move number: " + curNode.gameState.turnNum);
		System.out.println("H-Minimax lower bound: " + curNode.lowerBound);
		System.out.println("H-Minimax upper bound: " + curNode.upperBound);
		curNode.gameState.printBoard();
		System.out.println("\n");
		
		printTree(curNode.leftChild);
		printTree(curNode.rightSibling);
	}*/
	
	// function that allows the user to play 8x8 Reversi
	// with the computer player (and choose between playing
	// as Dark or Light)
	// NOTE: the implementation should be almost exactly the same as
	// 		 playReversi4x4() with the exception of using H_Minimax to
	//		 help the computer player select a move (and some minor tweaks
	//		 in processing valid user inputs)
	public static void playReversi8x8(int searchDepth) throws IOException {

		String characterSelection = "";
		
		System.out.println("Welcome to 8x8 Reversi!");
		while (characterSelection.compareTo("X") != 0 && characterSelection.compareTo("O") != 0) {
			System.out.print("Type 'X' to play as Dark and 'O' to play as Light: ");
			Scanner scnr = new Scanner(System.in);
			characterSelection = scnr.next();
		}
		System.out.println();
		
		
		Reversi8x8 game = new Reversi8x8();
		game.printBoard();
		
		
		// if the user opts to play as Dark (X)...
		if (characterSelection.compareTo("X") == 0) {
			
			// while the game hasn't ended
			while (!game.gameOver()) {
				
				// announce current turn
				if (game.turnNum % 2 == 0) {
					System.out.print("Dark (X)'s turn: ");
				}
				else {
					System.out.print("Light (O)'s turn: ");
				}
				
				// if no moves are available, skip the current turn
				if (game.checkForMoves() == 0) {
					System.out.println("No valid moves available, skipping turn...");
				}
				
				// otherwise, play the next turn
				else {
					
					// if it is Light (O)'s turn, use H_Minimax to
					// help the computer select a move
					if (game.turnNum % 2 != 0) {
						BigReversiNode curStateNode = new BigReversiNode(game);
						H_chooseMove(game, curStateNode, searchDepth);
						
					}
					
					// otherwise, prompt the user for input on their turn
					else {
						
						Scanner scnr = new Scanner(System.in);
						String nextMove = scnr.next();
						
						boolean validInput = false;
						
						while (!validInput) {
							
							try {
								if (nextMove.length() > 2) {
									throw new Exception();
								}
								char moveCol = nextMove.charAt(0);
								if ((int) moveCol < 97 || (int) moveCol > 104) {
									throw new Exception();
								}
								int moveRow = Integer.parseInt(Character.toString(nextMove.charAt(1)));
								System.out.println();
								if (game.playMove(row(moveRow), col(moveCol)) != 0) {
									validInput = true;
								}
								else {
									System.out.println("Invalid move. Try again.");
									if (game.turnNum % 2 == 0) {
										System.out.print("Dark (X)'s turn: ");
									}
									else {
										System.out.print("Light (O)'s turn: ");
									}
									nextMove = scnr.next();
								}
							}
							catch (Exception e) {
								System.out.println("Invalid input. Try again.");
								if (game.turnNum % 2 == 0) {
									System.out.print("Dark (X)'s turn: ");
								}
								else {
									System.out.print("Light (O)'s turn: ");
								}
								nextMove = scnr.next();
							}
						}
						
					}
					
					// print the board at the end
					// of each turn (unless a skip has occurred)
					game.printBoard();
				
				}
				
			}
			
			// once the game has ended, decide
			// and announce the winner
			if (game.checkWinner() == 1) {
				System.out.println("Dark (X) wins!");
			}
			else if (game.checkWinner() == -1) {
				System.out.println("Light (O) wins!");
			}
			else {
				System.out.println("The game is a draw!");
			}
			
		}
		
		// if the user opts to play as Light (O)...
		else if (characterSelection.compareTo("O") == 0) {
			
			// while the game hasn't ended
			while (!game.gameOver()) {
				
				// announce current turn
				if (game.turnNum % 2 == 0) {
					System.out.print("Dark (X)'s turn: ");
				}
				else {
					System.out.print("Light (O)'s turn: ");
				}
				
				// if no moves are available, skip the current turn
				if (game.checkForMoves() == 0) {
					System.out.println("No valid moves available, skipping turn...");
				}
				
				// otherwise, play the next turn
				else {
					
					if (game.turnNum % 2 == 0) {
						BigReversiNode curStateNode = new BigReversiNode(game);
						H_chooseMove(game, curStateNode, searchDepth);
						
					}
					else {
						
						Scanner scnr = new Scanner(System.in);
						String nextMove = scnr.next();
						
						boolean validInput = false;
						while (!validInput) {
							
							try {
								
								if (nextMove.length() > 2) {
									throw new Exception();
								}
								char moveCol = nextMove.charAt(0);
								if ((int) moveCol < 97 || (int) moveCol > 104) {
									throw new Exception();
								}
								int moveRow = Integer.parseInt(Character.toString(nextMove.charAt(1)));
								System.out.println();
								if (game.playMove(row(moveRow), col(moveCol)) != 0) {
									validInput = true;
								}
								else {
									System.out.println("Invalid move. Try again.");
									if (game.turnNum % 2 == 0) {
										System.out.print("Dark (X)'s turn: ");
									}
									else {
										System.out.print("Light (O)'s turn: ");
									}
									nextMove = scnr.next();
								}
							}
							catch (Exception e) {
								System.out.println("Invalid input. Try again.");
								if (game.turnNum % 2 == 0) {
									System.out.print("Dark (X)'s turn: ");
								}
								else {
									System.out.print("Light (O)'s turn: ");
								}
								nextMove = scnr.next();
							}
						}
					
						
					}
					
					game.printBoard();
				
				}
				
			}
			
			System.out.println("\n");
			
			// once the game has ended, decide
			// and announce the winner
			if (game.checkWinner() == 1) {
				System.out.println("Dark (X) wins!");
			}
			else if (game.checkWinner() == -1) {
				System.out.println("Light (O) wins!");
			}
			else {
				System.out.println("The game is a draw!");
			}
		}
		
	}
	
	
	// main method to ask the user whether they want to play 4x4
	// or 8x8 Reversi and then play the game of the user's choice
	public static void main(String[] args) throws IOException {
	
		System.out.println("Welcome to Reversi!");
		System.out.println("(1) 4x4 Reversi against Minimax opponent");
		System.out.println("(2) 8x8 Reversi against limited depth H-Minimax with alpha-beta pruning");
		System.out.print("Select your play option by number: ");
		
		String playerOption = "";
		Scanner scnr = new Scanner(System.in);
		
		playerOption = scnr.next();
		while (playerOption.compareTo("1") != 0 && playerOption.compareTo("2") != 0) {
			System.out.println("Invalid option. Try again.\n");
			System.out.println("(1) 4x4 Reversi against Minimax opponent");
			System.out.println("(2) 8x8 Reversi against limited depth H-Minimax with alpha-beta pruning");
			System.out.print("Select your play option by number: ");
			playerOption = scnr.next();
		}

		
		if (playerOption.compareTo("1") == 0) {
			System.out.println("\n\n\n\n\n\n\n\n\n");
			playReversi4x4();
		}
		else if (playerOption.compareTo("2") == 0) {
			int searchDepth = 0;
			boolean validInput = false;
			
			// ask the user what search depth they 
			// want to use for 8x8 Reversi's H-Minimax
			while (!validInput) {
			
				System.out.println();
				System.out.println("NOTE: search depths 5-7 are recommended for the highest skill computer player.");
				System.out.print("Select your search depth (between 1 and 7): ");
			
				try {
					searchDepth = Integer.parseInt(scnr.next());
					
					if (searchDepth > 0 && searchDepth <= 7) {
						validInput = true;
					}
					else {
						throw new Exception();
					}
				}
				catch (Exception e) {
					System.out.println("Invalid input. Try again.");
				}
			}
			
			System.out.println("\n\n\n\n\n\n\n\n\n");
			
			// start the Reversi game
			playReversi8x8(searchDepth);
		}


		
	}
	
}
