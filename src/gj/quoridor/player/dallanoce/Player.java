package gj.quoridor.player.dallanoce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dllni on 12/07/2017.
 */
public class Player {

    private Box location;
    private int availableWalls;
    private int goalLine;
    private List<Move> lastMoves;
    private boolean isRed;
    private Move move;

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public List<Move> getLastMoves() {
        return lastMoves;
    }

    public void setLastMoves(List<Move> lastMoves) {
        this.lastMoves = lastMoves;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public int getGoalLine() {
        return goalLine;
    }

    public void setGoalLine(int goalLine) {
        this.goalLine = goalLine;
    }

    public Box getLocation() {
        return location;
    }

    public void setLocation(Box location) {
        this.location = location;
    }

    public int getAvailableWalls() {
        return availableWalls;
    }

    public void decreaseWalls() {
        this.availableWalls--;
    }

    public void increaseWalls() {
        this.availableWalls++;
    }

    public Player(Box location, int availableWalls, int goalLine, boolean isRed) {

        this.location = location;
        this.availableWalls = availableWalls;
        this.goalLine = goalLine;
        this.isRed = isRed;
        this.lastMoves = new ArrayList<>();
    }
}
