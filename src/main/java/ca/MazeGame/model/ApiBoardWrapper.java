package ca.MazeGame.model;

import java.util.ArrayList;
import java.util.List;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    public ApiBoardWrapper() {
        boardWidth = 10;
        boardHeight = 10;
        placeMouse();
        placeCheese();
        setVisibilityArray();
        place_cat();
        place_wall();
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
        mouseLocation = new ApiLocationWrapper(3, 4);
    }

    private void placeCheese() {
        cheeseLocation = new ApiLocationWrapper(4,5);
    }

    private void place_cat() {
        catLocations = new ArrayList<>();
        ApiLocationWrapper cat_loc = new ApiLocationWrapper(3, 3);
        catLocations.add(cat_loc);
    }

    private void place_wall() {
        hasWalls = new boolean[boardWidth][boardHeight];
        for (int i = 0; i < boardHeight; i++) {
            hasWalls[0][i] = true;
            hasWalls[boardWidth - 1][i] = true;
        }

        for (int i = 0; i < boardWidth; i++) {
            hasWalls [i][0] = true;
            hasWalls [i][boardHeight - 1] = true;
        }
    }



    public boolean is_at_wall(ApiLocationWrapper apiLocationWrapper) {
        return hasWalls[apiLocationWrapper.x][apiLocationWrapper.y];
    }
}
