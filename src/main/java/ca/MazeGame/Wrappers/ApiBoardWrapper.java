package ca.MazeGame.Wrappers;

import ca.MazeGame.model.Cat;
import ca.MazeGame.model.CellLocation;
import ca.MazeGame.MazeGames.MazeGame;

import java.util.ArrayList;
import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;
    private MazeGame game;

    public ApiBoardWrapper(MazeGame game) {
        setWidths();
        this.game = game;
    }

    private void setWidths() {
        boardHeight = MazeGame.getBoardHeight();
        boardWidth = MazeGame.getBoardWidth();
    }

    private void setVisibilityArray() {
        isVisible = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                isVisible[i][j] = true;
            }
        }
    }

    private void placeMouse() {
        this.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.playerLocation);
    }

    private void placeCheese() {
        this.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheeseLocation());
    }

    private void place_cat() {
        List<CellLocation> locations = new ArrayList<>();
        for (Cat cat : game.getCats()) {
            locations.add(cat.getLocation());
        }
        catLocations = ApiLocationWrapper.makeFromCellLocations(locations);
    }

    private void place_wall() {
        hasWalls = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                hasWalls[i][j] = game.getMaze().getBoard()[i][j].isWall();
                isVisible[i][j] = game.getMaze().getBoard()[i][j].isVisible();
            }
        }
    }

    public ApiBoardWrapper processMaze() {
        placeMouse();
        placeCheese();
        setVisibilityArray();
        place_cat();
        place_wall();
        return this;
    }


    public static ApiBoardWrapper processMaze(MazeGame game) {
        ApiBoardWrapper apiBoardWrapper = new ApiBoardWrapper(game);
        apiBoardWrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.playerLocation);
        apiBoardWrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheeseLocation());
        apiBoardWrapper.setVisibilityArray();
        apiBoardWrapper.place_wall();
        apiBoardWrapper.place_cat();

        return apiBoardWrapper;
    }
}
