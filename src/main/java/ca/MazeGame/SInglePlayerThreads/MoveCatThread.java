package ca.MazeGame.SInglePlayerThreads;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MultiPlayersThreads.MultiPlayersMoveCatThread;
import ca.MazeGame.Wrappers.ApiGameWrapper;
import ca.MazeGame.exception.BadRequestException;

public class MoveCatThread implements Runnable {

    private boolean threadStop = false;

    public MazeGame singleUserGame;
    private int timeInterval = 1000;

    public MoveCatThread(MazeGame mazeGame) {
        this.singleUserGame = mazeGame;
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

    public void setMoveCatThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
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
