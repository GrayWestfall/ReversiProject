README 

Grayson Westfall
email: graywestfall@gmail.com

FILES INCLUDED:

Reversi4x4.java
ReversiNode.java
Reversi8x8.java
BigReversiNode.java
PlayReversi.java


OVERVIEW:

My Reversi project, when run, prompts the user to choose between playing 4x4 Reversi and 8x8 Reversi.  

If the user selects 4x4 Reversi, they will play against a computer player that uses traditional Minimax to select optimal moves.

If the user selects 8x8 Reversi, they will play against a computer player that uses heuristic minimax with limited depth search to select the most promising move.  The heuristic I implemented evaluates board states by counting how many of each players' chips are on the board at that state. The more of the computer player's chips are on the board in that state, the more beneficial it will weight that state.  The computer player will select the move that leads it closer to the most beneficial state discovered.  Before the game begins, the user will be prompted to specify the search depth they want the computer to use (between 1 and 7).  Depth 7 has been tested several times and doesn't appear to cause any stack overflow yet, but this remains uncertain.

For user input regarding moves, the proper input format should be 'a1', with the first character being the lowercase letter representing the column, and the second character being the row number.  Any input that does not follow this format will be rejected until a properly formatted move is selected.


HOW TO RUN:

To run this code, first create a folder with all of the source files in it (this should happen already when opening the zip file that contains the code). Secondly, use the terminal to cd into that folder.  Once in that folder using the terminal, use this command to compile all of the code:

javac *.java

This should compile the 5 source files and prepare them for runtime. Then to run the code, use this command:

java PlayReversi

This should begin the program.

The program will only play either 4x4 Reversi or 8x8 Reversi one time.  It will need to be run another time to play another game of either type once the selected game has ended.

If there are any questions regarding my code or how to run it, please send me an email at graywestfall@gmail.com

Thank you,

Grayson





