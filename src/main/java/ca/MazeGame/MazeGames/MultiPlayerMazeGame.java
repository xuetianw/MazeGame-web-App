package ca.MazeGame.MazeGames;

import ca.MazeGame.model.CellLocation;
import ca.MazeGame.model.Direction;
import ca.MazeGame.model.PCPlayer;

public class MultiPlayerMazeGame extends MazeGame {
    public static final int NUM_CHEESE_TO_COLLECT = 5;

    private int secondPlayerCheeseCollected;

    private PCPlayer pcPlayer;

    public PCPlayer getPcPlayer() {
        return pcPlayer;
    }

    public void setPcPlayer(PCPlayer pcPlayer) {
        this.pcPlayer = pcPlayer;
    }

    public MultiPlayerMazeGame() {
        super();
        placeSecondPlayer();
//        setVisibleAroundPlayerCell(secondPlayerLocation);
    }

    public void placeSecondPlayer() {
        pcPlayer = new PCPlayer(this, new CellLocation(boardWidth - 2, boardHeight - 2));
    }

//    public void recordSecondPlayerMove(Direction move) {
//        assert isValidSecondPlayerMove(move);
//        pcPlayer.setLocation(pcPlayer.getLocation().getTargetLocation(move));
//
////        setVisibleAroundPlayerCell(secondPlayerLocation);
//
//        // Compute goal states achieved
//        if (isCheeseAtLocation(pcPlayer.getLocation())) {
//            secondPlayerCheeseCollected++;
//            placeNewCheeseOnBoard();
//        }
////        recordMoveLock.unlock();
//
//    }

    private void placeNewCheeseOnBoard() {
        do {
            cheeseLocation = maze.getRandomLocationInsideMaze();
        } while (isMouseAtLocation(cheeseLocation) || (isSecondPlayerAtLocation(cheeseLocation))
                || maze.isCellAWall(cheeseLocation));
    }


    public boolean isValidSecondPlayerMove(Direction move) {
        CellLocation targetLocation = pcPlayer.getLocation().getTargetLocation(move);
        return maze.isCellOpen(targetLocation);
    }

    public boolean hasAnyUserWon() {
        boolean collectedEnoughCheese = numCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        boolean secondPlayerCollectedEnoughCheese = secondPlayerCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        return !hasAnyUserLost() && (collectedEnoughCheese || secondPlayerCollectedEnoughCheese);
    }

    public boolean hasAnyUserLost() {
        return isCatAtLocation(playerLocation) || isCatAtLocation(pcPlayer.getLocation());
    }

    private boolean isSecondPlayerAtLocation(CellLocation cell) {
        return pcPlayer.getLocation().equals(cell);
    }

    public boolean hasSecondPlayerWon() {
        return secondPlayerCheeseCollected >= NUM_CHEESE_TO_COLLECT;
    }

    public boolean hasSecondPlayerLost() {
        return isCatAtLocation(pcPlayer.getLocation());
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

    public void movePCPlayer() {
        //Todo
    }
}
