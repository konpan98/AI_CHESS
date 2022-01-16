import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Minimax {
    public int  myColor;
    public int  me;
    public BoardEvaluator boardEvaluator;
    public String move = null;
    public int evaluated ;
    public int maxDepth;
    public HashMap<Integer,String[][]> depthMove = new HashMap<>();
    private double timer;
    int rows = 7;
    int cols = 5;

//    public HashMap<String[][],Integer> board_score = new HashMap<>();

    public Minimax(int myColor,int maxDepth) {
        this.myColor = myColor;
        this.me = myColor;
        this.maxDepth = maxDepth;
        this.boardEvaluator = new BoardEvaluator();
    }


    public String execute(int depth, World world){
        World cloned = new World(world);
        timer = System.currentTimeMillis();
        minimax(cloned.board,cloned, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, cloned.myColor);


        return this.move;
    }



    public int minimax(String board[][], World world, int depth, int alpha, int beta, boolean maximizingPlayer, int myColor){
        int eval = 0;

        String[][] bd_cl;
        world.moves();

        ArrayList<String> availableMoves = world.getMoves(myColor,board);

        if(myColor==0 && me ==0 ){
            Collections.reverse(availableMoves);
        }




        double currentTime = System.currentTimeMillis() - timer;

        if (depth == 0 || currentTime>=5998 ){
            System.out.println(currentTime);
            return this.boardEvaluator.evaluate(board,myColor);
        }


        if (myColor == 0){
            myColor = 1;
        }else{
            myColor = 0;
        }

        if(maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            for (String move: availableMoves){

                bd_cl = makeMove(Integer.parseInt(String.valueOf(move.charAt(0))),Integer.parseInt(String.valueOf(move.charAt(1))),
                        Integer.parseInt(String.valueOf(move.charAt(2))),Integer.parseInt(String.valueOf(move.charAt(3))),board);

                eval = minimax(bd_cl,world,depth - 1,alpha, beta, false,myColor );

                if(eval >= maxEval){
                    maxEval = eval;
                    if(depth==maxDepth){
                        this.move = move;
                    }

                }
                alpha = Math.max(alpha,eval);
                if (beta <= alpha) break;
            }
            evaluated = maxEval;
            return maxEval;

        }
        else {
            int minEval = Integer.MAX_VALUE;

            for (String move: availableMoves){

                bd_cl = makeMove(Integer.parseInt(String.valueOf(move.charAt(0))),Integer.parseInt(String.valueOf(move.charAt(1))),
                        Integer.parseInt(String.valueOf(move.charAt(2))),Integer.parseInt(String.valueOf(move.charAt(3))),board);

                eval = minimax(bd_cl,world, depth - 1, alpha, beta, true,myColor );



                if(eval <= minEval){
                    minEval = eval;

                }
                beta = Math.min(beta,eval);
                if (beta <= alpha) break;
            }


            return minEval;
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

}
