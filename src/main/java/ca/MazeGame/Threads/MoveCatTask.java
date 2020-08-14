package ca.MazeGame.Threads;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.model.MazeGame;

public class MoveCatTask implements Runnable {
    public MazeGame game;
    private int timeInterval = 1000;
    private boolean threadStop = false;

    public MoveCatTask(MazeGame game) {
        this.game = game;
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

    public void revealBoard() {
        game.displayBoard();
    }

    public boolean isThreadStop() {
        return threadStop;
    }

    public void setThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
    }
}
