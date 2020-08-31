package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.Wrappers.MoveUtility;

import java.util.concurrent.locks.ReentrantLock;

public class MultiPlayersMoveCatThread extends MoveUtility implements Runnable {
    private MultiPlayerMazeGame multiPlayerMazeGame;

    private int timeInterval = 1000;
    private boolean threadStop = false;
    private ReentrantLock moveLock = new ReentrantLock();

    public MultiPlayersMoveCatThread(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
    }

    public boolean isThreadStop() {
        return threadStop;
    }

    public void setMoveCatThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
    }

    public void doWonOrLost() {
//        System.out.println("called");
        if (multiPlayerMazeGame.hasAnyUserWon()) {
            revealBoard();
        } else if (multiPlayerMazeGame.hasAnyUserLost()) {
            revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        multiPlayerMazeGame.displayBoard();
    }


    @Override
    public void run() {
        while (!multiPlayerMazeGame.hasAnyUserWon() && !multiPlayerMazeGame.hasAnyUserLost() && !threadStop) {
//            System.out.println(game.toString());
            try {
                multiPlayerMazeGame.moveCat();
                doWonOrLost();
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
