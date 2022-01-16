import java.util.ArrayList;

public class Tree {
    Node root;

    public Tree(String [][] board){
        this.root = new Node(board,null);
        root.setNumOfVisits(1);
    }

    public Tree(Node node){
        this.root = node;
        this.root.setParent(null);
    }

    public void addChildren(Node parNode, World world,int myColor){
        String [][] tmpboard = parNode.getState().getBoard();
        ArrayList<State> children =parNode.getState().allStates(tmpboard,world, myColor);
        for(State state: children){
            Node child = new Node(state,parNode);
            parNode.addChild(child);
        }
    }

    public void updateParents(Node node, int eval){
        node.setNumOfVisits(node.getNumOfVisits()+1);
        node.setValue(node.getValue()+eval);
        while(node.getParent()!=null){
            Node parent = node.getParent();
            parent.setNumOfVisits(node.getNumOfVisits()+1);
            parent.setValue(node.getValue()+eval);
            node = parent;
        }
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
        this.root.setParent(null);
    }
}