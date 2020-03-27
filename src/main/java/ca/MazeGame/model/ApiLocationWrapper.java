package ca.MazeGame.model;

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
}
