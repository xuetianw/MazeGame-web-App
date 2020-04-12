package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;

public class CellLocation {
    public int x;

    public int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public Direction previous_move = Direction.NOT_MOVING;

    public CellLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CellLocation getTargetLocation(Direction direction) {
        switch (direction) {
            case MOVE_LEFT:
                return new CellLocation(x - 1, y);
            case MOVE_UP:
                return new CellLocation(x, y - 1);
            case MOVE_RIGHT:
                return new CellLocation(x + 1, y);
            case MOVE_DOWN:
                return new CellLocation(x, y + 1);
            default:
                throw new BadRequestException("not from other ways");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != CellLocation.class) {
            return false;
        }

        CellLocation other = (CellLocation) o;
        return x == other.x && y == other.y;
    }
}
