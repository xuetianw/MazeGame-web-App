package ca.MazeGame.model;

public class CellState {
    private boolean isVisible = false;
    private boolean isWall = false;

    public CellState(boolean isWall) {
        this.isWall = isWall;
    }
    public CellState(boolean isWall, boolean isVisible) {
        this.isWall = isWall;
        this.isVisible = isVisible;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isVisible() {
        return isVisible;
    }
    public boolean isHidden() {
        return !isVisible;
    }

    // Create new instance based on current state (Immutable)
    public CellState makeVisible() {
        return new CellState(isWall, true);
    }
}
