package ca.MazeGame.model;

import ca.MazeGame.Graph.Graph;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;

import java.util.ArrayList;
import java.util.List;

public class PCPlayer {
    private final MultiPlayerMazeGame multiPlayerMazeGame;
    private CellLocation location;
    List<CellLocation> pathsLocations;

    public CellLocation getLocation() {
        return location;
    }

    public void setLocation(CellLocation location) {
        this.location = location;
    }

    public PCPlayer(MultiPlayerMazeGame multiPlayerMazeGame, CellLocation location) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        this.location = location;
    }



    private List<CellLocation> calculatePaths() {
        int innerMazeHeight = MultiPlayerMazeGame.getBoardHeight() - 2;
        int innerMazeWidth = MultiPlayerMazeGame.getBoardWidth() - 2;

        Graph g = new Graph(innerMazeHeight * innerMazeWidth);
        Maze maze = multiPlayerMazeGame.getMaze();
        CellState[][] board = maze.getBoard();
        int[][] directions = getDirections();

        for (int i = 1; i <= board.length - 2; i++) {
            for (int j = 1; j <= board[0].length - 2; j++) {
                if (!board[i][j].isWall()) {
                    for (int[] direction : directions) {
                        int tarX = i + direction[0];
                        int tarY = j + direction[1];
                        CellLocation cellLocation = new CellLocation(i, j);
                        CellLocation targetCellLocation = new CellLocation(tarX, tarY);
                        if (board[tarX][tarY].isWall()) {
                            int graphVertexIndex = convertLocToGraphVertexIndex(cellLocation);
                            int graphTargetVertexIndex = convertLocToGraphVertexIndex(targetCellLocation);
                            g.addEdge(graphVertexIndex, graphTargetVertexIndex);
                        }
                    }
                }
            }
        }

        int secondPLayerGraphVertexIndex = convertLocToGraphVertexIndex(location);
        CellLocation cheeseLoc = multiPlayerMazeGame.getCheeseLocation();
        int cheeseGraphLoc = convertLocToGraphVertexIndex(cheeseLoc);

        List<Integer> pathIndices = g.getFirstPath(secondPLayerGraphVertexIndex, cheeseGraphLoc);
        return convertGraphIndicesToLocs(pathIndices);
    }

    private void doSecondPlayerMove() {
        pathsLocations = calculatePaths();
        for (CellLocation pathsLocation : pathsLocations) {
            location = pathsLocation;
        }
    }


    private List<CellLocation> convertGraphIndicesToLocs(Iterable<Integer> indices) {
        List<CellLocation> list = new ArrayList<>();
        int innerMazeWidth = MultiPlayerMazeGame.getBoardWidth() - 2;
        for (Integer integer : indices) {
            int x = integer % innerMazeWidth;
            int y = integer / innerMazeWidth;
            list.add(new CellLocation(x, y));
        }

        return list;
    }

    private int convertLocToGraphVertexIndex(CellLocation cellLocation) {
        int innerMazeWidth = MultiPlayerMazeGame.getBoardWidth() - 2;
        return cellLocation.x * innerMazeWidth + cellLocation.y;
    }

    private int[][] getDirections() {
        return new int[][] {{0, 1}, {0, -1}, {-1, 0}, {1, 0} };
    }
}
