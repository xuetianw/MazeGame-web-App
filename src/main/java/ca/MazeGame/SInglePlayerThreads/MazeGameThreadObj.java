package ca.MazeGame.SInglePlayerThreads;

import ca.MazeGame.MazeGames.MazeGame;

public class MazeGameThreadObj {
    private MazeGame singleUserGame;
    private ControlMain controlMain;
    public long gameNumber;

    public MazeGameThreadObj(MazeGame singleUserGame, long gameNumber) {
        this.gameNumber = gameNumber;
        this.singleUserGame = singleUserGame;
        this.controlMain = new ControlMain(singleUserGame);
    }

    public MazeGame getSingleUserGame() {
        return singleUserGame;
    }

    public void setMultiPlayerMazeGame(MazeGame mazeGame) {
        this.singleUserGame = mazeGame;
    }

    public ControlMain getMainControl() {
        return controlMain;
    }

    public void setMainThread(ControlMain controlMain) {
        this.controlMain = controlMain;
    }
}
