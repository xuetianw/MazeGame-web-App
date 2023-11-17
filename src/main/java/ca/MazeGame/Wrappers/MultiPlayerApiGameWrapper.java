package ca.MazeGame.Wrappers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;

import java.util.concurrent.locks.ReentrantLock;

public class MultiPlayerApiGameWrapper {
    public boolean isFirstPlayerWon;
    public boolean isSecondPlayerWon;

    public boolean isFirstPlayerLost;
    public boolean isSecondPlayerLost;

    public boolean isGameWon;

    public boolean isGameLost;
    public int firstPlayerNumCheeseFound;
    public int secondPlayerNumCheeseFound;

    public int numCheeseGoal;
    public Long gameNumber;


    public static MultiPlayerApiGameWrapper processMaze(MultiPlayerMazeGame multiPlayerMazeGame, long id) {

        MultiPlayerApiGameWrapper multiPlayerApiGameWrapper = new MultiPlayerApiGameWrapper();
        multiPlayerApiGameWrapper.gameNumber = id;
        multiPlayerApiGameWrapper.isFirstPlayerWon = multiPlayerMazeGame.hasUserWon();
        multiPlayerApiGameWrapper.isSecondPlayerWon = multiPlayerMazeGame.hasSecondPlayerWon();
        multiPlayerApiGameWrapper.isGameWon = multiPlayerApiGameWrapper.isFirstPlayerWon || multiPlayerApiGameWrapper.isSecondPlayerWon;
        multiPlayerApiGameWrapper.isFirstPlayerLost = multiPlayerMazeGame.hasUserLost();
        multiPlayerApiGameWrapper.isSecondPlayerLost = multiPlayerMazeGame.hasSecondPlayerLost();
        multiPlayerApiGameWrapper.isGameLost = multiPlayerApiGameWrapper.isFirstPlayerLost || multiPlayerApiGameWrapper.isSecondPlayerLost;
        multiPlayerApiGameWrapper.firstPlayerNumCheeseFound = multiPlayerMazeGame.getNumCheeseCollected();
        multiPlayerApiGameWrapper.secondPlayerNumCheeseFound = multiPlayerMazeGame.getSecondPlayerCheeseCollected();
        multiPlayerApiGameWrapper.numCheeseGoal = MultiPlayerMazeGame.getNumCheeseToCollect();

        return multiPlayerApiGameWrapper;
    }
}
