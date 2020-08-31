package ca.MazeGame.MultiPlayersThreads;

import ca.MazeGame.Graph.Graph;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovePCPlayerThread implements Runnable {
    private final MultiPlayerMazeGame multiPlayerMazeGame;

    private int timeInterval = 1000;
    private boolean threadStop = false;

    public MovePCPlayerThread(MultiPlayerMazeGame multiPlayerMazeGame) {
        this.multiPlayerMazeGame = multiPlayerMazeGame;
    }



    @Override
    public void run() {
        while (!multiPlayerMazeGame.hasAnyUserWon() && !multiPlayerMazeGame.hasAnyUserLost() && !threadStop) {
//            System.out.println(game.toString());
            try {
                multiPlayerMazeGame.movePCPlayer();
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

}
