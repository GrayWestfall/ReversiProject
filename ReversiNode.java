
// class that represents a node of the state space
// search tree for 4x4 Reversi (the implementation of the tree
// consists of a linked list of children for each parent node)
public class ReversiNode {
	
	Reversi4x4 gameState;				// hold a game state object
	ReversiNode parent;					// pointer to the node's parent node
	ReversiNode leftChild;				// pointer to the node's left child
	ReversiNode rightSibling;			// pointer to the node's right sibling
	int depth;							// the depth of the node in the current tree
	int moveRow;						// the row number of the last move made to get to that node
	int moveCol;						// the column number of the last move made to get to that node
	
	// standard constructor for a node,
	// starts with an initial Reversi board
	public ReversiNode() {
		gameState = new Reversi4x4();
		parent = null;
		leftChild = null;
		rightSibling = null;
		depth = 0;
		moveRow = 0;
		moveCol = 0;
	}
	
	// extra constructor for a node that copies
	// the Reversi4x4 argument's state into the 
	// node's game state
	public ReversiNode(Reversi4x4 curState) {
		gameState = new Reversi4x4(curState);
		parent = null;
		leftChild = null;
		rightSibling = null;
		depth = 0;
		moveRow = -1;
		moveCol = -1;
	}

	// inserts a new left child into the linked list of
	// the current node's children (based on the move's
	// row and column number arguments passed in)
	public void insertChild(int moveRow, int moveCol) {
		ReversiNode newChild = new ReversiNode();
		newChild.gameState = new Reversi4x4(this.gameState);
		newChild.parent = this;
		newChild.leftChild = null;
		newChild.rightSibling = this.leftChild;
		newChild.depth = this.depth + 1;
		newChild.moveRow = moveRow;
		newChild.moveCol = moveCol;
		
		this.leftChild = newChild;
		
	}
	
}
