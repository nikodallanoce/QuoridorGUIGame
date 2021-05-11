package gj.quoridor.player.dallanoce;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by dllni on 04/07/2017.
 */
public class Graph {

    private Node[] adjacencyList;
    private Node start;
    private Table table;

    Node[] getAdjacencyList() {
        return adjacencyList;
    }

    void setAdjacencyList(Node[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    Table getTable() {
        return table;
    }

    void setTable(Table table) {
        this.table = table;
    }

    Node getStart() {
        return start;
    }

    void setStart(Node start) {
        this.start = start;
    }

    public Graph(Table table) {
        adjacencyList = new Node[81];
        this.table = table;
        adjacencyListNeighbors();
    }

    private void createAdjacencyList() {
        Box[][] scacchiera = table.getScacchiera();
        int index = 0;

        for (int i = 0; i < scacchiera.length; i = i + 2) {
            for (int j = 0; j < scacchiera[0].length; j = j + 2) {
                //adjacencyList[index] = new Node(scacchiera[i][j]);
                adjacencyList[index] = new Node(new Box(scacchiera[i][j].getX(), scacchiera[i][j].getY()));
                adjacencyList[index].getCasella().setX(i / 2);
                adjacencyList[index].getCasella().setY(j / 2);
                index++;
            }
        }
    }

    private void adjacencyListNeighbors() {
        createAdjacencyList();
        Box[][] scacchiera = table.getScacchiera();
        int index = 0;
        for (int i = 0; i < scacchiera.length; i = i + 2) {
            for (int j = 0; j < scacchiera[0].length; j = j + 2) {
                if (isGoodNeighbor(i, j + 2))
                    adjacencyList[index].addNeighbor(adjacencyList[(i / 2) * 9 + (j / 2 + 1)]);
                if (isGoodNeighbor(i, j - 2))
                    adjacencyList[index].addNeighbor(adjacencyList[(i / 2) * 9 + (j / 2 - 1)]);
                if (isGoodNeighbor(i + 2, j)) adjacencyList[index].addNeighbor(adjacencyList[(i / 2 + 1) * 9 + j / 2]);
                if (isGoodNeighbor(i - 2, j)) adjacencyList[index].addNeighbor(adjacencyList[(i / 2 - 1) * 9 + j / 2]);
                index++;
            }
        }
    }

    private boolean isGoodNeighbor(int x, int y) {
        Box[][] scacchiera = table.getScacchiera();
        if (x < 0 || x >= scacchiera.length || y < 0 || y >= scacchiera.length) return false;
        return true;
    }

    List<Box> breadthFirstSearch(Node start, Node goal) {
        List<Box> shortestPath = new ArrayList<>(30);
        Node current = targetNode(start, goal);
        if (current == null) return new LinkedList<>();

        while (!start.getCasella().equals(current.getCasella())) {
            shortestPath.add(current.getCasella());
            current = current.getFather();
        }

        return shortestPath;
    }

    Node targetNode(Node start, Node goal) {
        Queue<Node> queue = new LinkedList<>();
        int index = start.getCasella().getX() * 9 + start.getCasella().getY();
        adjacencyList[index].setVisited(true);
        Node current = adjacencyList[index];
        queue.add(current);

        while (!current.equals(goal) && !(queue.isEmpty())) {
            current = queue.remove();
            queueAdd(current, queue);
        }
        uncheckAll();
        return current.equals(goal) ? current : null;
    }

    boolean isGraphConnected(Node start, int goalLine) {
        Queue<Node> queue = new LinkedList<>();
        int index = start.getCasella().getX() * 9 + start.getCasella().getY();
        adjacencyList[index].setVisited(true);
        Node current = adjacencyList[index];
        queue.add(current);
        boolean con = false;

        while (current.getCasella().getX() != goalLine && !(queue.isEmpty())) {
            current = queue.remove();
            if (current.getCasella().getX() == goalLine) {
                con = true;
                break;
            }
            queueAdd(current, queue);
        }
        uncheckAll();
        return con;
    }

    private void queueAdd(Node current, Queue queue) {
        int index = current.getCasella().getX() * 9 + current.getCasella().getY();
        for (Node n : adjacencyList[index].getNeighbors()) {
            if (!n.isVisited()) {
                n.setFather(current);
                n.setVisited(true);
                queue.add(n);
            }
        }
    }

    private void uncheckAll() {
        for (Node n : adjacencyList) {
            n.setVisited(false);
        }

    }

    void removeEdgeByWallCordinates(int x, int y, int verso) {
        Node n = new Node(new Box(x, y));

        if (verso == 0) { //horizontal
            removeEdge(n, new Node(new Box(x + 1, y)));
            removeEdge(new Node(new Box(x, y + 1)), new Node(new Box(x + 1, y + 1)));
        } else //vertical
        {
            removeEdge(n, new Node(new Box(x, y + 1)));
            removeEdge(new Node(new Box(x + 1, y)), new Node(new Box(x + 1, y + 1)));
        }
    }

    public void removeEdge(Node n1, Node n2) {
        int index1 = refInAdjacencyListOf(n1);
        int index2 = refInAdjacencyListOf(n2);
        adjacencyList[index1].removeNeighbor(n2);
        adjacencyList[index2].removeNeighbor(n1);
    }

    void addEdgeByWallCordinates(int x, int y, int verso) {
        Node n = new Node(new Box(x, y));
        if (verso == 0) { //horizontal
            addEdge(n, new Node(new Box(x + 1, y)));
            addEdge(new Node(new Box(x, y + 1)), new Node(new Box(x + 1, y + 1)));
        } else //vertical
        {
            addEdge(n, new Node(new Box(x, y + 1)));
            addEdge(new Node(new Box(x + 1, y)), new Node(new Box(x + 1, y + 1)));
        }
    }

    private int refInAdjacencyListOf(Node node) {
        return node.getCasella().getX() * 9 + node.getCasella().getY();
    }

    void addEdge(Node n1, Node n2) {   //needs test
        int index1 = refInAdjacencyListOf(n1);
        int index2 = refInAdjacencyListOf(n2);

        adjacencyList[index1].addNeighbor(adjacencyList[index2]);
        adjacencyList[index2].addNeighbor(adjacencyList[index1]);
    }

    public boolean areNeighbor(Node n1, Node n2) {
        int index1 = refInAdjacencyListOf(n1);
        return adjacencyList[index1].isNeighbor(n2);
    }

}
