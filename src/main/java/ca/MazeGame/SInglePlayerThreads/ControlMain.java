package ca.MazeGame.SInglePlayerThreads;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MultiPlayersThreads.MultiPlayersMoveCatThread;
import ca.MazeGame.Wrappers.MoveUtility;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;

import java.util.concurrent.locks.ReentrantLock;

public class ControlMain extends MoveUtility {

    private MazeGame singleUserGame;
    private ReentrantLock moveLock = new ReentrantLock();

    MoveCatThread moveCatTask;

    public ControlMain(MazeGame singleUserGame) {
        this.singleUserGame = singleUserGame;
        moveCatTask = new MoveCatThread(singleUserGame);
        Thread moveCatThread = new Thread(moveCatTask);
        moveCatThread.start();
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

    public boolean gameNotWonOrLost() {
        return !singleUserGame.hasUserWon() && !singleUserGame.hasUserLost();
    }

    public void increaseTimeInterval() {
        moveCatTask.increaseTimeInterval();
    }

    public void decreaseTimeInterval() {
        moveCatTask.decreaseTimeInterval();
    }

    public void stopThreads() {
        moveCatTask.setMoveCatThreadStop(true);
    }
}
