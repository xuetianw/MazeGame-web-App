package ca.MazeGame.MazeGames;

import ca.MazeGame.model.Cat;
import ca.MazeGame.model.CellLocation;
import ca.MazeGame.model.Direction;
import ca.MazeGame.model.Maze;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MazeGame {
    public static final int NUM_CHEESE_TO_COLLECT = 1;
    protected int numCheeseCollected;

    @Getter
    protected static int boardWidth = 15;
    @Getter
    protected static int boardHeight = 15;
    protected final Maze maze = new Maze(boardWidth, boardHeight);

    protected CellLocation cheeseLocation;
    public CellLocation playerLocation;

    private final List<Cat> cats = new ArrayList<>();


    public static int getNumCheeseToCollect() {
        return NUM_CHEESE_TO_COLLECT;
    }

    public MazeGame() {
        placePlayer();
        placeNewCheeseOnBoard();
        placeCatsOnBoard();
        setVisibleAroundPlayerCell(playerLocation);
    }

    public void placePlayer() {
        playerLocation = new CellLocation(1, 1);
    }

    public void setVisibleAroundPlayerCell(CellLocation location) {
        CellLocation up = location.getTargetLocation(Direction.MOVE_UP);
        CellLocation down = location.getTargetLocation(Direction.MOVE_DOWN);
        CellLocation right = location.getTargetLocation(Direction.MOVE_RIGHT);
        CellLocation left = location.getTargetLocation(Direction.MOVE_LEFT);

        // Current cell, Up, Down, Right, Left.
        maze.recordCellVisible(location);
        maze.recordCellVisible(up);
        maze.recordCellVisible(down);
        maze.recordCellVisible(right);
        maze.recordCellVisible(left);

        // 45' Angles
        maze.recordCellVisible(up.getTargetLocation(Direction.MOVE_RIGHT));
        maze.recordCellVisible(up.getTargetLocation(Direction.MOVE_LEFT));
        maze.recordCellVisible(down.getTargetLocation(Direction.MOVE_RIGHT));
        maze.recordCellVisible(down.getTargetLocation(Direction.MOVE_LEFT));
    }

    protected void placeCatsOnBoard() {
//        cats.clear();
        CellLocation cat;
        do {
            cat = maze.getRandomLocationInsideMaze();
        } while (isMouseAtLocation(cheeseLocation)
                || maze.isCellAWall(cheeseLocation));
        cats.add(new Cat(this, cat));
    }

    private void placeNewCheeseOnBoard() {
        do {
            cheeseLocation = maze.getRandomLocationInsideMaze();
        } while (isMouseAtLocation(cheeseLocation)
                || maze.isCellAWall(cheeseLocation));
    }

    public boolean isMouseAtLocation(CellLocation cell) {
        return playerLocation.equals(cell);
    }
    public void moveCat() {
//        System.out.println("cat moved\n");
        for (Cat cat : cats) {
            cat.doMove();
        }
        cats.forEach(Cat::doMove);
    }

    public void recordPlayerMove(Direction move) {
        assert isValidPlayerMove(move);
        playerLocation = playerLocation.getTargetLocation(move);

        setVisibleAroundPlayerCell(playerLocation);

        // Compute goal states achieved
        if (isCheeseAtLocation(playerLocation)) {
            numCheeseCollected++;
            placeNewCheeseOnBoard();
        }
    }


    public boolean isCellOpen(CellLocation cell) {
        return maze.isCellOpen(cell);
    }


    public boolean isValidPlayerMove(Direction move) {
        CellLocation targetLocation = playerLocation.getTargetLocation(move);
        return maze.isCellOpen(targetLocation);
    }

    public boolean isCheeseAtLocation(CellLocation cell) {
        return cheeseLocation != null && cheeseLocation.equals(cell);
    }
    public boolean hasUserWon() {
        boolean collectedEnoughCheese = numCheeseCollected >= NUM_CHEESE_TO_COLLECT;
        return !hasUserLost() && collectedEnoughCheese;
    }
    public boolean hasUserLost() {
        return isCatAtLocation(playerLocation);
    }

    public boolean isCatAtLocation(CellLocation cell) {
        for (Cat cat : cats) {
            CellLocation catLocation = cat.getLocation();
            if (catLocation.equals(cell)) {
                return true;
            }
        }
        return false;
    }

    public void displayBoard() {
        for (int y = 0; y < MazeGame.boardHeight; y++) {
            for (int x = 0; x < MazeGame.boardWidth; x++) {
                CellLocation cell = new CellLocation(x, y);
                maze.recordCellVisible(cell);
            }
        }
    }
}
