package ca.MazeGame.model;

import java.util.ArrayList;
import java.util.List;

public class MazeGame {
    public static final int NUM_CHEESE_TO_COLLECT = 1;
    private int numCheeseCollected;

    private static int boardWidth = 15;
    private static int boardHeight = 15;
    private final Maze maze = new Maze(boardWidth, boardHeight);

    private CellLocation cheeseLocation;
    public static CellLocation playerLocation;


    public CellLocation getCheeseLocation() {
        return cheeseLocation;
    }

    public void setCheeseLocation(CellLocation cheeseLocation) {
        this.cheeseLocation = cheeseLocation;
    }

    public int getNumCheeseCollected() {
        return numCheeseCollected;
    }

    public void setNumCheeseCollected(int numCheeseCollected) {
        this.numCheeseCollected = numCheeseCollected;
    }


    public static int getNumCheeseToCollect() {
        return NUM_CHEESE_TO_COLLECT;
    }

    public static void setBoardWidth(int boardWidth) {
        MazeGame.boardWidth = boardWidth;
    }

    public static void setBoardHeight(int boardHeight) {
        MazeGame.boardHeight = boardHeight;
    }

    public static int getBoardWidth() {
        return boardWidth;
    }

    public static int getBoardHeight() {
        return boardHeight;
    }


    public List<Cat> getCats() {
        return cats;
    }

    public final List<Cat> cats = new ArrayList<>();

    public Maze getMaze() {
        return maze;
    }


    public MazeGame() {
        placePlayer();
        placeNewCheeseOnBoard();
        placeCatsOnBoard();
        setVisibleAroundPlayerCell();
    }

    private void placePlayer() {
        playerLocation = new CellLocation(1, 1);
    }

    private void setVisibleAroundPlayerCell() {
        CellLocation up = playerLocation.getTargetLocation(Direction.MOVE_UP);
        CellLocation down = playerLocation.getTargetLocation(Direction.MOVE_DOWN);
        CellLocation right = playerLocation.getTargetLocation(Direction.MOVE_RIGHT);
        CellLocation left = playerLocation.getTargetLocation(Direction.MOVE_LEFT);

        // Current cell, Up, Down, Right, Left.
        maze.recordCellVisible(playerLocation);
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

    private void placeCatsOnBoard() {
        cats.clear();
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
    }

    public void recordPlayerMove(Direction move) {
        assert isValidPlayerMove(move);
        playerLocation = playerLocation.getTargetLocation(move);

        setVisibleAroundPlayerCell();

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
        for (int y = 0; y < MazeGame.getBoardHeight(); y++) {
            for (int x = 0; x < MazeGame.getBoardWidth(); x++) {
                CellLocation cell = new CellLocation(x, y);
                maze.recordCellVisible(cell);
            }
        }
    }
}
