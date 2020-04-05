package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    public Direction previous_move = Direction.NOT_MOVING;

    public ApiLocationWrapper(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
//    public static ApiLocationWrapper makeFromCellLocation(CellLocation cell) {
//        ApiLocationWrapper location = new ApiLocationWrapper();
//
//        // Populate this object!
//
//        return location;
//    }
//    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
//    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<CellLocation> cells) {
//        List<ApiLocationWrapper> locations = new ArrayList<>();
//
//        // Populate this object!
//
//        return locations;
//    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != ApiLocationWrapper.class) {
            return false;
        }

        ApiLocationWrapper other = (ApiLocationWrapper) o;
        return x == other.x && y == other.y;
    }

    public ApiLocationWrapper getTargetLocation(Direction direction) {
        switch (direction) {
            case MOVE_LEFT:
                return new ApiLocationWrapper(x - 1, y);
            case MOVE_UP:
                return new ApiLocationWrapper(x, y - 1);
            case MOVE_RIGHT:
                return new ApiLocationWrapper(x + 1, y);
            case MOVE_DOWN:
                return new ApiLocationWrapper(x, y + 1);
            default:
                throw new BadRequestException("not from other ways");
        }
    }
    public static List<Direction> getPossibleMoves() {
        List<Direction> list = new ArrayList<>();
        list.add(Direction.MOVE_LEFT);
        list.add(Direction.MOVE_UP);
        list.add(Direction.MOVE_RIGHT);
        list.add(Direction.MOVE_DOWN);
        return list;
    }
}
