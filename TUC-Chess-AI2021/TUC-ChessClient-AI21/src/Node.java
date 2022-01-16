import java.util.ArrayList;

public class Node {
    private State state;
    private	 double numOfVisits;
    private	  double value;
    private	 Node parent;
    private ArrayList<Node> children;

    public Node(String [][] board, String action, Node parent) {
        this.state = new State(board,action);
        this.numOfVisits=0;
        this.value=0;
//
        this.parent=parent;
        this.children = new ArrayList<Node>();
    }

    public Node(String [][] board, Node parent) {
        this.state = new State(board);
        this.numOfVisits = 0;
        this.value = 0;
        this.parent = parent;
        this.children = new ArrayList<Node>();
    }

    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
        this.numOfVisits = 0;
        this.value = 0;
        this.children = new ArrayList<Node>();
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public void addChild(Node node){
        node.setParent(this);
        this.children.add(node);
    }

    public Node getChild(int index){
        return this.children.get(index);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getNumOfVisits() {
        return numOfVisits;
    }

    public void setNumOfVisits(double numOfVisits) {
        this.numOfVisits = numOfVisits;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

}