package gj.quoridor.player.dallanoce;

import gj.quoridor.player.Player;

/**
 * Created by dllni on 05/07/2017.
 */
public class DallaNocePlayer implements Player {

    private GameState gameState;
    private AI ai;

    @Override
    public void start(boolean b) {
        boolean isRed = b;
        gj.quoridor.player.dallanoce.Player opponent;
        gj.quoridor.player.dallanoce.Player bot;
        int botGoalLine;
        int opponentGoalLine;
        if (isRed) {
            botGoalLine = 8;
            bot = new gj.quoridor.player.dallanoce.Player(new Box(0, 4), 10, botGoalLine, true);
            opponentGoalLine = 0;
            opponent = new gj.quoridor.player.dallanoce.Player(new Box(8, 4), 10, opponentGoalLine, false);
        } else {
            botGoalLine = 0;
            bot = new gj.quoridor.player.dallanoce.Player(new Box(8, 4), 10, botGoalLine, false);
            opponentGoalLine = 8;
            opponent = new gj.quoridor.player.dallanoce.Player(new Box(0, 4), 10, opponentGoalLine, true);
        }
        gameState = new GameState(new Table(9), bot, opponent);
        ai = new AI(gameState);
    }

    @Override
    public int[] move() {
        ai.move();
        Move m = gameState.getBot().getMove();
        gameState.updateBot(m);
        if (m.placeWall()) {
            ai.placeWall(m.wallNumber());
        }
        return m.getMove();
    }

    @Override
    public void tellMove(int[] ints) {
        Move opp = new Move(ints);
        gameState.getOpponent().getLastMoves().add(opp);
        if (opp.placeWall()) {
            int[] c = Translate.translateWallNumberToCordinates(opp.wallNumber(), gameState.getT());
            gameState.getT().insertWall(c);
            gameState.getG().removeEdgeByWallCordinates(c[0], c[1], c[2]);
        }
        gameState.updateOpponent(new Move(ints));

    }
}
