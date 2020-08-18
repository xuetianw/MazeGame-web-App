package ca.MazeGame.MazeGames;

import ca.MazeGame.model.CellLocation;
import ca.MazeGame.model.Direction;
import ca.MazeGame.model.Maze;

public class MultiPlayerMazeGame extends MazeGame {
    public static final int NUM_CHEESE_TO_COLLECT = 5;

    public int secondPlayerCheeseCollected;

    public static CellLocation secondPlayerLocation;

    public MultiPlayerMazeGame() {
        super();
        placeSecondPlayer();
        setVisibleAroundPlayerCell(secondPlayerLocation);
    }

    public void placeSecondPlayer() {
        secondPlayerLocation = new CellLocation(boardWidth - 2, boardHeight - 2);
    }

    public void recordSecondPlayerMove(Direction move) {
        assert isValidSecondPlayerMove(move);
        secondPlayerLocation = secondPlayerLocation.getTargetLocation(move);

        setVisibleAroundPlayerCell(secondPlayerLocation);

        // Compute goal states achieved
        if (isCheeseAtLocation(secondPlayerLocation)) {
            secondPlayerCheeseCollected++;
            placeNewCheeseOnBoard();
        }

    }

    private void placeNewCheeseOnBoard() {
        do {
            cheeseLocation = maze.getRandomLocationInsideMaze();
        } while (isMouseAtLocation(cheeseLocation) || (isSecondPlayerAtLocation(cheeseLocation))
                || maze.isCellAWall(cheeseLocation));
    }


    public boolean isValidSecondPlayerMove(Direction move) {
        CellLocation targetLocation = secondPlayerLocation.getTargetLocation(move);
        return maze.isCellOpen(targetLocation);
    }

    public boolean hasAnyUserWon() {
        boolean collectedEnoughCheese = numCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        boolean secondPlayerCollectedEnoughCheese = secondPlayerCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        return !hasAnyUserLost() && (collectedEnoughCheese || secondPlayerCollectedEnoughCheese);
    }

    public boolean hasAnyUserLost() {
        return isCatAtLocation(playerLocation) || isCatAtLocation(secondPlayerLocation);
    }

    private boolean isSecondPlayerAtLocation(CellLocation cell) {
        return secondPlayerLocation.equals(cell);
    }
}
