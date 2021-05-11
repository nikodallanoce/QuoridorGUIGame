package gj.quoridor.player.dallanoce;

import java.util.List;

/**
 * Created by dllni on 11/07/2017.
 */
public class GameState {

    private Graph g;
    private Table t;
    private Player bot;
    private Player opponent;

    public Graph getG() {
        return g;
    }

    public void setG(Graph g) {
        this.g = g;
    }

    public Table getT() {
        return t;
    }

    public void setT(Table t) {
        this.t = t;
    }

    public Player getBot() {
        return bot;
    }

    public void setBot(Player bot) {
        this.bot = bot;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    GameState(Table t, Player bot, Player opponent) {
        this.t = t;
        this.bot = bot;
        this.opponent = opponent;
        this.g = new Graph(t);
        //possibleMoves = new ArrayList<>(100);
    }

    GameState(GameState current) {
        this(current.getT(), current.getBot(), current.getOpponent());
    }

    List<Move> generatePossibleMoves(Player player) {
        AI ai = new AI(this);
        return ai.generateMoves(player);
    }

    void updateOpponent(Move oppMove) {
        opponent.setMove(oppMove);
        if (oppMove.placeWall()) {
            opponent.decreaseWalls();
            return;
        }
        try {
            if (oppMove.isaMovement())
                opponent.setLocation(Translate.translateMoveToDestinationBox(opponent.getLocation(), oppMove.movement(), opponent.isRed()));
        } catch (Exception e) {
        }
    }

    void updateBot(Move move) {
        if (move.placeWall()) {
            bot.decreaseWalls();
            return;
        }
        try {
            bot.setLocation(Translate.translateMoveToDestinationBox(bot.getLocation(), move.movement(), bot.isRed()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
