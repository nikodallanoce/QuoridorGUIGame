package gj.quoridor.player.dallanoce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dllni on 05/07/2017.
 */
public class AI {

    private GameState gameState;
    private Move bestMove;
    private int c = 0;


    public AI(GameState gameState) {
        this.gameState = gameState;
    }


    private List<Box> shortestPath(List<List<Box>> paths) {
        return Collections.min(paths, Comparator.comparingInt(value -> value.size()));
    }

    private List<Box> longestPath(List<List<Box>> paths) {
        return Collections.max(paths, Comparator.comparingInt(value -> value.size()));
    }

    private List<List<Box>> findPaths(Node start, Player p) {
        List<List<Box>> paths = new ArrayList<>();

        for (int i = 0; i < Math.sqrt(gameState.getG().getAdjacencyList().length); i++) {
            List<Box> sPath = gameState.getG().breadthFirstSearch(start, new Node(new Box(p.getGoalLine(), i)));
            if (!sPath.isEmpty() && !thereIsDuplicateGoalLineBoxInPath(sPath, p)) {
                paths.add(sPath);
            }
        }
        return paths;
    }


    private boolean thereIsDuplicateGoalLineBoxInPath(List<Box> path, Player p) {
        if (path.size() < 3) return false;

        Box last = path.get(0);
        Box penultimate = path.get(1);

        if (last.getX() == p.getGoalLine() && penultimate.getX() == p.getGoalLine()) {
            return true;
        }
        return false;
    }

    boolean placeWall(int wallNumber) {
        int[] xyverso = Translate.translateWallNumberToCordinates(wallNumber, gameState.getT());
        int x = xyverso[0];
        int y = xyverso[1];
        int verso = xyverso[2];

        boolean placeable = gameState.getT().insertWall(xyverso);
        if (placeable) {
            gameState.getG().removeEdgeByWallCordinates(x, y, verso);
            boolean okBot = hasAtLeastAPath(gameState.getBot());
            boolean okOpponent = hasAtLeastAPath(gameState.getOpponent());
            if (!(okBot && okOpponent)) {
                gameState.getT().removeWall(xyverso);
                gameState.getG().addEdgeByWallCordinates(x, y, verso);
                return false;
            }
        }
        return placeable;
    }

    void removeWall(int wallNumber) {
        int[] xyverso = Translate.translateWallNumberToCordinates(wallNumber, gameState.getT());
        int x = xyverso[0];
        int y = xyverso[1];
        int verso = xyverso[2];

        gameState.getT().removeWall(xyverso);
        gameState.getG().addEdgeByWallCordinates(x, y, verso);
    }

    boolean hasAtLeastAPath(Player current) {

        return gameState.getG().isGraphConnected(new Node(current.getLocation()), current.getGoalLine());
    }

