package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class ApiGameWrapper {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;
    public ApiBoardWrapper apiBoardWrapper;

    public ApiGameWrapper(long id) {
        isGameWon = false;
        isGameLost = false;
        numCheeseFound = 0;
        numCheeseGoal = 1;
        apiBoardWrapper = new ApiBoardWrapper();
        gameNumber = id;
    }




    public void move(String new_move) {
        if (isGameLost || isGameWon) {
            throw new BadRequestException("game finished");
        }
        ApiLocationWrapper newLocation;
        ApiLocationWrapper mouseLocation = apiBoardWrapper.mouseLocation;
        System.out.println("111111111111111111111111111111111\n");
        switch (new_move) {
            case "MOVE_LEFT":
                newLocation = new ApiLocationWrapper(mouseLocation.x - 1, mouseLocation.y);
                break;
            case "MOVE_UP":
                newLocation = new ApiLocationWrapper(mouseLocation.x, mouseLocation.y + 1);
                break;
            case "MOVE_RIGHT":
                newLocation = new ApiLocationWrapper(mouseLocation.x + 1, mouseLocation.y);
                break;
            case "MOVE_DOWN":
                newLocation = new ApiLocationWrapper(mouseLocation.x, mouseLocation.y - 1);
                break;
            default:
                throw new BadRequestException("not from other ways");
        }


        if (apiBoardWrapper.is_at_wall(newLocation)) {
            throw new InvalidMoveException("new location on the wall");
        }

//        if (newLocation.equals(apiBoardWrapper.cheeseLocation)) {
//            numCheeseFound++;
//            if (numCheeseFound == numCheeseGoal) {
//                isGameWon = true;
//                apiBoardWrapper.cheeseLocation = null;
//                moveCat();
//            } else {
//
//            }
//        }
//        if (newLocation.equals()) {
//
//        }

        mouseLocation.x = newLocation.x;
        mouseLocation.y = newLocation.y;

    }

    private void moveCat() {
        List<ApiLocationWrapper> newCatLoc = apiBoardWrapper.catLocations;
        while (!isGameWon && !isGameLost) {
            for (ApiLocationWrapper apiLocationWrapper : apiBoardWrapper.catLocations) {

            }
        }

    }
}
