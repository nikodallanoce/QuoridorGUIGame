package gj.quoridor.player.dallanoce;

/**
 * Created by dllni on 04/07/2017.
 */
public class Table {

    private Box[][] scacchiera;

    Box[][] getScacchiera() {
        return scacchiera;
    }

    void setScacchiera(Box[][] scacchiera) {
        this.scacchiera = scacchiera;
    }


    public Table(int size) {
        this.scacchiera = new Box[size * 2 - 1][size * 2 - 1];
        initializeTable();
    }

    private void initializeTable() {
        Box b;
        int wallCount = 0;
        for (int i = 0; i < scacchiera.length; i++) {
            for (int j = 0; j < scacchiera.length; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        b = new Box(i, j, -10);
                    } else {
                        b = new Box(i, j, wallCount);
                        wallCount++;
                    }
                } else {
                    if (j % 2 == 0 && j != scacchiera.length - 1) {
                        b = new Box(i, j, wallCount);
                        wallCount++;
                    } else {
                        b = new Box(i, j, -1);
                    }
                }
                scacchiera[i][j] = b;
            }
        }
    }

    boolean insertWall(int[] xyverso) {

        int x = xyverso[0];
        int y = xyverso[1];
        int verso = xyverso[2]; // 0 horizontal, 1 vertical
        if (verso == 0) {
            for (int i = 0; i < 3; i++) {
                if (scacchiera[x * 2 + 1][y * 2 + i].isWallSet())
                    return false;
            }
            scacchiera[x * 2 + 1][y * 2].setWallSet(true);
            scacchiera[x * 2 + 1][y * 2 + 1].setWallSet(true);
            scacchiera[x * 2 + 1][y * 2 + 2].setWallSet(true);
        } else {
            for (int i = 0; i < 3; i++) {
                if (scacchiera[x * 2 + i][y * 2 + 1].isWallSet()) {
                    return false;
                }
            }
            scacchiera[x * 2][y * 2 + 1].setWallSet(true);
            scacchiera[x * 2 + 1][y * 2 + 1].setWallSet(true);
            scacchiera[x * 2 + 2][y * 2 + 1].setWallSet(true);
        }
        return true;
    }

    void removeWall(int[] xyverso)
    {
        int x = xyverso[0];
        int y = xyverso[1];
        int verso = xyverso[2]; // 0 horizontal, 1 vertical
        if(verso==0)
        {
            scacchiera[x * 2 + 1][y * 2].setWallSet(false);
            scacchiera[x * 2 + 1][y * 2 + 1].setWallSet(false);
            scacchiera[x * 2 + 1][y * 2 + 2].setWallSet(false);
        }
        else {
            scacchiera[x * 2][y * 2 + 1].setWallSet(false);
            scacchiera[x * 2 + 1][y * 2 + 1].setWallSet(false);
            scacchiera[x * 2 + 2][y * 2 + 1].setWallSet(false);
        }

    }


}
