package gj.quoridor.player.dallanoce;

/**
 * Created by dllni on 05/07/2017.
 */
public class Translate {

    static Box translateMoveToDestinationBox(Box current, int direction, boolean isRed) throws CloneNotSupportedException {

        Box next = (Box) current.clone();

        if (direction == 0) if (isRed) next.setX(next.getX() + 1);
        else next.setX(next.getX() - 1);
        else if (direction == 1) if (isRed) next.setX(next.getX() - 1);
        else next.setX(next.getX() + 1);
        else if (direction == 2) if (isRed) next.setY(next.getY() + 1);
        else next.setY(next.getY() - 1);
        else if (direction == 3) if (isRed) next.setY(next.getY() - 1);
        else next.setY(next.getY() + 1);
        return next;
    }


    static int translateNextBoxToDirection(Box current, Box next, boolean isRed) {
        if (next.getX() > current.getX()) if (isRed) return 0;
        else return 1;

        else if (next.getX() < current.getX()) if (isRed) return 1;
        else return 0;

        else if (next.getY() > current.getY()) if (isRed) return 2;
        else return 3;

        else if (next.getY() < current.getY()) if (isRed) return 3;
        else return 2;
        throw new Error();
    }

    static int[] translateWallNumberToCordinates(int wallNumber, Table scacchiera) {
        int x;
        int y;
        int verso;
        if (isHorizontallWall(wallNumber, scacchiera)) { //horizontall
            x = ((wallNumber / 8) - 1) / 2;
            y = wallNumber - (2 * x + 1) * 8;
            verso = 0;
        } else { //vertical
            x = (wallNumber / 8) / 2;
            y = wallNumber - 2 * x * 8;
            verso = 1;
        }

        return new int[]{x, y, verso};
    }

    private static boolean isHorizontallWall(int wallNumber, Table scacchiera) {
        for (int i = 0; i < scacchiera.getScacchiera().length / 2; i++) {
            if (wallNumber >= (2 * i + 1) * 8 && wallNumber <= ((2 * i + 1) * 8 + 7)) {
                return true; //horizontall
            }
        }
        return false; //vertical
    }

}
