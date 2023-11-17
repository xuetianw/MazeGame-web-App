package ca.MazeGame.Wrappers;

import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;
import ca.MazeGame.MazeGames.MazeGame;

import java.util.concurrent.locks.ReentrantLock;

public class ApiGameWrapper {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;


    public static ApiGameWrapper processMaze(MazeGame singleUserGame, long id) {

        ApiGameWrapper apiGameWrapper = new ApiGameWrapper();
        apiGameWrapper.gameNumber = id;
        apiGameWrapper.isGameWon = singleUserGame.hasUserWon();
        apiGameWrapper.isGameLost = singleUserGame.hasUserLost();
        apiGameWrapper.numCheeseFound = singleUserGame.getNumCheeseCollected();
        apiGameWrapper.numCheeseGoal = MazeGame.getNumCheeseToCollect();
        return apiGameWrapper;
    }

}
