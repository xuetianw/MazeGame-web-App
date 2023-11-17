package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultiPLayerMazeGameThreadObj {
    private MultiPlayerMazeGame multiPlayerMazeGame;
    private MultiPlayersControlMain multiPlayersControlMain;
    public long gameNumber;

    public MultiPLayerMazeGameThreadObj(MultiPlayerMazeGame multiPlayerMazeGame, long gameNumber) {
        this.gameNumber = gameNumber;
        this.multiPlayerMazeGame = multiPlayerMazeGame;
        this.multiPlayersControlMain = new MultiPlayersControlMain(multiPlayerMazeGame);
    }
}
