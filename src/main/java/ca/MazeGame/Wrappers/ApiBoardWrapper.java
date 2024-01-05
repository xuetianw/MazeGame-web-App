package ca.MazeGame.Wrappers;

import ca.MazeGame.model.Cat;
import ca.MazeGame.model.CellLocation;
import ca.MazeGame.MazeGames.MazeGame;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiBoardWrapper {
    protected int boardWidth;
    protected int boardHeight;
    protected ApiLocationWrapper firstUserLocation;
    protected ApiLocationWrapper cheeseLocation;
    protected List<ApiLocationWrapper> catLocations;
    protected boolean[][] hasWalls;
    protected boolean[][] isVisible;


    private void setWidths() {
        boardHeight = MazeGame.getBoardHeight();
        boardWidth = MazeGame.getBoardWidth();
    }

    protected void setVisibilityArray() {
        isVisible = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                isVisible[i][j] = true;
            }
        }
    }


    private void place_cat(MazeGame game) {
        List<CellLocation> locations = new ArrayList<>();
        for (Cat cat : game.getCats()) {
            locations.add(cat.getLocation());
        }
        catLocations = ApiLocationWrapper.makeFromCellLocations(locations);
    }

    protected void place_wall(MazeGame game) {
        hasWalls = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                hasWalls[i][j] = game.getMaze().getBoard()[i][j].isWall();
                isVisible[i][j] = game.getMaze().getBoard()[i][j].isVisible();
            }
        }
    }


    public static ApiBoardWrapper processMaze(MazeGame game) {
        ApiBoardWrapper apiBoardWrapper = new ApiBoardWrapper();
        apiBoardWrapper.setWidths();
        apiBoardWrapper.firstUserLocation = ApiLocationWrapper.makeFromCellLocation(game.playerLocation);
        apiBoardWrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheeseLocation());
        apiBoardWrapper.setVisibilityArray();
        apiBoardWrapper.place_wall(game);
        apiBoardWrapper.place_cat(game);

        return apiBoardWrapper;
    }
}
