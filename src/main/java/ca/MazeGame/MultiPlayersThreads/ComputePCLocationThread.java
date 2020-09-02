package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.Graph.Graph;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.model.CellLocation;
import ca.MazeGame.model.CellState;
import ca.MazeGame.model.Maze;

import java.util.List;

public class ComputePCLocationThread implements Runnable {

    private final MultiPlayerMazeGame multiPlayerMazeGame;
    private CellLocation lastCheeseLoc = new CellLocation(-1, -1);

    private int timeInterval = 200;

    private Graph g;

    private boolean threadStop = false;

    private CellLocation[] playerLocArray;
    private int pcPlayerLocArrInd = 0;

    public ComputePCLocationThread(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        addGraphEdges();
    }

    @Override
    public void run() {
        while (!multiPlayerMazeGame.hasAnyUserWon() && !multiPlayerMazeGame.hasAnyUserLost() && !threadStop) {
            try {
                System.out.println("running");
                //todo add observer instead of using if condition
                if (!lastCheeseLoc.equals(multiPlayerMazeGame.getCheeseLocation())) {
                    System.out.println("new cheese placed");
                    lastCheeseLoc = multiPlayerMazeGame.getCheeseLocation();
                    calculateArray();
                }
                setPCPlayerLoc();
                doWonOrLost();
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void doWonOrLost() {
//        System.out.println("called");
        if (multiPlayerMazeGame.hasAnyUserWon()) {
            revealBoard();
        } else if (multiPlayerMazeGame.hasAnyUserLost()) {
            revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        multiPlayerMazeGame.displayBoard();
    }



    private void addGraphEdges() {
        g = new Graph(MultiPlayerMazeGame.getBoardHeight() * MultiPlayerMazeGame.getBoardWidth());

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
                        if (!board[tarX][tarY].isWall()) {
                            int graphVertexIndex = convertLocToGraphVertexIndex(cellLocation);
                            int graphTargetVertexIndex = convertLocToGraphVertexIndex(targetCellLocation);
//                            System.out.println(graphVertexIndex);
                            g.addEdge(graphVertexIndex, graphTargetVertexIndex);
                        }
                    }
                }
            }
        }
    }


    private void calculateArray() {
        System.out.println("calculating array");
        pcPlayerLocArrInd = 0;
        int secondPLayerGraphVertexIndex = convertLocToGraphVertexIndex(multiPlayerMazeGame.getPCCellLocation());
        CellLocation cheeseLoc = multiPlayerMazeGame.getCheeseLocation();
        int cheeseGraphLoc = convertLocToGraphVertexIndex(cheeseLoc);

        List<Integer> pathIndices = g.getFirstPath(secondPLayerGraphVertexIndex, cheeseGraphLoc);
        convertGraphIndicesToLocs(pathIndices);
        System.out.println("calculating array finished");
    }


    private void setPCPlayerLoc() {
        System.out.println("setPCPlayerLoc");
        if (pcPlayerLocArrInd < playerLocArray.length) {
            multiPlayerMazeGame.recordPCPlayerLoc(playerLocArray[pcPlayerLocArrInd++]);
            System.out.printf("pcPlayerLocArrInd : %d: out of %d \n", pcPlayerLocArrInd, playerLocArray.length);
        } else {
            System.out.println("in chesse location");
        }
    }



    private void convertGraphIndicesToLocs(List<Integer> indices) {
        playerLocArray = new CellLocation[indices.size()];
        int width = MultiPlayerMazeGame.getBoardWidth();
        for (int i = 0; i < indices.size(); i++) {
            int x = indices.get(i) % width;
            int y = indices.get(i) / width;
            playerLocArray[i] = new CellLocation(x, y);
        }
    }

    private int convertLocToGraphVertexIndex(CellLocation cellLocation) {
        return cellLocation.y * MultiPlayerMazeGame.getBoardWidth() + cellLocation.x;
    }

    private int[][] getDirections() {
        return new int[][] {{0, 1}, {0, -1}, {-1, 0}, {1, 0} };
    }

    public void setPCThreadStop(boolean b) {
        threadStop = true;
    }
}
