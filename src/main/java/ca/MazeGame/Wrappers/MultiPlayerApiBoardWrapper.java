package ca.MazeGame.Wrappers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.model.Cat;
import ca.MazeGame.model.CellLocation;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper firstUserLocation;
    public ApiLocationWrapper secondUserLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;
    public MazeGame game;

    public MultiPlayerApiBoardWrapper(MazeGame game) {
        setWidths();
        this.game = game;
    }

    private void placeSecondMouse() {
        secondUserLocation = ApiLocationWrapper.makeFromCellLocation(MultiPlayerMazeGame.secondPlayerLocation);
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
        this.firstUserLocation = ApiLocationWrapper.makeFromCellLocation(MazeGame.playerLocation);
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

    public MultiPlayerApiBoardWrapper processMaze() {
        placeMouse();
        placeCheese();
        setVisibilityArray();
        place_cat();
        place_wall();
        placeSecondMouse();
        return this;
    }
}
