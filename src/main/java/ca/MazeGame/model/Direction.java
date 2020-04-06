package ca.MazeGame.model;

public enum Direction {
    MOVE_LEFT,
    MOVE_UP,
    MOVE_RIGHT,
    MOVE_DOWN,
    NOT_MOVING;

    public Direction getOppositeMove() {
        switch (this) {
            case MOVE_UP:    return MOVE_DOWN;
            case MOVE_DOWN:  return MOVE_UP;
            case MOVE_LEFT:  return MOVE_RIGHT;
            case MOVE_RIGHT: return MOVE_LEFT;
            default:
                return NOT_MOVING;
        }
    }
}