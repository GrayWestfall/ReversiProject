
// class that represents a node of the state space
// search tree for 8x8 Reversi (the implementation of the tree
// consists of a linked list of children for each parent node)
public class BigReversiNode {
				
	Reversi8x8 gameState;				// hold a game state object	
	BigReversiNode parent;				// pointer to the node's parent node
	BigReversiNode leftChild;			// pointer to the node's left child
	BigReversiNode rightSibling;		// pointer to the node's right sibling
	int depth;							// the depth of the node in the current tree
	int moveRow;						// the row number of the last move made to get to that node
	int moveCol;						// the column number of the last move made to get to that node
	int lowerBound;						// the 'alpha' lower bound value of the node used for alpha-beta pruning
	int upperBound;						// that 'beta' upper bound value of the node used for alpha-beta pruning
	
	
	// standard constructor for a node,
	// starts with an initial Reversi board
	public BigReversiNode() {
		gameState = new Reversi8x8();
		parent = null;
		leftChild = null;
		rightSibling = null;
		depth = 0;
		moveRow = 0;
		moveCol = 0;
		lowerBound = -70;
		upperBound = 70;
		
	}
	
	// extra constructor for a node that copies
	// the Reversi8x8 argument's state into the 
	// node's game state
	public BigReversiNode(Reversi8x8 curState) {
		gameState = new Reversi8x8(curState);
		parent = null;
		leftChild = null;
		rightSibling = null;
		depth = 0;
		moveRow = -1;
		moveCol = -1;
		lowerBound = -70;
		upperBound = 70;
	}
	
	// inserts a new left child into the linked list of
	// the current node's children (based on the move's
	// row and column number arguments passed in)
	public void insertChild(int moveRow, int moveCol) {
		BigReversiNode newChild = new BigReversiNode();
		newChild.gameState = new Reversi8x8(this.gameState);
		newChild.parent = this;
		newChild.leftChild = null;
		newChild.rightSibling = this.leftChild;
		newChild.depth = this.depth + 1;
		newChild.moveRow = moveRow;
		newChild.moveCol = moveCol;
		
		this.leftChild = newChild;
		
	}
}
