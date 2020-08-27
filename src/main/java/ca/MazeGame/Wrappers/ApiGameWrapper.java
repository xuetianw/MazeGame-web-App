package ca.MazeGame.Wrappers;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;
import ca.MazeGame.MazeGames.MazeGame;

public class ApiGameWrapper extends MoveUtility implements Runnable {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;
    private boolean threadStop = false;

    public MazeGame game;
    public ApiBoardWrapper apiBoardWrapper;
    private int timeInterval = 1000;

    public ApiGameWrapper(MazeGame game, long id) {
        apiBoardWrapper = new ApiBoardWrapper(game);
        this.game = game;
        gameNumber = id;
    }


    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public MazeGame getGame() {
        return game;
    }

    public void setGame(MazeGame game) {
        this.game = game;
    }

    public boolean isThreadStop() {
        return threadStop;
    }

    public void setThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
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
        while (!game.hasUserWon() && !game.hasUserLost() && !threadStop) {
//            System.out.println(game.toString());
            try {
                game.moveCat();
                doWonOrLost();
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doPlayerMove(String arrow) {
        Direction move = getPlayerMove(arrow);
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


    public boolean gameNotWonOrLost() {
        return !game.hasUserWon() && !game.hasUserLost();
    }

    public void doWonOrLost() {
//        System.out.println("called");
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

    public void decreaseTimeInterval() {
        if (timeInterval >= 200) {
            timeInterval -= 50;
        } else {
            throw new BadRequestException("the limit has been reached");
        }

    }

    public void increaseTimeInterval() {
        if (timeInterval <= 1000) {
            timeInterval += 50;
        } else {
            throw new BadRequestException("the limit has been reached");
        }
    }
}
