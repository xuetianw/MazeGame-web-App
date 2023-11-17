package ca.MazeGame.Wrappers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.model.Cat;
import ca.MazeGame.model.CellLocation;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerApiBoardWrapper extends ApiBoardWrapper {

    public ApiLocationWrapper secondUserLocation;

    private void placeSecondMouse(MultiPlayerMazeGame game) {
        secondUserLocation = ApiLocationWrapper.makeFromCellLocation(game.getPCCellLocation());
    }

    private void setWidths() {
        boardHeight = MazeGame.getBoardHeight();
        boardWidth = MazeGame.getBoardWidth();
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
