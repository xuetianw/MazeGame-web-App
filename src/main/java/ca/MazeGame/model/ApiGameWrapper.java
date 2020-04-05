package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;

import java.util.Collections;
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
        ApiLocationWrapper targetLocation;
        ApiLocationWrapper mouseLocation = apiBoardWrapper.mouseLocation;

        if (new_move.equals("MOVE_CATS")) {
            moveCat();
            if (checkStepOnCats()) return;
            return;
        }

        switch (new_move) {
            case "MOVE_LEFT":
                targetLocation = mouseLocation.getTargetLocation(Direction.MOVE_LEFT);
                break;
            case "MOVE_UP":
                targetLocation = mouseLocation.getTargetLocation(Direction.MOVE_UP);
                break;
            case "MOVE_RIGHT":
                targetLocation = mouseLocation.getTargetLocation(Direction.MOVE_RIGHT);
                break;
            case "MOVE_DOWN":
                targetLocation = mouseLocation.getTargetLocation(Direction.MOVE_DOWN);
                break;
            default:
                throw new BadRequestException("NoSuchMove");
        }

        if (apiBoardWrapper.is_at_wall(targetLocation)) {
            throw new InvalidMoveException("new location on the wall");
        }

        mouseLocation.x = targetLocation.x;
        mouseLocation.y = targetLocation.y;

        if (checkStepOnCats()) return;

        check_cheese_num();
    }

    private void check_cheese_num() {
        ApiLocationWrapper mouseLocation = apiBoardWrapper.mouseLocation;
        if (mouseLocation.equals(apiBoardWrapper.cheeseLocation)) {
            numCheeseFound++;
            if (numCheeseFound == numCheeseGoal) {
                isGameWon = true;
            }
        }
    }

    private boolean checkStepOnCats() {
        List<ApiLocationWrapper> catLocations = apiBoardWrapper.catLocations;
        for (ApiLocationWrapper catLocationWrapper : catLocations) {
            if(catLocationWrapper.equals(apiBoardWrapper.mouseLocation)) {
                isGameLost = true;
                return true;
            }
        }
        return false;
    }

    private void moveCat() {
        List<ApiLocationWrapper> newCatLocList = apiBoardWrapper.catLocations;
        for (ApiLocationWrapper catLoc : newCatLocList) {
            if (catLoc.previous_move == Direction.NOT_MOVING) {
                ApiLocationWrapper newLoc;

                List<Direction> directions = ApiLocationWrapper.getPossibleMoves();
                Collections.shuffle(directions);
                Direction direction;
                do {
                    direction = pickRandomMove(directions);
                    newLoc = catLoc.getTargetLocation(direction);
                    directions.remove(direction);
                } while (isLocationOccupied(newLoc));

                catLoc.x = newLoc.x;
                catLoc.y = newLoc.y;
                catLoc.previous_move = direction;
            } else {
                ApiLocationWrapper targetLocation;
                targetLocation = catLoc.getTargetLocation(catLoc.previous_move);
                List<Direction> directions = ApiLocationWrapper.getPossibleMoves();
                Collections.shuffle(directions);
                directions.remove(catLoc.previous_move);
                Direction direction;
                while (isLocationOccupied(targetLocation)) {
                    direction = pickRandomMove(directions);
                    directions.remove(direction);
                    targetLocation = catLoc.getTargetLocation(direction);
                    catLoc.previous_move = direction;
                }
                catLoc.x = targetLocation.x;
                catLoc.y = targetLocation.y;
            }
        }
    }

    private boolean isLocationOccupied(ApiLocationWrapper newLoc) {
        return apiBoardWrapper.is_at_wall(newLoc) || newLoc.equals(apiBoardWrapper.cheeseLocation);
    }

    private Direction pickRandomMove(List<Direction> directions) {
        return directions.get(0);
    }
}
