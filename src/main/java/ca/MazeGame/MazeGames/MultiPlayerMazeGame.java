package ca.MazeGame.MazeGames;

import ca.MazeGame.Observer.Observer;
import ca.MazeGame.model.*;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerMazeGame extends MazeGame {

    private List<Observer> observers = new ArrayList<>();

    public static final int NUM_CHEESE_TO_COLLECT = 5;

    private int secondPlayerCheeseCollected;

    private CellLocation pcCellLocation;

    public CellLocation getPCCellLocation() {
        return pcCellLocation;
    }

    public void setPCPlayer(CellLocation pcPlayer) {
        this.pcCellLocation = pcPlayer;
    }

    public MultiPlayerMazeGame() {
        super();
        placeSecondPlayer();
    }

    public void placeSecondPlayer() {
        pcCellLocation = new CellLocation(boardWidth - 2, boardHeight - 2);
    }

    @Override
    public void recordPlayerMove(Direction move) {
        assert isValidPlayerMove(move);
        playerLocation = playerLocation.getTargetLocation(move);

        setVisibleAroundPlayerCell(playerLocation);

        // Compute goal states achieved
        if (isCheeseAtLocation(playerLocation)) {
            numCheeseCollected++;
            placeNewCheeseOnBoard();
            notifyAllObservers();
        }
    }


    public void recordPCPlayerLoc(CellLocation location) {
        pcCellLocation = location;
        if (isCheeseAtLocation(pcCellLocation)) {
            secondPlayerCheeseCollected++;
            placeNewCheeseOnBoard();
            notifyAllObservers();
        }
    }


    private void placeNewCheeseOnBoard() {
        do {
            cheeseLocation = maze.getRandomLocationInsideMaze();
        } while (isMouseAtLocation(cheeseLocation) || (isSecondPlayerAtLocation(cheeseLocation))
                || maze.isCellAWall(cheeseLocation));
    }


    public boolean hasAnyUserWon() {
        boolean collectedEnoughCheese = numCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        boolean secondPlayerCollectedEnoughCheese = secondPlayerCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        return !hasAnyUserLost() && (collectedEnoughCheese || secondPlayerCollectedEnoughCheese);
    }

    public boolean hasAnyUserLost() {
        return isCatAtLocation(playerLocation) || isCatAtLocation(pcCellLocation);
    }

    private boolean isSecondPlayerAtLocation(CellLocation cell) {
        return pcCellLocation.equals(cell);
    }

    public boolean hasSecondPlayerWon() {
        return secondPlayerCheeseCollected >= NUM_CHEESE_TO_COLLECT;
    }

    public boolean hasSecondPlayerLost() {
        return isCatAtLocation(pcCellLocation);
    }


    public int getSecondPlayerCheeseCollected() {
        return secondPlayerCheeseCollected;
    }

    public void setSecondPlayerCheeseCollected(int secondPlayerCheeseCollected) {
        this.secondPlayerCheeseCollected = secondPlayerCheeseCollected;
    }

    public static int getNumCheeseToCollect() {
        return NUM_CHEESE_TO_COLLECT;
    }


    public CellLocation getCheeseLocation() {
        return cheeseLocation;
    }

    public void setCheeseLocation(CellLocation cheeseLocation) {
        this.cheeseLocation = cheeseLocation;
    }

    @Override
    public boolean hasUserWon() {
        boolean collectedEnoughCheese = numCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        return !hasUserLost() && collectedEnoughCheese;
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
            System.out.println("notify observer");
        }
    }
}
