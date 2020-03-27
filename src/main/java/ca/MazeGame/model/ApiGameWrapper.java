package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;

import java.util.List;
import java.util.Random;


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

        if (new_move.equals("MOVE_CATS")) {
            moveCat();
            if (checkStepOnCats()) return;
            return;
        }

        switch (new_move) {
            case "MOVE_LEFT":
                newLocation = new ApiLocationWrapper(mouseLocation.x - 1, mouseLocation.y);
                break;
            case "MOVE_UP":
                newLocation = new ApiLocationWrapper(mouseLocation.x, mouseLocation.y - 1);
                break;
            case "MOVE_RIGHT":
                newLocation = new ApiLocationWrapper(mouseLocation.x + 1, mouseLocation.y);
                break;
            case "MOVE_DOWN":
                newLocation = new ApiLocationWrapper(mouseLocation.x, mouseLocation.y + 1);
                break;
            default:
                throw new BadRequestException("not from other ways");
        }


        if (apiBoardWrapper.is_at_wall(newLocation)) {
            throw new InvalidMoveException("new location on the wall");
        }

        mouseLocation.x = newLocation.x;
        mouseLocation.y = newLocation.y;

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

                do {
                    newLoc = findNextMove(catLoc);
                } while (apiBoardWrapper.is_at_wall(newLoc) || newLoc.equals(apiBoardWrapper.cheeseLocation));

                catLoc.x = newLoc.x;
                catLoc.y = newLoc.y;
            } else {
                ApiLocationWrapper newLoc;
                if (catLoc.previous_move == Direction.MOVE_LEFT) {
                    newLoc = new ApiLocationWrapper(catLoc.x - 1, catLoc.y);
                } else if (catLoc.previous_move == Direction.MOVE_UP) {
                    newLoc = new ApiLocationWrapper(catLoc.x, catLoc.y - 1);
                } else if (catLoc.previous_move == Direction.MOVE_RIGHT) {
                    newLoc = new ApiLocationWrapper(catLoc.x + 1, catLoc.y);
                } else {
                    newLoc = new ApiLocationWrapper(catLoc.x, catLoc.y + 1);
                }
                while (apiBoardWrapper.is_at_wall(newLoc) || newLoc.equals(apiBoardWrapper.cheeseLocation)) {
                    newLoc = findNextMove(catLoc);
                }
                catLoc.x = newLoc.x;
                catLoc.y = newLoc.y;
            }
        }

    }

    private ApiLocationWrapper findNextMove(ApiLocationWrapper catLoc) {
        ApiLocationWrapper newLoc;// create instance of Random class
        Random rand = new Random(System.currentTimeMillis());

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(4); // numbers generated [0,1,2,3]

        if (rand_int1 == 0) {
            newLoc = new ApiLocationWrapper(catLoc.x - 1, catLoc.y);
            catLoc.previous_move = Direction.MOVE_LEFT;
        } else if (rand_int1 == 1) {
            newLoc = new ApiLocationWrapper(catLoc.x, catLoc.y - 1);
            catLoc.previous_move = Direction.MOVE_UP;
        } else if (rand_int1 == 2) {
            newLoc = new ApiLocationWrapper(catLoc.x + 1, catLoc.y);
            catLoc.previous_move = Direction.MOVE_RIGHT;
        } else {
            newLoc = new ApiLocationWrapper(catLoc.x, catLoc.y + 1);
            catLoc.previous_move = Direction.MOVE_DOWN;
        }
        return newLoc;
    }
}
