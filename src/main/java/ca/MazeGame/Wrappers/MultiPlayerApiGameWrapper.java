package ca.MazeGame.Wrappers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;

public class MultiPlayerApiGameWrapper extends MoveUtility implements Runnable  {
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
    private boolean threadStop = false;


    MultiPlayerMazeGame multiPlayerMazeGame;
    public MultiPlayerApiBoardWrapper apiBoardWrapper;
    private int timeInterval = 1000;



    public MultiPlayerApiGameWrapper(MultiPlayerMazeGame multiPlayerMazeGame, long id) {
        apiBoardWrapper = new MultiPlayerApiBoardWrapper(multiPlayerMazeGame);
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        gameNumber = id;
    }



    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public MazeGame getMultiPlayerMazeGame() {
        return multiPlayerMazeGame;
    }

    public void setMultiPlayerMazeGame(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
    }


    public boolean isThreadStop() {
        return threadStop;
    }

    public void setThreadStop(boolean threadStop) {
        this.threadStop = threadStop;
    }

    public void move(String newMove) {
        if (newMove.equals("MOVE_CATS")) {
            multiPlayerMazeGame.moveCat();
            doWonOrLost();
            return;
        }
        doPlayerMove(newMove);
    }

    @Override
    public void run() {
        while (!multiPlayerMazeGame.hasUserWon() && !multiPlayerMazeGame.hasUserLost() && !threadStop) {
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

    public void doPlayerMove(String arrow) {
        Direction move = getPlayerMove(arrow);
        if (!multiPlayerMazeGame.isValidPlayerMove(move)) {
            throw new InvalidMoveException("new location on the wall");
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

    public MultiPlayerApiGameWrapper processMaze() {
        isFirstPlayerWon = multiPlayerMazeGame.hasUserWon();
        isSecondPlayerWon = multiPlayerMazeGame.hasSecondPlayerWon();

        isGameWon = isFirstPlayerWon || isSecondPlayerWon;

        isFirstPlayerLost = multiPlayerMazeGame.hasUserLost();
        isSecondPlayerLost = multiPlayerMazeGame.hasSecondPlayerLost();

        isGameLost = isFirstPlayerLost || isSecondPlayerLost;

        firstPlayerNumCheeseFound = multiPlayerMazeGame.getNumCheeseCollected();
        secondPlayerNumCheeseFound = multiPlayerMazeGame.getSecondPlayerCheeseCollected();

        numCheeseGoal = MazeGame.getNumCheeseToCollect();
        return this;
    }

}
