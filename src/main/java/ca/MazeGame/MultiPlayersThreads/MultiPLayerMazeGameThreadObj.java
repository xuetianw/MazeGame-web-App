package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.MazeGames.MultiPlayerMazeGame;

public class MultiPLayerMazeGameThreadObj {
    private MultiPlayerMazeGame multiPlayerMazeGame;
    private MultiPlayersControlMain multiPlayersControlMain;
    public long gameNumber;

    public MultiPLayerMazeGameThreadObj(MultiPlayerMazeGame multiPlayerMazeGame, long gameNumber) {
        this.gameNumber = gameNumber;
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        this.multiPlayersControlMain = new MultiPlayersControlMain(multiPlayerMazeGame);
    }

    public MultiPlayerMazeGame getMultiPlayerMazeGame() {
        return multiPlayerMazeGame;
    }

    public void setMultiPlayerMazeGame(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
    }

    public MultiPlayersControlMain getMultiPlayersMainControl() {
        return multiPlayersControlMain;
    }

    public void setMultiPlayersMainThread(MultiPlayersControlMain multiPlayersControlMain) {
        this.multiPlayersControlMain = multiPlayersControlMain;
    }
}
