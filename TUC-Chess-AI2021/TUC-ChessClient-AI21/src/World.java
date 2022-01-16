import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;


public class World
{
	public String[][] board = null;
	private int rows = 7;
	private int columns = 5;
	public int myColor = 0;
	private ArrayList<String> availableMoves = null;

	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;

	public int scoreWhite = 0;
	public int scoreBlack = 0;

	public int depth = 7;
	public int option = 1;
	public World()
	{
		board = new String[rows][columns];
		
		/* represent the board

		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP

		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";

		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}


	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public String selectAction()
	{
		String move = "";
		if(option == 1){
			Minimax minimax = new Minimax(myColor,depth);
			move = minimax.execute(depth,this);
			//System.out.println("EVALUATED: " + minimax.evaluated);
		}
		else{
			MonteCarlo monteCarlo = new MonteCarlo(this.board,1000,50,myColor);
			monteCarlo.runMonteCarlo(this);
			move = monteCarlo.chooseAction();
		}
		nTurns++;
		nBranches += availableMoves.size();

		return move;
	}


	public void moves()
	{
		availableMoves = new ArrayList<String>();


		if(myColor == 0)		// I am the white player
			this.whiteMoves();

		else					// I am the black player
			this.blackMoves();

		// keeping track of the branch factor

	}
	public ArrayList<String> getMoves(int myColor,String[][] board){
		String[][] old = deepCopy(this.board);
		this.board = board;
		availableMoves = new ArrayList<String>();

		if(myColor == 0)		// I am the white player
			this.whiteMoves();

		else					// I am the black player
			this.blackMoves();
		this.board = old;
		return availableMoves;
	}


	private void whiteMoves()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";

		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));

				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;

				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));

				if(secondLetter.equals("P"))	// it is a pawn
				{

					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));

					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i-1) + Integer.toString(j);

						availableMoves.add(move);
					}

					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i-1) + Integer.toString(j-1);

							availableMoves.add(move);
						}
					}

					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {

							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i-1) + Integer.toString(j+1);
							availableMoves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;

						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));

						if(firstLetter.equals("W"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i-(k+1)) + Integer.toString(j);

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}

					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;

						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));

						if(firstLetter.equals("W"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i+(k+1)) + Integer.toString(j);

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}

					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;

						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));

						if(firstLetter.equals("W"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i) + Integer.toString(j-(k+1));

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}

					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;

						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));

						if(firstLetter.equals("W"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i) + Integer.toString(j+(k+1));

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));

						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i-1) + Integer.toString(j);

							availableMoves.add(move);
						}
					}

					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));

						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i+1) + Integer.toString(j);

							availableMoves.add(move);
						}
					}

					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));

						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i) + Integer.toString(j-1);

							availableMoves.add(move);
						}
					}

					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));

						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i) + Integer.toString(j+1);

							availableMoves.add(move);
						}
					}
				}
			}
		}
	}

	private void blackMoves()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";

		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));

				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;

				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));

				if(secondLetter.equals("P"))	// it is a pawn
				{

					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));

					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i+1) + Integer.toString(j);

						availableMoves.add(move);
					}

					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));

						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i+1) + Integer.toString(j-1);

							availableMoves.add(move);
						}
					}

					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));

						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i+1) + Integer.toString(j+1);

							availableMoves.add(move);
						}



					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;

						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));

						if(firstLetter.equals("B"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i-(k+1)) + Integer.toString(j);

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}

					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;

						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));

						if(firstLetter.equals("B"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i+(k+1)) + Integer.toString(j);

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}

					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;

						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));

						if(firstLetter.equals("B"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i) + Integer.toString(j-(k+1));

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}

					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;

						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));

						if(firstLetter.equals("B"))
							break;

						move = Integer.toString(i) + Integer.toString(j) +
								Integer.toString(i) + Integer.toString(j+(k+1));

						availableMoves.add(move);

						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));

						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i-1) + Integer.toString(j);

							availableMoves.add(move);
						}
					}

					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));

						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i+1) + Integer.toString(j);

							availableMoves.add(move);
						}
					}

					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));

						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i) + Integer.toString(j-1);

							availableMoves.add(move);
						}
					}

					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));

						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) +
									Integer.toString(i) + Integer.toString(j+1);

							availableMoves.add(move);
						}
					}
				}
			}
		}
	}
	private String selectRandomAction()
	{
		BoardEvaluator evaluator = new BoardEvaluator();
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
//
//		System.out.println("EVALUATION: "+ evaluator.evaluate(getInfo(),distanceFromPrize(),myColor));
		return availableMoves.get(x);
	}
	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}

	//x1,y1 my pawn from, x2,y2 to, prizeX,prizeY
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}


	//	0 -> whites, 1->blacks
	public int[] getInfo(){
		int[] info = new int[4];
		String firstLetter = "";
		String secondLetter = "";
		int valueWhite = 0;
		int valueBlack =0;
		int whites = 0;
		int blacks = 0;

		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){

				try {
					firstLetter = Character.toString(board[i][j].charAt(0));
					secondLetter = Character.toString(board[i][j].charAt(1));
					if(firstLetter.equals("B")){
						if(secondLetter.equals("P")){
							valueBlack += 1;
						}
						else if(secondLetter.equals("R")){
							valueBlack += 3;
						}else if(secondLetter.equals("K")){
							valueBlack += 7;
						}
						blacks++;

					}else if(firstLetter.equals("W")){
						if(secondLetter.equals("P")){
							valueWhite += 1;
						}
						else if(secondLetter.equals("R")){
							valueWhite += 3;
						}else if(secondLetter.equals("K")){
							valueWhite += 7;
						}
						whites++;
					}
				}catch (StringIndexOutOfBoundsException e){

				}


			}
		}

		info[0] = whites;
		info[1] = blacks;
		info[2] = valueWhite;
		info[3] = valueBlack;

		return info;
	}


	public int[] distanceFromPrize(){
		//Manhattan distances
		int[] returnValues = new int[2];
		ArrayList<Location> prizes, whites, blacks;
		prizes = getPrizeLoc();
		whites = getWhiteLoc();
		blacks = getBlackLoc();
		int distance_white = Integer.MAX_VALUE;
		int distance_black = Integer.MAX_VALUE;
		int temp = 0;

		for(Location loc: whites){
			for(Location loc2: prizes){
				temp = Math.abs(loc.x - loc2.x) + Math.abs(loc.y - loc2.y);
				distance_white = Math.min(temp,distance_white);
			}
		}
		for(Location loc: blacks){
			for(Location loc2: prizes){
				temp = Math.abs(loc.x - loc2.x) + Math.abs(loc.y - loc2.y);
				distance_black = Math.min(temp,distance_black);
			}
		}
		returnValues[0] = distance_white;
		returnValues[1] = distance_black;
		return returnValues;

	}
	public ArrayList<Location> getPrizeLoc(){
		String firstLetter;
		ArrayList<Location>  prizes = new ArrayList<>();

 		for(int i=0; i<rows; i++) {
			for (int j = 0; j < columns; j++) {
				firstLetter = Character.toString(board[i][j].charAt(0));
				if(firstLetter.equals("P")){
					Location loc = new Location(i,j);
					prizes.add(loc);
				}
			}
		}
 		return prizes;
	}

	public ArrayList<Location> getWhiteLoc(){
		String firstLetter;
		ArrayList<Location>  whites = new ArrayList<>();

		for(int i=0; i<rows; i++) {
			for (int j = 0; j < columns; j++) {
				firstLetter = Character.toString(board[i][j].charAt(0));
				if(firstLetter.equals("W")){
					Location loc = new Location(i,j);
					whites.add(loc);
				}
			}
		}
		return whites;
	}

	public ArrayList<Location> getBlackLoc(){
		String firstLetter;
		ArrayList<Location>  blacks = new ArrayList<>();

		for(int i=0; i<rows; i++) {
			for (int j = 0; j < columns; j++) {
				firstLetter = Character.toString(board[i][j].charAt(0));
				if(firstLetter.equals("B")){
					Location loc = new Location(i,j);
					blacks.add(loc);
				}
			}
		}
		return blacks;
	}

	public World(World another) {
		this.board = deepCopy(another.board);
		this.rows = another.rows;
		this.columns = another.columns;
		this.myColor = another.myColor;
		this.availableMoves = another.availableMoves;
		this.rookBlocks = another.rookBlocks;
		this.nTurns = another.nTurns;
		this.nBranches = another.nBranches;
		this.noPrize = another.noPrize;
	}

	public ArrayList<String> getAvailableMoves() {
		return availableMoves;
	}

	public  String[][] deepCopy(String[][] org) {
		if (org == null) {
			return null;
		}

		final String[][] res = new String[org.length][];
		for (int i = 0; i < org.length; i++) {
			res[i] = Arrays.copyOf(org[i], org[i].length);
		}
		return res;
	}

}
