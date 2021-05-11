package gj.quoridor.player.dallanoce;

/**
 * Created by dllni on 04/07/2017.
 */
public class Box {

    private int ID;
    private boolean wallSet;
    private int x;
    private int y;

    int getID() {
        return ID;
    }

    void setID(int ID) {
        this.ID = ID;
    }

    boolean isWallSet() {
        return wallSet;
    }

    void setWallSet(boolean wallSet) {
        this.wallSet = wallSet;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }


    Box(int x, int y) {
        this(x, y, -1);
    }

    public Box(int x, int y, int ID) {
        this(x, y, ID, false);
        /*this.x = x;
        this.y = y;
        this.wallSet = false;
        this.ID = ID;*/
    }

    public Box(int x, int y, int ID, boolean wallSet) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.wallSet = wallSet;
    }


    @Override
    public String toString() {
        return this.x + "," + this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Box)) return false;

        Box b = (Box) obj;
        return this.x == b.x && this.y == b.y;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Box(this.getX(), this.getY(), this.getID(), this.wallSet);
    }
}
