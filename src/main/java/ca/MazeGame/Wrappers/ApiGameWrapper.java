package ca.MazeGame.Wrappers;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;
import ca.MazeGame.MazeGames.MazeGame;

import java.util.concurrent.locks.ReentrantLock;

public class ApiGameWrapper extends MoveUtility implements Runnable {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;
    private boolean threadStop = false;

    private ReentrantLock moveLock = new ReentrantLock();

    public MazeGame singleUserGame;
    public ApiBoardWrapper apiBoardWrapper;
    private int timeInterval = 1000;

    public ApiGameWrapper(MazeGame singleUserGame, long id) {
        apiBoardWrapper = new ApiBoardWrapper(singleUserGame);
        this.singleUserGame = singleUserGame;
        gameNumber = id;
    }


    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public MazeGame getSingleUserGame() {
        return singleUserGame;
    }

    public void setSingleUserGame(MazeGame singleUserGame) {
        this.singleUserGame = singleUserGame;
    }

    public boolean isThreadStop() {
        return threadStop;
    }

    public void setThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
    }

    public void move(String newMove) {
        if (newMove.equals("MOVE_CATS")) {
            moveLock.lock();

            singleUserGame.moveCat();
            doWonOrLost();

            moveLock.unlock();
            return;
        }
        moveLock.lock();

        doPlayerMove(newMove);

        moveLock.unlock();
    }

    @Override
    public void run() {
        while (!singleUserGame.hasUserWon() && !singleUserGame.hasUserLost() && !threadStop) {
//            System.out.println(game.toString());
            try {
                singleUserGame.moveCat();
                doWonOrLost();
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doPlayerMove(String arrow) {
        Direction move = getPlayerMove(arrow);
        if (!singleUserGame.isValidPlayerMove(move)) {
            RuntimeException copy = new InvalidMoveException("new location on the wall");
            moveLock.unlock();
            throw copy;
        } else {
            singleUserGame.recordPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }


    public boolean gameNotWonOrLost() {
        return !singleUserGame.hasUserWon() && !singleUserGame.hasUserLost();
    }

    public void doWonOrLost() {
//        System.out.println("called");
        if (singleUserGame.hasUserWon()) {
            revealBoard();
        } else if (singleUserGame.hasUserLost()) {
			revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        singleUserGame.displayBoard();
    }

    public ApiGameWrapper processMaze() {
        isGameWon = singleUserGame.hasUserWon();
        isGameLost = singleUserGame.hasUserLost();
        numCheeseFound = singleUserGame.getNumCheeseCollected();
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
