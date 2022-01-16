import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MonteCarlo {

    private Tree tree;
    private int iterations;
    private int depth;
    private BoardEvaluator evaluator;
    private int myColor;
    //private int me;

    public MonteCarlo(String [][] board, int iterations,int depth,int myColor){
        this.tree = new Tree(board);
        this.iterations= iterations;
        this.depth = depth;
        this.myColor=myColor;
        //this.me = myColor;
        this.evaluator= new BoardEvaluator();
    }

    public void runMonteCarlo(World world){
        World clone =new World(world);
        this.tree.addChildren(tree.getRoot(),clone,myColor);
        for(int i=0; i<iterations; i++){
            System.out.println("Iteration :" +(i+1));
            search(tree.getRoot(), clone.board,clone,clone.myColor);
        }
    }

    public String chooseAction(){
        Node node = tree.getRoot();
        Node actionNode= tree.getRoot();
        double bestVal= Double.NEGATIVE_INFINITY;
        for(Node child : node.getChildren()){
            double childVal = ucb(child);
            if(childVal >= bestVal){
                bestVal = childVal;
                actionNode = child;
            }

        }
        return actionNode.getState().getAction();




    }



    public void search(Node node,String [][] board,World world,int myColor){

        while(!node.isLeaf()){
            Node best = chooseChild(node);
            node = best;
            if(node.getState().isTerminal())
                break;

        }
        if(node.getNumOfVisits() == 0){
            rollout(node,myColor,world,board);
        }
        else{
            tree.addChildren(node,world,myColor);
            assert !node.getChildren().isEmpty():"No children";
            rollout(node.getChild(0),myColor,world,board);
        }


    }

    public void rollout(Node node,int myColor,World world , String [][] board){
        State state = node.getState();
        String [][] tmpboard = state.getBoard();
        while(!(state.isTerminal()) && depth ==0){
            if(state.isTerminal())
                break;

            ArrayList<String> availablemoves = world.getMoves(myColor,tmpboard);
//            if(myColor==0 && me ==0 ){
//                Collections.reverse(availablemoves);
//            }
            Random ran = new Random();
            int rand = ran.nextInt(availablemoves.size());
            String ranmove= availablemoves.get(rand);
            tmpboard=state.makeMove(Integer.parseInt(String.valueOf(ranmove.charAt(0))),Integer.parseInt(String.valueOf(ranmove.charAt(1))),
                    Integer.parseInt(String.valueOf(ranmove.charAt(2))),Integer.parseInt(String.valueOf(ranmove.charAt(3))),board);

            state= new State(tmpboard,ranmove);
            if(myColor ==0){
                myColor = 1;

            }
            else{
                myColor=0;
            }
            depth--;
        }
        tree.updateParents(node,evaluator.evaluate(state.getBoard(),myColor ));
    }

    public double ucb(Node node){
        if(node.getNumOfVisits() == 0)
            return Double.POSITIVE_INFINITY;
        else
            return (node.getValue()/node.getNumOfVisits())+2*(Math.sqrt(Math.log(node.getParent().getNumOfVisits())/node.getNumOfVisits()));


    }

    public Node chooseChild(Node node){
        Node bestChild = null;
        double minvalue = Double.NEGATIVE_INFINITY;
        for(Node child : node.getChildren()){
            double tmpucb=ucb(child);
            if(tmpucb >= minvalue){
                minvalue = tmpucb;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
