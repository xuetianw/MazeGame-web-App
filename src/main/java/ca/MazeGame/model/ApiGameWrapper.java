package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;


public class ApiGameWrapper implements Runnable {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;
    public MazeGame game;
    public ApiBoardWrapper apiBoardWrapper;

    public ApiGameWrapper(MazeGame game, long id) {
        apiBoardWrapper = new ApiBoardWrapper(game);
        this.game = game;
        gameNumber = id;
    }

    public ApiGameWrapper(boolean isGameWon, boolean isGameLost, int numCheeseFound, int numCheeseGoal, Long gameNumber, MazeGame game) {
        this.isGameWon = isGameWon;
        this.isGameLost = isGameLost;
        this.numCheeseFound = numCheeseFound;
        this.numCheeseGoal = numCheeseGoal;
        this.gameNumber = gameNumber;
        this.game = game;
    }


    public void move(String newMove) {
        if (newMove.equals("MOVE_CATS")) {
            game.moveCat();
            doWonOrLost();
            return;
        }
        doPlayerMove(newMove);
    }

    @Override
    public void run() {
        while (!game.hasUserWon() && !game.hasUserLost()) {
            try {
                game.moveCat();
                doWonOrLost();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doPlayerMove(String arrow) {
        Direction move = Direction.NOT_MOVING;
        move = getPlayerMove(arrow);
        if (!game.isValidPlayerMove(move)) {
            throw new InvalidMoveException("new location on the wall");
        } else {
            game.recordPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }

    Direction getPlayerMove(String newMove) {
        switch (newMove) {
            case "MOVE_LEFT":
                return Direction.MOVE_LEFT;
            case "MOVE_UP":
                return Direction.MOVE_UP;
            case "MOVE_RIGHT":
                return Direction.MOVE_RIGHT;
            case "MOVE_DOWN":
                return Direction.MOVE_DOWN;
            default:
                throw new BadRequestException("NoSuchMove");
        }
    }
    public boolean gameNotWonOrLost() {
        return !game.hasUserWon() && !game.hasUserLost();
    }

    public void doWonOrLost() {
        if (game.hasUserWon()) {
            revealBoard();
        } else if (game.hasUserLost()) {
			revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        game.displayBoard();
    }

    public ApiGameWrapper processMaze() {
        isGameWon = game.hasUserWon();
        isGameLost = game.hasUserLost();
        numCheeseFound = game.getNumCheeseCollected();
        numCheeseGoal = MazeGame.getNumCheeseToCollect();
        return this;
    }
}
