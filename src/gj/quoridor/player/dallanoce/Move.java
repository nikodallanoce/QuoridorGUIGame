package gj.quoridor.player.dallanoce;

/**
 * Created by dllni on 20/07/2017.
 */
public class Move {

    private int[] move;

    int[] getMove() {
        return move;
    }

    void setMove(int[] move) {
        this.move = move;
    }

    Move(int[] move) {
        this.move = move;
    }

    boolean placeWall() {
        return move[0] == 1;
    }

    boolean isaMovement() {
        return move[0] == 0;
    }

    int movement() {
        if (isaMovement()) {
            return move[1];
        } else {
            throw new Error("this move is not a movement");
        }
    }

    int wallNumber() {
        if (placeWall()) {
            return move[1];
        } else {
            throw new Error("this move do not place a wall");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Move)) {
            return false;
        }
        Move m = (Move) obj;

        return this.getMove()[0] == m.getMove()[0] && this.getMove()[1] == m.getMove()[1];
    }
}
