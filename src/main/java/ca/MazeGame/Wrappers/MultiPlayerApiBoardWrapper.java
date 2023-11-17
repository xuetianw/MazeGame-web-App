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

//    public MultiPlayerApiBoardWrapper(MultiPlayerMazeGame game) {
//        setWidths();
//        this.game = game;
//    }

    private void placeSecondMouse(MultiPlayerMazeGame game) {
        secondUserLocation = ApiLocationWrapper.makeFromCellLocation(game.getPCCellLocation());
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

    private void placeMouse(MultiPlayerMazeGame game) {
        this.firstUserLocation = ApiLocationWrapper.makeFromCellLocation(game.playerLocation);
    }

    private void placeCheese(MultiPlayerMazeGame game) {
        this.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheeseLocation());
    }

    private void place_cat(MultiPlayerMazeGame game) {
        List<CellLocation> locations = new ArrayList<>();
        for (Cat cat : game.getCats()) {
            locations.add(cat.getLocation());
        }
        catLocations = ApiLocationWrapper.makeFromCellLocations(locations);
    }

    private void place_wall(MultiPlayerMazeGame game) {
        hasWalls = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                hasWalls[i][j] = game.getMaze().getBoard()[i][j].isWall();
                isVisible[i][j] = game.getMaze().getBoard()[i][j].isVisible();
            }
        }
    }

//    public MultiPlayerApiBoardWrapper processMaze() {
//        placeMouse();
//        placeCheese();
//        setVisibilityArray();
//        place_cat();
//        place_wall();
//        placeSecondMouse();
//        return this;
//    }

    public static MultiPlayerApiBoardWrapper processMaze(MultiPlayerMazeGame multiPlayerMazeGame, int id) {
        MultiPlayerApiBoardWrapper multiPlayerApiBoardWrapper = new MultiPlayerApiBoardWrapper();
        multiPlayerApiBoardWrapper.setWidths();

        multiPlayerApiBoardWrapper.placeMouse(multiPlayerMazeGame);
        multiPlayerApiBoardWrapper.placeCheese(multiPlayerMazeGame);
        multiPlayerApiBoardWrapper.setVisibilityArray();
        multiPlayerApiBoardWrapper.place_cat(multiPlayerMazeGame);
        multiPlayerApiBoardWrapper.place_wall(multiPlayerMazeGame);
        multiPlayerApiBoardWrapper.placeSecondMouse(multiPlayerMazeGame);

        return multiPlayerApiBoardWrapper;
    }
}
