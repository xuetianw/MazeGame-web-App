package ca.MazeGame.SInglePlayerThreads;

import ca.MazeGame.MazeGames.MazeGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MazeGameThreadObj {
    private MazeGame singleUserGame;
    private ControlMain controlMain;
    public long gameNumber;

    public MazeGameThreadObj(MazeGame singleUserGame, long gameNumber) {
        this.gameNumber = gameNumber;
        this.singleUserGame = singleUserGame;
        this.controlMain = new ControlMain(singleUserGame);
    }
}
