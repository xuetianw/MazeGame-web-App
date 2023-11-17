package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.Wrappers.MoveUtility;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;

import java.util.concurrent.locks.ReentrantLock;

public class MultiPlayersControlMain extends MoveUtility {

    private MultiPlayerMazeGame multiPlayerMazeGame;
    private ReentrantLock moveLock = new ReentrantLock();

    private MultiPlayersMoveCatTask moveCatTask;
    private ComputePCLocationTask computePCLocationTask;

    public MultiPlayersControlMain(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        this.moveCatTask = new MultiPlayersMoveCatTask(multiPlayerMazeGame);
        Thread moveCatThread = new Thread(moveCatTask);
        moveCatThread.start();

        computePCLocationTask = new ComputePCLocationTask(multiPlayerMazeGame);
        Thread computePCLocThread = new Thread(computePCLocationTask);
        computePCLocThread.start();

    }


    public void stopThreads() {
        moveCatTask.setMoveCatThreadStop(true);
        computePCLocationTask.setPCThreadStop(true);
    }

    public void move(String newMove) {
        if (newMove.equals("MOVE_CATS")) {
            moveLock.lock();

            multiPlayerMazeGame.moveCat();
            doWonOrLost();

            moveLock.unlock();
            return;
        }

        moveLock.lock();

        doPlayerMove(newMove);

        moveLock.unlock();
    }


    public void doWonOrLost() {
//        System.out.println("called");
        if (multiPlayerMazeGame.hasUserWon()) {
            revealBoard();
        } else if (multiPlayerMazeGame.hasUserLost()) {
            revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        multiPlayerMazeGame.displayBoard();
    }

    public void doPlayerMove(String arrow) {
        Direction move = getPlayerMove(arrow);
        if (!multiPlayerMazeGame.isValidPlayerMove(move)) {
            RuntimeException copy = new InvalidMoveException("new location on the wall");
            moveLock.unlock();
            throw copy;
        } else {
            multiPlayerMazeGame.recordPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }

    public boolean gameNotWonOrLost() {
        return !multiPlayerMazeGame.hasUserWon() && !multiPlayerMazeGame.hasUserLost();
    }
}
