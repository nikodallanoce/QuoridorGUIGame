package gj.quoridor.player.dallanoce;

import java.util.ArrayList;

/**
 * Created by dllni on 04/07/2017.
 */
public class Node {

    private Box casella;
    private Boolean isVisited;
    private Node father;
    private ArrayList<Node> neighbors;

    ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    void setNeighbors(ArrayList<Node> neighbors) {
        this.neighbors = neighbors;
    }


    Node getFather() {
        return father;
    }

    void setFather(Node father) {
        this.father = father;
    }


    Box getCasella() {
        return casella;
    }

    void setCasella(Box casella) {
        this.casella = casella;
    }


    boolean isVisited() {
        return isVisited;
    }

    void setVisited(boolean visited) {
        isVisited = visited;
    }


    public Node(Box casella) {
        this.casella = casella;
        this.neighbors = new ArrayList<>(4);
        this.isVisited = false;
    }

    boolean addNeighbor(Node node) {
        return neighbors.add(node);
    }

    boolean removeNeighbor(Node node) {
        return neighbors.removeIf(n -> n.getCasella().equals(node.getCasella()));
    }

    boolean isNeighbor(Node node) {
        return neighbors.contains(node);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.casella.toString() + "v: " + this.isVisited);
        //sb.append(this.father == null ? "// f: null- n:" : this.father.toString()+"- n:");
        for (Node n : neighbors) {
            sb.append(n.getCasella().toString() + "-");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Node)) {
            return false;
        }
        Node n = (Node) obj;
        return this.getCasella().equals(n.getCasella());
    }
}
