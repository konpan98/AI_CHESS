import java.util.ArrayList;
import java.util.Arrays;

public class State {
    private String [][] board;
    private String action;
    private double evaluation;
    int rows = 7;
    int cols = 5;

    public State(String[][] board) {
        this.board = board;
    }

    public State(String[][] board, String action) {
        this.board = board;
        this.action = action;
    }

    public State(String[][] board, String action, double evaluation) {
        this.board = board;
        this.action = action;
        this.evaluation = evaluation;
    }



    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isTerminal(){
        int counter=0;
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                if(board[i][j].equals("BK") || board[i][j].equals("WK"))
                    counter++;
            }
        }
        if(counter == 2)
            return false;
        else {
            return true;
        }
    }
    public String[][] makeMove(int x1, int y1, int x2, int y2, String[][] bd)
    {
        String[][] board = deepCopy(bd);
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

        return board;
        // check if a prize has been added in the game

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

    public ArrayList<State> allStates(String [][] board , World world,int myColor){
        ArrayList<String> availablemoves = world.getMoves(myColor,board);
        ArrayList<State> states = new ArrayList<State>();
        String [][] testBoard = deepCopy(board);
        for(String move : availablemoves){
            testBoard = makeMove(Integer.parseInt(String.valueOf(move.charAt(0))),Integer.parseInt(String.valueOf(move.charAt(1))),
                    Integer.parseInt(String.valueOf(move.charAt(2))),Integer.parseInt(String.valueOf(move.charAt(3))),board);
            states.add(new State(testBoard,move));

        }
        return states;


    }

}