    List<Move> generateMoves(Player player) {
        List<Move> moves = new ArrayList<>(100);
        Move move;
        for (int i = 0; i < 4; i++) {
            move = new Move(new int[]{0, i});
            if (isPossibleMove(move, player)) {
                moves.add(move);
            }
        }
        if (player.getAvailableWalls() > 0) {
            for (int i = 0; i < 128; i++) {
                move = new Move(new int[]{1, i});
                if (isPossibleMove(move, player)) {
                    //gameState.getT().removeWall(Translate.translateWallNumberToCordinates(i,gameState.getT()));
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private boolean isPossibleMove(Move move, Player p) {
        if (move.isaMovement()) {
            Box origin = p.getLocation();
            Box next = origin;
            try {
                next = Translate.translateMoveToDestinationBox(origin, move.movement(), p.isRed());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gameState.getG().areNeighbor(new Node(origin), new Node(next));
        } else {
            int wallNumber = move.wallNumber();
            if (placeWall(wallNumber)) {
                removeWall(wallNumber);
                return true;
            }
            return false;
        }
    }

    void move() {

        if (gameState.getBot().getAvailableWalls() == 0 || c < 3 || opponentRepeatMoveLR()) {
            Player bot = gameState.getBot();
            List<Box> sp = shortestPath(findPaths(new Node(bot.getLocation()), bot));
            Box dest = sp.get(sp.size() - 1);
            int dir = Translate.translateNextBoxToDirection(bot.getLocation(), dest, bot.isRed());
            bestMove = new Move(new int[]{0, dir});
            c++;
        } else {
            minimaxAlphaBeta(2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        }
        if (gameState.getOpponent().getLastMoves().size() > 1) {
            gameState.getOpponent().getLastMoves().remove(0);
        }

        gameState.getBot().setMove(bestMove);
    }

    boolean opponentRepeatMoveLR() {
        List<Move> oppLM = gameState.getOpponent().getLastMoves();
        if (oppLM.size() < 2) {
            return false;
        }
        Move m1 = oppLM.get(0);
        Move m2 = oppLM.get(oppLM.size() - 1);
        if (m1.isaMovement() && m2.isaMovement()) {
            if ((m1.movement() == 3 && m2.movement() == 2) || (m1.movement() == 2 && m2.movement() == 3)) {
                return true;
            }
        }
        return false;
    }

    public int minimaxAlphaBeta(int depth, int alpha, int beta, boolean maxPlayer) {
        if (depth == 0) {
            int heuristic = heuristic();
            return heuristic;
        }
        boolean wallPlaced = false;
        if (maxPlayer) {

            int val;
            List<Move> moves = gameState.generatePossibleMoves(gameState.getBot());
            for (Move move : moves) {
                if (move.isaMovement()) {
                    gameState.updateBot(move);
                } else {
                    wallPlaced = placeWall(move.wallNumber());
                    gameState.getBot().decreaseWalls();
                }
                val = minimaxAlphaBeta(depth - 1, alpha, beta, false);
                Move om = undoMove(move, wallPlaced);
                if (om != null) {
                    gameState.updateBot(om);
                } else {
                    gameState.getBot().increaseWalls();
                }
                if (val > alpha) {
                    alpha = val;
                    bestMove = move;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return alpha;
        } else {
            List<Move> moves = gameState.generatePossibleMoves(gameState.getOpponent());
            for (Move move : moves) {
                if (move.isaMovement()) {
                    gameState.updateOpponent(move);
                } else {
                    wallPlaced = placeWall(move.wallNumber());
                    gameState.getOpponent().decreaseWalls();
                }
                beta = Math.min(beta, minimaxAlphaBeta(depth - 1, alpha, beta, true));
                Move om = undoMove(move, wallPlaced);
                if (om != null) {
                    gameState.updateOpponent(om);
                } else {
                    gameState.getOpponent().increaseWalls();
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return beta;
        }
    }

    private boolean playerHasWon(Player p) {
        return p.getGoalLine() == p.getLocation().getX();
    }


    private Move undoMove(Move move, boolean wallPlaced) {
        Move oppositeMove = null;
        if (move.isaMovement()) {
            switch (move.movement()) {
                case 0:
                    oppositeMove = new Move(new int[]{0, 1});
                    break;
                case 1:
                    oppositeMove = new Move(new int[]{0, 0});
                    break;
                case 2:
                    oppositeMove = new Move(new int[]{0, 3});
                    break;
                case 3:
                    oppositeMove = new Move(new int[]{0, 2});
                    break;
            }
        } else {
            removeWall(move.wallNumber());
        }
        return oppositeMove;
    }

    int heuristic() {
        Player bot = gameState.getBot();
        Player opponent = gameState.getOpponent();
        if (playerHasWon(opponent)) {
            return Integer.MIN_VALUE + 10;
        } else if (playerHasWon(bot)) {
            return Integer.MAX_VALUE - 10;
        }

        List<List<Box>> opponentPaths = findPaths(new Node(opponent.getLocation()), opponent);
        List<List<Box>> botPaths = findPaths(new Node(bot.getLocation()), bot);

        int botMinPath = shortestPath(botPaths).size();
        int opponentMinPath = shortestPath(opponentPaths).size();
        int botWalls = bot.getAvailableWalls();
        int opponentWalls = opponent.getAvailableWalls();
        int opponentLongestPath = longestPath(opponentPaths).size();
        int botLongestPath = longestPath(botPaths).size();
        c++;
        if (c == 3 && !gameState.getBot().isRed()) {
            botMinPath = botMinPath + 2;
            opponentMinPath = opponentMinPath - 2;
        }

        int h = (opponentMinPath - botMinPath) * 10 + (opponentLongestPath - botLongestPath) * 6;

        return h;
    }


}
